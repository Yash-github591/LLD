from clock.clock import Clock
from enums.rate_limit_type import RateLimitType
from enums.user_tier import UserTier
from factory.rate_limiter_factory import RateLimiterFactory
from model.rate_limit_policy import RateLimitPolicy
from model.rate_limit_result import RateLimitResult
from model.user import User
from store.rate_limit_store import InMemoryRateLimitStore, RateLimitStore


class RateLimiterService:

    def __init__(
        self,
        store: RateLimitStore | None = None,
        clock: Clock | None = None,
    ):
        self.store = store or InMemoryRateLimitStore()
        self.clock = clock

        self.policies = {
            UserTier.FREE: RateLimitPolicy(
                algorithm=RateLimitType.TOKEN_BUCKET,
                max_requests=10,
                window_in_seconds=60,
            ),
            UserTier.PREMIUM: RateLimitPolicy(
                algorithm=RateLimitType.FIXED_WINDOW,
                max_requests=100,
                window_in_seconds=60,
            ),
        }

        self.rate_limiters = {
            tier: RateLimiterFactory.create(
                policy,
                self.store,
                self.clock,
            )
            for tier, policy in self.policies.items()
        }

    def allow_request(self, user: User) -> RateLimitResult:
        limiter = self.rate_limiters.get(user.tier)

        if limiter is None:
            raise ValueError(
                f"No rate-limit policy configured for tier: {user.tier}"
            )

        return limiter.allow_request(user.user_id)

    def update_policy(
        self,
        tier: UserTier,
        policy: RateLimitPolicy,
    ) -> None:
        self.policies[tier] = policy
        self.rate_limiters[tier] = RateLimiterFactory.create(
            policy,
            self.store,
            self.clock,
        )
