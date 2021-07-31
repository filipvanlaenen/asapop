# Aggregation and Statistical Analysis of Political Opinion Polls

## Getting Started

First of all, you need to obtain a copy of the source code and compile it into
an executable. Run the following commands to do this:

```
git clone git@github.com:filipvanlaenen/asapop.git
cd asapop
mvn clean compile assembly:single
```

If everything works well, you'll finda JAR file in the `target` directory with
all dependencies included. Let's test it out, using no input parameters:

```
java -jar asapop-1.0-SNAPSHOT-jar-with-dependencies.jar
```

This should produce a short report displaying how to use the program.

## Rich Opinion Poll File Format (ROPF)

A rich opinion poll file (ROPF) contains lines with opinion polls. Each opinion
poll should be on a line, and a line should always contain one opinion poll.
Below is an example of a minimal opinion poll, having only a polling firm, a
publication date, and the results for two electoral lists:

```
•PF: ACME •PD: 2021-07-27 A:55 B:45
```

Metadata fields, like the polling firm and the publication date, are prefixed
with a bullet ("•"), followed by the key for the metadata field, a colon (":"),
and the value for the metadata field. The following metadata fields have been
defined so far:

* C: Commissioner (text, can occur multiple times)
* FE: Fieldwork end (date)
* FS: Fieldwork start (date)
* O: Result for other (text)
* PD: Publication date (date)
* PF: Polling firm (text)
* SS: Sample size (text)

The following example shows how the all the metadata fields can be used:

```
•PF: ACME •C: The Times •C: The Post •FS: 2021-07-14 •FE: 2021-07-20 •PD: 2021-07-27 •SS: 1000 A:55 B:43 •O:2
```

Result fields consist of an electoral list key, a colon (":"), and a value. The
electoral list key can only contain uppercase letters, but diacritics (e.g.
"Ä"), letters from other languages (e.g. "Æ") and other alphabets (e.g. Greek
"Ω" or Cyrillic "Б") are allowed.
