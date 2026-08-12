from dataclasses import dataclass

from enums.user_tier import UserTier


@dataclass(frozen=True)
class User:
    user_id: str
    tier: UserTier
