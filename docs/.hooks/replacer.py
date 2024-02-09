#
# A simple hook that replaces any appearances of \| with just |.
#
# There is an inconsistency between GitHub flavourited Markdown and Python Markdown
# where the former treats | as part of a table, even if it is inside inline code
# causing unwanted table rendering.
#
# The only way to prevent this is to escape it by prefixing it with a back slash (\).
# Unfortunately does this cause issues on Python-Markdown, as it renders inline code
# content as-is without special treatments whatsoever, meaning that `\|` will be
# rendered as <code>\|</code> in the end.
#
# Using this hook, `\|` is being turned into `|` before further processing, resulting
# in <code>|</code> being rendered in the end, while preserving proper table rendering
# on GitHub and co.
#
import re

from mkdocs.config.defaults import MkDocsConfig
from mkdocs.structure.files import File, Files
from mkdocs.structure.pages import Page

def on_page_markdown(
        markdown: str, *, page: Page, config: MkDocsConfig, files: Files
):
    return re.sub(r"\\\|","|",markdown)