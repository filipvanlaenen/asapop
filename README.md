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
