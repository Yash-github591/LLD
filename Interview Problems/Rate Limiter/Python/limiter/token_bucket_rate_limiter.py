from model.rate_limit_result import RateLimitResult
from limiter.rate_limiter import RateLimiter


class TokenBucketRateLimiter(RateLimiter):

    def allow_request(self, user_id: str) -> RateLimitResult:
        key = f"token:{user_id}"

        with self.store.lock_for(key):
            now = self.clock.now()

            state = self.store.get(
                key,
                {
                    "tokens": float(self.policy.max_requests),
                    "last_refill": now,
                },
            )

            elapsed = max(0.0, now - state["last_refill"])
            refill_rate = (
                self.policy.max_requests
                / self.policy.window_in_seconds
            )

            state["tokens"] = min(
                float(self.policy.max_requests),
                state["tokens"] + elapsed * refill_rate,
            )
            state["last_refill"] = now

            if state["tokens"] < 1.0:
                retry_after = max(
                    1,
                    int(
                        (1.0 - state["tokens"])
                        / refill_rate
                    ) + 1,
                )

                self.store.set(key, state)

                return RateLimitResult(
                    allowed=False,
                    limit=self.policy.max_requests,
                    remaining=0,
                    retry_after_seconds=retry_after,
                    message="Rate limit exceeded",
                )

            state["tokens"] -= 1.0
            self.store.set(key, state)

            remaining = int(state["tokens"])

            return RateLimitResult(
                allowed=True,
                limit=self.policy.max_requests,
                remaining=remaining,
                retry_after_seconds=0,
                message="Request allowed",
            )
