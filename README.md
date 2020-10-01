![Java CI with Maven](https://github.com/parubok/multiline-label/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/parubok/multiline-label/blob/master/LICENSE)

# multiline-label

Java Swing component to display plain text. Allows to specify preferred wrapping width for cases when there is not enough room to display the text as a single line. Text with EOL characters (`"\n"` or `"\r\n"`) is displayed as multiline according to the EOL characters.

Motivation: Though Swing provides a number of options to display multiline text (e.g. `JLabel` with HTML, read-only `JTextArea`, etc.), none of them IMHO is very convenient and straightforward.
The goal of this component is to provide a multiline label with predictable and easily configurable behavior.

This project has no dependencies (except JUnit 5, for testing).

Requires Java 11 or later.

 * TODO: Consolidate demos
 * TODO: Improve UT coverage
 * TODO: JavaDoc
 * TODO: README with examples and screenshots
 * TODO: specify width limit in characters
 * TODO: fix AA to be as in L&F (UI delegate?)
 * TODO: javax.swing.text.Utilities.getBreakLocation ?
 * TODO: lines limit, "..." to display very long messages
 * TODO: 'flexible' pref. width (e.g. +/-100 pixels) to achieve the best distribution.
