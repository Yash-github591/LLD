from collections import deque

from model.rate_limit_result import RateLimitResult
from limiter.rate_limiter import RateLimiter


class SlidingWindowLogRateLimiter(RateLimiter):

    def allow_request(self, user_id: str) -> RateLimitResult:
        key = f"sliding:{user_id}"

        with self.store.lock_for(key):
            now = self.clock.now()
            window_start = now - self.policy.window_in_seconds

            timestamps = self.store.get(key, deque())

            while timestamps and timestamps[0] <= window_start:
                timestamps.popleft()

            if len(timestamps) >= self.policy.max_requests:
                retry_after = max(
                    1,
                    int(
                        timestamps[0]
                        + self.policy.window_in_seconds
                        - now
                    ) + 1,
                )

                self.store.set(key, timestamps)

                return RateLimitResult(
                    allowed=False,
                    limit=self.policy.max_requests,
                    remaining=0,
                    retry_after_seconds=retry_after,
                    message="Rate limit exceeded",
                )

            timestamps.append(now)
            self.store.set(key, timestamps)

            return RateLimitResult(
                allowed=True,
                limit=self.policy.max_requests,
                remaining=self.policy.max_requests - len(timestamps),
                retry_after_seconds=0,
                message="Request allowed",
            )
