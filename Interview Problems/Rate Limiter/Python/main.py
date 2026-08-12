from enums.user_tier import UserTier
from model.user import User
from service.rate_limiter_service import RateLimiterService


def main():
    service = RateLimiterService()

    free_user = User("free-user", UserTier.FREE)
    premium_user = User("premium-user", UserTier.PREMIUM)

    print("=== FREE USER ===")
    for i in range(12):
        result = service.allow_request(free_user)
        print(
            f"Request {i + 1}: "
            f"{'ALLOWED' if result.allowed else 'BLOCKED'} | "
            f"remaining={result.remaining} | "
            f"retry_after={result.retry_after_seconds}s"
        )

    print("\n=== PREMIUM USER ===")
    for i in range(3):
        result = service.allow_request(premium_user)
        print(
            f"Request {i + 1}: "
            f"{'ALLOWED' if result.allowed else 'BLOCKED'} | "
            f"remaining={result.remaining}"
        )


if __name__ == "__main__":
    main()
