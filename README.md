![Java CI with Maven](https://github.com/parubok/multiline-label/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/parubok/multiline-label/blob/master/LICENSE)
[![Latest Version](https://img.shields.io/maven-central/v/io.github.parubok/multiline-label)](https://search.maven.org/search?q=a:multiline-label)
[![javadoc](https://javadoc.io/badge2/io.github.parubok/multiline-label/javadoc.svg)](https://javadoc.io/doc/io.github.parubok/multiline-label)

# multiline-label

Java Swing component to display a plain, left-to-right text (single line or multiline).  Allows to specify preferred 
wrapping width (in pixels) for cases when there is not enough room to display the text as a single line.

Motivation: Though standard Swing provides a number of options to display multiline text (e.g. `JLabel` with HTML, 
read-only `JTextArea`, etc.), none of them IMHO is very convenient and straightforward.
The goal of this component is to provide a multiline label with predictable and easily configurable behavior.

The label implements `javax.swing.Scrollable` interface to support scrolling. By default, up to 20 lines will be 
displayed without vertical scroll bar. See `MultilineLabel.setPreferredViewportLineCount`.

The label allows to specify custom separator characters (a space character by default). See `MultilineLabel.setSeparators`.
The label always honors line breaks (e.g. `\n`) if they are present in the text.

The `MultilineLabel` class provides a number of static methods to support multiline text painting on components other 
than the `MultilineLabel`. For example, `MultilineLabel.calculatePreferredSize` method.

Example:
```java
import javax.swing.border.EmptyBorder;
import java.util.Set;

import io.github.parubok.text.multiline.MultilineLabel;

var label = new MultilineLabel();
label.setText(myText); // set text - possibly requiring multiline presentation
label.setPreferredWidthLimit(330); // the label's preferred width won't exceed 330 pixels
label.setLineSpacing(1.2f); // relative spacing between adjacent text lines
label.setMaxLines(30); // limit the label to 30 lines of text
label.setBorder(new EmptyBorder(10, 5, 10, 5));
label.setSeparators(Set.of(' ', '/', '|', '(', ')')); // allow separators other than space
panel.add(label); // add label to its parent container
```

A demo application is provided. See `io.github.parubok.text.multiline.demo.Demo`.
![alt tag](https://raw.github.com/parubok/multiline-label/master/wiki/images/demo.png)

This library is packaged as a Java 9 module `io.github.parubok.text.multiline` (with a single dependency on a system module `java.desktop`). 

This project has no external dependencies (except JUnit 5, for testing).

Requires Java 11 or later.

### License

This project is licensed under [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0)

### Installation

Releases are available in [Maven Central](https://repo1.maven.org/maven2/io/github/parubok/multiline-label/)

#### Maven

Add this snippet to the pom.xml `dependencies` section:

```xml
<dependency>
    <groupId>io.github.parubok</groupId>
    <artifactId>multiline-label</artifactId>
    <version>1.19</version>
</dependency>
```
