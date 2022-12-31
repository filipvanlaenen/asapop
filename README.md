# Aggregation and Statistical Analysis of Political Opinion Polls

- [Getting Started](#getting-started)
- [Rich Opinion Poll File Format (ROPF)](#rich-opinion-poll-file-format-ropf)
  - [Opinion Polls](#opinion-polls)
  - [Incomplete Results](#incomplete-results)
  - [Electoral Lists](#electoral-lists)
  - [Comments and Empty Lines](#comments-and-empty-lines)
- [File Cache](#file-cache)
- [Analysis](#analysis)
- [Build a Website](#build-a-website)
- [Conversion from ROPF to CSV](#conversion-from-ropf-to-csv)
- [Conversion from ROPF to PSV](#conversion-from-ropf-to-psv)
- [Provide SAPOR files](#provide-sapor-files)

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
* N: Number of no responses (as a percentage)
* O: Result for other
* PD: Publication date
* PF: Polling firm
* PFP: Polling firm partner
* SC: Scope
* SS: Sample size

The following example shows how the all the metadata fields can be used:

```
•PF: ACME •PFP: BCME •C: The Times •C: The Post •FS: 2021-07-14 •FE: 2021-07-20 •PD: 2021-07-27 •SC: N •A: IO •SS: 1000 •EX: 10 A:55 B:40 •O:2 •N:3
```

Result fields consist of an electoral list key, a colon (":"), and a value. The electoral list key can only contain
uppercase letters, but diacritics (e.g. "Ä"), letters from other languages (e.g. "Æ") and other alphabets (e.g. Greek
"Ω" or Cyrillic "Б") are allowed. Electoral list key can also be combined using the plus sign ("+") in cases where
electoral lists where polled together, as illustrated in the following example:

```
•PF: ACME •C: The Times •FS: 2021-07-14 •FE: 2021-07-20 •SC: N •SS: 1000 A:55 B+C:40 •O:2
```

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
| N            | No responses         | Result Value   | 0…1         | Yes¹              |
| O            | Result for other     | Result Value   | 0…1         | Yes¹              |
| PD           | Publication date     | Date           | 0…1         | No                |
| PF           | Polling firm         | Text           | 0…1         | No                |
| PFP          | Polling firm partner | Text           | 0…1         | No                |
| SC           | Scope                | Scope          | 0…1         | Yes               |
| SS           | Sample size          | Text           | 0…1         | Yes               |

¹ As for the regular results, the result for other and the number of no responses is not inherited by a response
scenario if absent.

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

### Incomplete Results

Polling firms and media outlets don't present their results in a standard way across all countries. Sometimes there are
even differences within the same country, or the results presented are incomplete. Opinion poll results should be
registered as close as possible to the original results, and any needed conversion and/or interpretation should be
left as much as possible to the software, possibly with the aid of hints in a configuration file.

Imagine an opinion poll was conducted with the following results:

* 1,250 people were polled,
* 250 of them refused to anwer,
* Of the 1,000 that answered:
  * 400 people had A as their preference,
  * 300 people had B as their preference,
  * 200 people had C as their preference,
  * and 100 people had other as their preference.

Below are a number of ways in which the results can be registered:

| Registration in the ROPF File                        | Derived Values  | Effective Sample Size |
|------------------------------------------------------|-----------------|-----------------------|
| `•SS: 1000         A: 40 B: 30 C: 20`                | `•O: 10 •N: ?`  | 1,000                 |
| `•SS: 1250 •EX: 20 A: 40 B: 30 C: 20`                | `•O: 10 •N: 20` | 1,250 - 20% = 1,000   |
| `•SS: 1000         A: 40 B: 30 C: 20 •O: 10`         | `•N: ?`         | 1,000                 |
| `•SS: 1250 •EX: 20 A: 40 B: 30 C: 20 •O: 10`         | `•N: 20`        | 1,250 - 20% = 1,000   |
| `•SS: 1250         A: 32 B: 24 C: 16 •O: 8`          | `•N: 20`¹       | 1,250 - 20% = 1,000   |
| `•SS: 1250 •EX: 20 A: 32 B: 24 C: 16 •O: 8`          | `•N: 20`¹²      | 1,250 - 20% = 1,000   |
| `•SS: 1250         A: 32 B: 24 C: 16 •O: 8  •N: 20`  |                 | 1,250 - 20% = 1,000   |
| `•SS: 1250 •EX: 20 A: 32 B: 24 C: 16 •O: 8  •N: 20`  | ²               | 1,250 - 20% = 1,000   |
| `•SS: 1250         A: 32 B: 24 C: 16        •N: 20`  | `•O: 8`         | 1,250 - 20% = 1,000   |
| `•SS: 1250 •EX: 20 A: 32 B: 24 C: 16        •N: 20`  | `•O: 8`²        | 1,250 - 20% = 1,000   |
| `•SS: 1250         A: 8  B: 6  C: 4  •O: 2  •N: 5 `  | ³               | 1,250 - 20% = 1,000   |
| `•SS: 1250 •EX: 20 A: 8  B: 6  C: 4  •O: 2  •N: 5 `  | ²³              | 1,250 - 20% = 1,000   |
| `•SS: 1250         A: 40 B: 30 C: 20 •O: 10 •N: 25`  | ³               | 1,250 - 20% = 1,000   |
| `•SS: 1250 •EX: 20 A: 40 B: 30 C: 20 •O: 10 •N: 25`  | ²³              | 1,250 - 20% = 1,000   |

¹ Field N is only calculated if the sum of the registered results is less than 100 - floor((_n_ - 1) / 2) × precision, in order to cater for rounding effects.

² Field EX will be ignored.

³ If the sum of the registered results is not within 100 ± floor((_n_ - 1) / 2) × precision, they will be rescaled.

### Electoral Lists

The second part of a rich opinion poll file contains the lines with electoral lists. Each electoral list appearing in
the opinion polls should be on a line, and a line should always contain one electoral list. The line should start with
the electoral list key, as used in the opinion poll results, followed by a colon (":"). The rest of the line contains
fields with information about the electoral list, using a bullet ("•") to mark the start of a new field key, followed
by a key, which is either "A" for the official abbreviation of the electoral list, "R" for the romanized version of the
official abbreviation, or a two-letter language code for the name of the electoral list, followed by a colon (":") and
the content of the field.

Below is an example of an electoral list using the key "A" in the opinion poll lines, having an official abbreviation
"AP", the English name "Apple Party" and a traduction into Esperanto "Pomo Partio":

```
A: •A: AP •EN: Apple Party •EO: Pomo Partio
```

The example below shows how to include a romanized abbreviation:

```
A: •A: ΑΠ •R: AP •EN: Apple Party •EO: Pomo Partio
```

### Comments and Empty Lines

Note that in addition to opinion poll and electoral list data, richt opinion poll files can also contain empty lines
and comment lines. Empty lines consist entirely of white space, whereas comment line are marked by a leading double 
dagger symbol (`‡`).

## File Cache

Sampled hypergeometric distributions will be stored to a file cache. Make sure that it is possible to create a
directory named `~/.asapop`, and read, create and write directories and files below that directory, with `~` being the
user's home directory as it can be retrieved using `System.getProperty("user.home")` in Java.

The structure of the cache is as follows:

```
~/.asapop/sampled-hypergeometric-distributions/<population-size>/<sample-size>/<number-of-responses>.yaml
```

This means that a hypergeometric distribution based on 120 responses in a sample size of 800 for a population size of
11,222,333 will be stored to the following location:

```
~/.asapop/sampled-hypergeometric-distributions/11222333/800/120.yaml
```

## Analysis

An ROPF file can be analysed using the following command:

```
analyze <ropf-file-name> <election-yaml-file-name> <analysis-result-yaml-file-name>
```

Assume the opinion polls have been stored in a file called `nn.ropf`, and you want to analyse them based on the election
specific data in a file called `nn-data.yaml` and then write the results to a file called `nn-result.yaml`, then you can
use the following command to run the analysis:

```
java -jar asapop-1.0-SNAPSHOT-jar-with-dependencies.jar analyze nn.ropf nn-data.yaml nn-result.yaml
```

Below is an example of a valid election specific data file:
```
---
populationSize: 36_054_394
```

## Build a Website

A website can be build using the following command:

```
build <site-dir-name> <website-configuration-yaml-file-name> <ropf-dir-name> <custom-style-sheet-file-name>
```

Assume ``~/public/asapop`` is a valid and existing directory on your computer, the website configuration is stored
in a local file ``website-configuration.yaml``, the ROPF files can be found in the directory ``~/ropf`` and
``custom.css`` is the custom style sheet, then you can use the following command to produce a website:

```
java -jar asapop-1.0-SNAPSHOT-jar-with-dependencies.jar build ~/public/asapop website-configuration.yaml ~/ropf custom.css
```

Below is an example of a valid website configuration file:

```
---
areaConfigurations:
- areaCode: "ad"
  electionConfigurations:
  - nextElectionDate: "≤2023-04"
    type: Parliament
  translations:
    de: "Andorra"
    en: "Andorra"
    eo: "Andoro"
    fr: "Andorre"
    nl: "Andorra"
    no: "Andorra"
- areaCode: "at"
  electionConfigurations:
  - nextElectionDate: "≈2024-05"
    type: European Parliament
  - gitHubWebsiteUrl: "https://filipvanlaenen.github.io/austrian_polls"
    nextElectionDate: "≤2024-10-27"
    type: Parliament
  - nextElectionDate: "≈2028-10"
    type: President
  translations:
    de: "Österreich"
    en: "Austria"
    eo: "Aŭstrio"
    fr: "Autriche"
    nl: "Oostenrijk"
    no: "Østerrike"
- areaCode: "gl"
  csvConfiguration:
    electoralListKeys:
    - "S"
    - "IA"
    - "D"
    - "N"
    - "A"
    - "NQ"
    - "SA"
  electionConfigurations:
  - nextElectionDate: "⪅2025-04-06"
    type: Parliament
  translations:
    de: "Grönland"
    en: "Greenland"
    eo: "Gronlando"
    fr: "Groenland"
    nl: "Groenland"
    no: "Grønland"
```

Notice that the field ``nextElectionDate`` doesn't need to contain a complete date (``YYYY-MM-DD``): a month
(``YYYY-MM``) or a year (``YYYY``) already suffice. In addition, the date can be preceded by ``≤`` (no later than),
``≈`` (approximate date) or ``⪅`` (no later than approximate date) to indicate further uncertainty about when the next
election will take place.

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

In case the electoral lists `ABC` and `GHI` were polled together in one of the opinion polls, a column with that
combination can be exported using `ABC+GHI` as an argument:

```
java -jar asapop-1.0-SNAPSHOT-jar-with-dependencies.jar convert nn.ropf nn.csv ABC DEF GHI ABC+GHI
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

In case the electoral lists `ABC` and `GHI` were polled together in one of the opinion polls, a column with that
combination can be exported using `ABC+GHI` as an argument:

```
java -jar asapop-1.0-SNAPSHOT-jar-with-dependencies.jar convert nn.ropf nn.psv ABC DEF GHI ABC+GHI
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

## Provide SAPOR files

An ROPF file can be converted to a directory with SAPOR files using the following command:

```
provide <ropf-file-name> <sapor-dir-name> <sapor-configuration-yaml-file-name>
```

Assume the opinion polls have been stored in a file called `nn.ropf`, and you
want to convert them to SAPOR files in a directory called ``~/sapor/nn``, and the SAPOR configuration is stored
in a local file ``sapor-configuration.yaml``, then you can use the following command to do the
conversion:

```
java -jar asapop-1.0-SNAPSHOT-jar-with-dependencies.jar provide nn.ropf ~/sapor/nn sapor-configuration.yaml
```

Below is an example of a valid SAPOR configuration file:

```
---
area: "LU"
mapping:
- directMapping:
    source: "ADR"
    target: "Alternativ Demokratesch Reformpartei"  
- directMapping:
    source: "CSV"
    target: "Chrëschtlech-Sozial Vollekspartei"  
- directMapping:
    source: "DG"
    target: "déi gréng"  
- directMapping:
    source: "DL"
    target: "déi Lénk"  
- directMapping:
    source: "DP"
    target: "Demokratesch Partei"  
- directMapping:
    source: "KPL"
    target: "Kommunistesch Partei Lëtzebuerg"  
- directMapping:
    source: "LSAP"
    target: "Lëtzebuerger Sozialistesch Aarbechterpartei"  
- directMapping:
    source: "PID"
    target: "Partei fir Integral Demokratie"  
- directMapping:
    source: "PPLU"
    target: "Piratepartei Lëtzebuerg"  
lastElectionDate: "2018-10-14"
```
