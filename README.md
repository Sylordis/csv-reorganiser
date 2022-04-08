# CSV Reorganiser

This utility tool allows you to produce quickly reorganised CSV from a YAML configuration definition.

## Description

Ever had to manipulate hundred of CSV files, change ordering from exports, or whatever to produce a final result? Then this tool was made for you.

Stop to the long hours trying to export, reorganise and separate fields values. All you need is Java and a YAML configuration file and that tool will make the job for you.

## Getting Started

### Dependencies

Only Java 17+ is required for runtime.

This tool was made with/for Java 17 and Apache Maven as dependency manager.

It is using the following dependencies (already included as-is in the jar):
* [SnakeYAML](https://bitbucket.org/asomov/snakeyaml)
* [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/)
* [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/)
* [Apache Commons IO](https://commons.apache.org/proper/commons-io/)
* [Apache Log4J2](https://logging.apache.org/log4j/2.x/)
* [Reflections](https://github.com/ronmamo/reflections)

Testing dependencies:
* [JUnit 5 (Jupiter)](https://junit.org/junit5/)
* [Mockito](https://site.mockito.org/)
* [Hamcrest 2](http://hamcrest.org/JavaHamcrest)
* [junit5-system-exit](https://github.com/tginsberg/junit5-system-exit)

Check the `pom.xml` file for dependencies versions.

### Compilation

Download the code, you should have Java 17+ and Apache Maven to be able to compile the project.
Follow typical Maven goals regarding your intention (`compile`, `package`, `install`, ...).

### Installing

Just download the jar file associated to the release or run `mvn package` (at least) from the repository.
Jar file should be provided under `target/csv-reorganiser-<VERSION>.jar`

### Executing software

Run the following command:

```
java -jar <path-to-jar>/csv-reorganiser-X.X.jar <yaml-cfg> <csv-source> <csv-target>
```

With:
* `yaml-cfg` the YAML configuration file to be used for the reorganisation. Examples are provided in the `examples/` folder, documentation provided on the [wiki](https://github.com/Sylordis/csv-reorganiser/wiki).
* `csv-source` the base CSV file to be reorganised.
* `csv-target` the target file to be written. If existing, it will be overwritten.

### Executing unit tests

A simple `mvn test` should suffice. Check the wiki for known issues. This project has been built with the idea of achieving at least 80% of code coverage through unit and functional tests.

If you want to skip unit tests to package or install, run command for installing (see before) with option `-DskipTests`.

## Help

Check out the [wiki](https://github.com/Sylordis/csv-reorganiser/wiki).

## Authors

* Sylvain Domenjoud aka "[Sylordis](https://github.com/Sylordis)" (creator and maintainer)

## Version History

* v1.1
  * Updated Log4J.
  * Some unit tests were left behind (disabled).
  * New operation: Concat.
* v1.0
  * First official release.
  * Unit-tested (most of it) along with some integration tests.
  * 3 Operations: Get, Value and RegReplace.

## License

This project is licensed under the Apache License v2 - see the LICENSE file for details

## Links

Project website: <https://github.com/sylordis/csv-reorganiser>

# Known issues
Check the [wiki/Known issues](https://github.com/sylordis/csv-reorganiser/wiki/Known-issues).
