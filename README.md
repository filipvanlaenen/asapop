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

* C: Commissioner
* FE: Fieldwork end
* FS: Fieldwork start
* O: Result for other
* PD: Publication date
* PF: Polling firm
* SC: Scope
* SS: Sample size

The following example shows how the all the metadata fields can be used:

```
•PF: ACME •C: The Times •C: The Post •FS: 2021-07-14 •FE: 2021-07-20 •PD: 2021-07-27 •SC: N •SS: 1000 A:55 B:43 •O:2
```

Result fields consist of an electoral list key, a colon (":"), and a value. The
electoral list key can only contain uppercase letters, but diacritics (e.g.
"Ä"), letters from other languages (e.g. "Æ") and other alphabets (e.g. Greek
"Ω" or Cyrillic "Б") are allowed.

Some opinion polls have multiple scenarios, e.g. national versus EP voting
intentions, scenarios with hypothetical parties or alliances, etc... Scenarios
belonging to the same opinion poll can add by starting a new line with an
ampersand ("&"), and then adding all metadata fields that are different in the
scenario, plus the results.

The following example illustrates an opinion poll with an alternative response
scenario testing out the support for a new, third party:

```
•PF: ACME •FS: 2021-07-14 •FE: 2021-07-20 •SC: N •SS: 1000 A:55 B:43 •O:2
&                                                          A:50 B:40 C:9 •O:1
```

This example illustrates an opinion poll in which respondents where asked about
their voting intentions both for the national and the European parliament:

```
•PF: ACME •FS: 2021-07-14 •FE: 2021-07-20 •SC: N •SS: 1000 A:55 B:43 •O:2
&                                         •SC: E           A:60 B:39 •O:1
```

The table below gives an overview over the metadata fields and their use:

| Abbreviation | Description      | Type | Cardinality | Response Scenario |
|--------------|------------------|------|-------------|-------------------|
| C            | Commissioner     | Text | 0...n       | No                |
| FE           | Fieldwork end    | Date | 0...1       | No                |
| FS           | Fieldwork start  | Date | 0...1       | No                |
| O            | Result for other | Text | 0...1       | Yes¹              |
| PD           | Publication date | Date | 0...1       | No                |
| PF           | Polling firm     | Text | 0...1       | No                |
| SC           | Scope            | Text | 0...1       | Yes               |
| SS           | Sample size      | Text | 0...1       | No                |

¹ As for the regular results, the result for other is not inherited by a response scenario if absent.
