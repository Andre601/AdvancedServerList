#
# Modified version of Squidfunk's shortcodes.py file for Material for MkDocs.
#
# It was altered to create compact admonition-like boxes to display in the documentation.
#
# Original shortcodes.py file:
# https://github.com/squidfunk/mkdocs-material/blob/56b96b4d799174d48d36613d70ffac9b964171cc/material/overrides/hooks/shortcodes.py
#

from __future__ import annotations

import posixpath
import re

from mkdocs.config.defaults import MkDocsConfig
from mkdocs.structure.files import File, Files
from mkdocs.structure.pages import Page
from re import Match

def on_page_markdown(
    markdown: str, *, page: Page, config: MkDocsConfig, files: Files
):

    # Replace callback
    def replace(match: Match):
        type, args = match.groups()
        args = args.strip()
        if type == "abstract":   return _admo(text = args, type = "abstract")
        elif type == "info":     return _admo(text = args, type = "info")
        elif type == "tip":      return _admo(text = args, type = "tip")
        elif type == "success":  return _admo(text = args, type = "success")
        elif type == "question": return _admo(text = args, type = "question")
        elif type == "warning":  return _admo(text = args, type = "warning")
        elif type == "failure":  return _admo(text = args, type = "failure")
        elif type == "danger":   return _admo(text = args, type = "danger")
        elif type == "bug":      return _admo(text = args, type = "bug")
        elif type == "example":  return _admo(text = args, type = "example")
        elif type == "quote":    return _admo(text = args, type = "quote")
        else:                    return _admo(text = args)

        # Otherwise, raise an error
        raise RuntimeError(f"Unknown shortcode: {type}")

    # Find and replace all external asset URLs in current page
    return re.sub(
        r"<!-- admo:(\w+)(.*?) -->",
        replace, markdown, flags = re.I | re.M
    )

def _admo(text: str = "", type: str = ""):
    classes = f"mdx-badge mdx-badge--{type}" if type else "mdx-badge"
    iconClasses = f"mdx-badge__icon mdx-badge--{type}" if type else "mdx-badge__icon"
    return "".join([
        f"<div class=\"{classes}\">",
        f"<span class=\"{iconClasses}\"></span>",
        *([f"<span class=\"mdx-badge__text\">{text}</span>"] if text else []),
        f"</div>",
    ])