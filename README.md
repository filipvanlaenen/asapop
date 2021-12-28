# Aggregation and Statistical Analysis of Political Opinion Polls

- [Getting Started](#getting-started)
- [Rich Opinion Poll File Format (ROPF)](#rich-opinion-poll-file-format-ropf)
- [Conversion from ROPF to CSV](#conversion-from-ropf-to-csv)
- [Conversion from ROPF to PSV](#conversion-from-ropf-to-psv)

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

### Opinion Polls

The first part of a rich opinion poll file (ROPF) contains the lines with opinion polls. Each opinion
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

* A: Area
* C: Commissioner
* EX: Excluded responses (as a percentage)
* FE: Fieldwork end
* FS: Fieldwork start
* O: Result for other
* PD: Publication date
* PF: Polling firm
* PFP: Polling firm partner
* SC: Scope
* SS: Sample size

The following example shows how the all the metadata fields can be used:

```
•PF: ACME •PFP: BCME •C: The Times •C: The Post •FS: 2021-07-14 •FE: 2021-07-20 •PD: 2021-07-27 •SC: N •A: IO •SS: 1000 •EX: 10 A:55 B:43 •O:2
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

| Abbreviation | Description          | Type           | Cardinality | Response Scenario |
|--------------|----------------------|----------------|-------------|-------------------|
| A            | Area                 | Text           | 0…1         | Yes               |
| C            | Commissioner         | Text           | 0…_n_       | No                |
| EX           | Excluded responses   | Decimal Number | 0…1         | No                |
| FE           | Fieldwork end        | Date or Month  | 0…1         | No                |
| FS           | Fieldwork start      | Date or Month  | 0…1         | No                |
| O            | Result for other     | Result Value   | 0…1         | Yes¹              |
| PD           | Publication date     | Date           | 0…1         | No                |
| PF           | Polling firm         | Text           | 0…1         | No                |
| PFP          | Polling firm partner | Text           | 0…1         | No                |
| SC           | Scope                | Scope          | 0…1         | Yes               |
| SS           | Sample size          | Text           | 0…1         | Yes               |

¹ As for the regular results, the result for other is not inherited by a response scenario if absent.

The table below gives an informal overview over the field types:

| Type           | Definition | Examples     |
|----------------|------------|--------------|
| Date           | ####-##-## | 2021-12-09   |
| Decimal Number | #(.#)      | 1, 1.1       |
| Month          | ####-##    | 2021-12      |
| Result Value   | (<)#(.#)   | 1, 1.1, <0.5 |
| Text           | *          | a, 1         |

The type `Scope` accepts the values as in the table below:

| Value | Description                        |
|-------|------------------------------------|
| E     | European elections                 |
| N     | National elections                 |
| P1    | Presidential election, first round |

### Electoral Lists

The second part of a rich opinion poll file contains the lines with electoral lists. Each electoral list appearing in
the opinion polls should be on a line, and a line should always contain one electoral list. The line should start with
the electoral list key, as used in the opinion poll results, followed by a colon (":"). The rest of the line contains
fields with information about the electoral list, using a bullet ("•") to mark the start of a new field key, followed
by a key, which is either "A" for the official abbreviation of the electoral list, or a two-letter language code for
the name of the electoral list, followed by a colon (":") and the content of the field.

Below is an example of an electoral list using the key "A" in the opinion poll lines, having an official abbreviation
"AP", the English name "Apple Party" and a traduction into Esperanto "Pomo Partio":

```
A: •A: AP •EN: Apple Party •EO: Pomo Partio
```

## Conversion from ROPF to CSV

An ROPF file can be converted to EOPAOD's CSV file format using the following
command:

```
convert <ropf-file-name> <csv-file-name> <electoral-list-key>+ [-a=<area>]
```

Assume the opinion polls have been stored in a file called `nn.ropf`, and you
want to convert them to a file called `nn.csv`, and the electoral lists have
keys `ABC`, `DEF` and `GHI`, then you can use the following command to do the
conversion:

```
java -jar asapop-1.0-SNAPSHOT-jar-with-dependencies.jar convert nn.ropf nn.csv ABC DEF GHI
```

The option `-a` can be used to filter the opinion polls and response scenarios by a certain area. The following command
would export only the opinion polls and response scenarios related to the area `XX`:

```
java -jar asapop-1.0-SNAPSHOT-jar-with-dependencies.jar convert nn.ropf nn.csv ABC DEF GHI -a=XX
```

Use `--` to indicate that only opinion polls and response scenarios not related to an area should be exported:

```
java -jar asapop-1.0-SNAPSHOT-jar-with-dependencies.jar convert nn.ropf nn.csv ABC DEF GHI -a=--
```

## Conversion from ROPF to PSV

An ROPF file can be converted to EOPAOD's PSV file format using the following
command:

```
convert <ropf-file-name> <psv-file-name> <electoral-list-key>+ [-a=<area>]
```

Assume the opinion polls have been stored in a file called `nn.ropf`, and you
want to convert them to a file called `nn.psv`, and the electoral lists have
keys `ABC`, `DEF` and `GHI`, then you can use the following command to do the
conversion:

```
java -jar asapop-1.0-SNAPSHOT-jar-with-dependencies.jar convert nn.ropf nn.psv ABC DEF GHI
```

The option `-a` can be used to filter the opinion polls and response scenarios by a certain area. The following command
would export only the opinion polls and response scenarios related to the area `XX`:

```
java -jar asapop-1.0-SNAPSHOT-jar-with-dependencies.jar convert nn.ropf nn.psv ABC DEF GHI -a=XX
```

Use `--` to indicate that only opinion polls and response scenarios not related to an area should be exported:

```
java -jar asapop-1.0-SNAPSHOT-jar-with-dependencies.jar convert nn.ropf nn.psv ABC DEF GHI -a=--
```
