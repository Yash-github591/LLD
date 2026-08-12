import time
from abc import ABC, abstractmethod


class Clock(ABC):

    @abstractmethod
    def now(self) -> float:
        pass


class SystemClock(Clock):

    def now(self) -> float:
        return time.monotonic()


class FakeClock(Clock):

    def __init__(self, initial_time: float = 0.0):
        self._time = initial_time

    def now(self) -> float:
        return self._time

    def advance(self, seconds: float) -> None:
        if seconds < 0:
            raise ValueError("Cannot move fake clock backwards")
        self._time += seconds
