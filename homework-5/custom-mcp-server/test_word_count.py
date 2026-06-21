import pytest

import server
from server import normalize_word_count, read_lorem_ipsum_words


def _word_count(text: str) -> int:
    return len(text.split())


def test_read_lorem_ipsum_words_defaults_to_30_words() -> None:
    result = read_lorem_ipsum_words()
    assert _word_count(result) == 30


def test_read_lorem_ipsum_words_accepts_custom_word_count() -> None:
    result = read_lorem_ipsum_words(12)
    assert _word_count(result) == 12


def test_assignment_read_tool_accepts_custom_word_count() -> None:
    assert hasattr(server, "read")
    result = server.read(5)
    assert _word_count(result) == 5


def test_read_lorem_ipsum_words_accepts_string_query_value() -> None:
    result = read_lorem_ipsum_words("7")
    assert _word_count(result) == 7


def test_read_lorem_ipsum_words_accepts_zero_word_count() -> None:
    assert read_lorem_ipsum_words(0) == ""


def test_normalize_word_count_defaults_blank_query_value() -> None:
    assert normalize_word_count("") == 30


@pytest.mark.parametrize("invalid", [-1, "abc", "12.5", True])
def test_normalize_word_count_rejects_invalid_query_values(invalid: object) -> None:
    with pytest.raises(ValueError, match="word_count must be a non-negative integer"):
        normalize_word_count(invalid)


def test_read_lorem_ipsum_words_rejects_count_larger_than_source() -> None:
    with pytest.raises(ValueError, match="word_count cannot exceed available words"):
        read_lorem_ipsum_words(1000)
