from abc import ABC, abstractmethod

from clock.clock import Clock, SystemClock
from model.rate_limit_policy import RateLimitPolicy
from model.rate_limit_result import RateLimitResult
from store.rate_limit_store import RateLimitStore


class RateLimiter(ABC):

    def __init__(
        self,
        policy: RateLimitPolicy,
        store: RateLimitStore,
        clock: Clock | None = None,
    ):
        self.policy = policy
        self.store = store
        self.clock = clock or SystemClock()

    @abstractmethod
    def allow_request(self, user_id: str) -> RateLimitResult:
        pass
