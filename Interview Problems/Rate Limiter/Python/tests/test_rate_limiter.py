import threading
import unittest
from concurrent.futures import ThreadPoolExecutor

from clock.clock import FakeClock
from enums.rate_limit_type import RateLimitType
from enums.user_tier import UserTier
from factory.rate_limiter_factory import RateLimiterFactory
from model.rate_limit_policy import RateLimitPolicy
from model.user import User
from service.rate_limiter_service import RateLimiterService
from store.rate_limit_store import InMemoryRateLimitStore


class RateLimiterTest(unittest.TestCase):

    def test_fixed_window(self):
        clock = FakeClock()
        store = InMemoryRateLimitStore()

        policy = RateLimitPolicy(
            RateLimitType.FIXED_WINDOW,
            max_requests=2,
            window_in_seconds=10,
        )

        limiter = RateLimiterFactory.create(policy, store, clock)

        self.assertTrue(limiter.allow_request("u1").allowed)
        self.assertTrue(limiter.allow_request("u1").allowed)
        self.assertFalse(limiter.allow_request("u1").allowed)

        clock.advance(10)

        self.assertTrue(limiter.allow_request("u1").allowed)

    def test_token_bucket_refills(self):
        clock = FakeClock()
        store = InMemoryRateLimitStore()

        policy = RateLimitPolicy(
            RateLimitType.TOKEN_BUCKET,
            max_requests=2,
            window_in_seconds=10,
        )

        limiter = RateLimiterFactory.create(policy, store, clock)

        self.assertTrue(limiter.allow_request("u1").allowed)
        self.assertTrue(limiter.allow_request("u1").allowed)
        self.assertFalse(limiter.allow_request("u1").allowed)

        clock.advance(5)

        self.assertTrue(limiter.allow_request("u1").allowed)

    def test_sliding_window(self):
        clock = FakeClock()
        store = InMemoryRateLimitStore()

        policy = RateLimitPolicy(
            RateLimitType.SLIDING_WINDOW_LOG,
            max_requests=2,
            window_in_seconds=10,
        )

        limiter = RateLimiterFactory.create(policy, store, clock)

        self.assertTrue(limiter.allow_request("u1").allowed)
        clock.advance(2)
        self.assertTrue(limiter.allow_request("u1").allowed)
        self.assertFalse(limiter.allow_request("u1").allowed)

        clock.advance(8)

        self.assertTrue(limiter.allow_request("u1").allowed)

    def test_users_have_independent_limits(self):
        clock = FakeClock()
        service = RateLimiterService(clock=clock)

        user_a = User("a", UserTier.FREE)
        user_b = User("b", UserTier.FREE)

        for _ in range(10):
            self.assertTrue(service.allow_request(user_a).allowed)

        self.assertFalse(service.allow_request(user_a).allowed)
        self.assertTrue(service.allow_request(user_b).allowed)

    def test_concurrency(self):
        clock = FakeClock()
        store = InMemoryRateLimitStore()

        policy = RateLimitPolicy(
            RateLimitType.TOKEN_BUCKET,
            max_requests=10,
            window_in_seconds=60,
        )

        limiter = RateLimiterFactory.create(policy, store, clock)

        barrier = threading.Barrier(20)

        def request():
            barrier.wait()
            return limiter.allow_request("same-user").allowed

        with ThreadPoolExecutor(max_workers=20) as executor:
            results = list(executor.map(lambda _: request(), range(20)))

        self.assertEqual(sum(results), 10)


if __name__ == "__main__":
    unittest.main()
