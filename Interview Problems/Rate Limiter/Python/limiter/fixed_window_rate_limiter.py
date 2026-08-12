from model.rate_limit_result import RateLimitResult
from limiter.rate_limiter import RateLimiter


class FixedWindowRateLimiter(RateLimiter):

    def allow_request(self, user_id: str) -> RateLimitResult:
        key = f"fixed:{user_id}"

        with self.store.lock_for(key):
            now = self.clock.now()
            window = int(now // self.policy.window_in_seconds)

            state = self.store.get(
                key,
                {"window": window, "count": 0},
            )

            if state["window"] != window:
                state = {"window": window, "count": 0}

            if state["count"] >= self.policy.max_requests:
                window_end = (window + 1) * self.policy.window_in_seconds
                retry_after = max(1, int(window_end - now))

                return RateLimitResult(
                    allowed=False,
                    limit=self.policy.max_requests,
                    remaining=0,
                    retry_after_seconds=retry_after,
                    message="Rate limit exceeded",
                )

            state["count"] += 1
            self.store.set(key, state)

            return RateLimitResult(
                allowed=True,
                limit=self.policy.max_requests,
                remaining=self.policy.max_requests - state["count"],
                retry_after_seconds=0,
                message="Request allowed",
            )
