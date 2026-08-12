from dataclasses import dataclass

from enums.rate_limit_type import RateLimitType


@dataclass(frozen=True)
class RateLimitPolicy:
    algorithm: RateLimitType
    max_requests: int
    window_in_seconds: int

    def __post_init__(self):
        if self.max_requests <= 0:
            raise ValueError("max_requests must be greater than zero")
        if self.window_in_seconds <= 0:
            raise ValueError("window_in_seconds must be greater than zero")
