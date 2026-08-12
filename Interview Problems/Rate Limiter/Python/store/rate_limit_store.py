from abc import ABC, abstractmethod
from collections import defaultdict
from contextlib import contextmanager
import threading


class RateLimitStore(ABC):

    @abstractmethod
    def get(self, key: str, default=None):
        pass

    @abstractmethod
    def set(self, key: str, value) -> None:
        pass

    @abstractmethod
    def delete(self, key: str) -> None:
        pass

    @abstractmethod
    @contextmanager
    def lock_for(self, key: str):
        yield


class InMemoryRateLimitStore(RateLimitStore):

    def __init__(self):
        self._data = {}
        self._data_lock = threading.RLock()
        self._locks = defaultdict(threading.RLock)

    def get(self, key: str, default=None):
        with self._data_lock:
            return self._data.get(key, default)

    def set(self, key: str, value) -> None:
        with self._data_lock:
            self._data[key] = value

    def delete(self, key: str) -> None:
        with self._data_lock:
            self._data.pop(key, None)

    @contextmanager
    def lock_for(self, key: str):
        lock = self._locks[key]
        with lock:
            yield
