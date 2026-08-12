from clock.clock import Clock
from enums.rate_limit_type import RateLimitType
from limiter.fixed_window_rate_limiter import FixedWindowRateLimiter
from limiter.rate_limiter import RateLimiter
from limiter.sliding_window_log_rate_limiter import SlidingWindowLogRateLimiter
from limiter.token_bucket_rate_limiter import TokenBucketRateLimiter
from model.rate_limit_policy import RateLimitPolicy
from store.rate_limit_store import RateLimitStore


class RateLimiterFactory:

    @staticmethod
    def create(
        policy: RateLimitPolicy,
        store: RateLimitStore,
        clock: Clock | None = None,
    ) -> RateLimiter:

        if policy.algorithm == RateLimitType.TOKEN_BUCKET:
            return TokenBucketRateLimiter(policy, store, clock)

        if policy.algorithm == RateLimitType.FIXED_WINDOW:
            return FixedWindowRateLimiter(policy, store, clock)

        if policy.algorithm == RateLimitType.SLIDING_WINDOW_LOG:
            return SlidingWindowLogRateLimiter(policy, store, clock)

        raise ValueError(
            f"Unsupported rate limiter algorithm: {policy.algorithm.value}"
        )
