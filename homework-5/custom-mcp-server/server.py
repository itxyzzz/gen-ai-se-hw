from pathlib import Path

from fastmcp import FastMCP


mcp = FastMCP("custom-lorem-reader")
LOREM_PATH = Path(__file__).with_name("lorem-ipsum.md")


def normalize_word_count(word_count: object = 30, available_words: int | None = None) -> int:
    if word_count is None or word_count == "":
        count = 30
    elif isinstance(word_count, bool):
        raise ValueError("word_count must be a non-negative integer")
    else:
        text_value = str(word_count).strip()
        if not text_value.isdecimal():
            raise ValueError("word_count must be a non-negative integer")
        count = int(text_value)

    if available_words is not None and count > available_words:
        raise ValueError("word_count cannot exceed available words")

    return count


def read_lorem_ipsum_words(word_count: object = 30) -> str:
    words = LOREM_PATH.read_text(encoding="utf-8").split()
    count = normalize_word_count(word_count, available_words=len(words))
    return " ".join(words[:count])


@mcp.resource("lorem-ipsum://content{?word_count}")
def lorem_resource(word_count: str | int = 30) -> str:
    return read_lorem_ipsum_words(word_count)


@mcp.tool(name="read_lorem_ipsum")
def read_lorem_ipsum(word_count: str | int = 30) -> str:
    return read_lorem_ipsum_words(word_count)


if __name__ == "__main__":
    mcp.run()
