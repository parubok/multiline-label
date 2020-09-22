# multiline-label

Java Swing component to display plain text. Allows to specify preferred wrapping width for cases when there is not enough room to display the text as a single line. Text with EOL characters (`"\n"` or `"\r\n"`) is displayed as multiline according to the EOL characters.

Motivation: Though Swing provides a number of options to display multiline text (e.g. `JLabel` with HTML, read-only `JTextArea`, etc.), none of them IMHO is very convenient and straightforward.
The goal of this component is to provide a multiline label with predictable and easily configurable behavior.

This project has no dependencies (except JUnit 5, for testing).

Requires Java 8 or later.
