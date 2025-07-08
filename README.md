# CSV Reorganiser

This utility tool allows you to produce quickly reorganised CSV from a YAML configuration definition.

## Description

Ever had to manipulate hundred of CSV files, change ordering from exports, or whatever to produce a final result? Then this tool was made for you.

Stop to the long hours trying to export, reorganise and separate fields values. All you need is Java and a YAML configuration file and that tool will make the job for you.

A new engine, `Hyde`, was created, making it even easier to use (previous one is called `Chess`).

## Latest news

Hyde engine is out and working! Check out the [documentation](https://github.com/sylordis/csv-reorganiser/wiki/Hyde-engine).

## Getting Started

### Dependencies

Java 21 is required for runtime.

This tool was made with/for Java 21 and Gradle as dependency manager.

It is using the following dependencies (already included as-is in the jar):
* [SnakeYAML](https://bitbucket.org/asomov/snakeyaml)
* Some Apache Commons
* [Apache Log4J](https://logging.apache.org/log4j/2.x/)
* [Reflections](https://github.com/ronmamo/reflections)
* [Guava](https://github.com/google/guava)

Testing dependencies:
* [JUnit 5 (Jupiter)](https://junit.org/junit5/)
* [Mockito](https://site.mockito.org/)
* [Hamcrest 3](http://hamcrest.org/JavaHamcrest)

Documentation dependencies (by and for):
* [Javaparser](https://javaparser.org/)

To check for dependencies versions, check the following files:

- `[build.gradle](csv-reorganiser-app/build.gradle)`
- `[libs.versions.toml](gradle/libs.versions.toml)`

### Compilation

Download the code, you should have Java 21+ and Gradle 9+ to be able to compile the project.
Follow typical Gradle java application goals regarding your intention (`build`, `test`, `shadowJar`).

### Installing

Just download the jar file associated to the release or run `gradle shadowJar` (at least) from the repository.
Jar file should be provided under `csv-reorganiser-app/build/libs/csv-reorganiser-app-<version>-all.jar`

### Executing software

Run the following command:

```
java -jar <path-to-jar>/csv-reorganiser-app-<version>-all.jar [options] <yaml-cfg> <csv-sources..> <csv-target>
```

With:
* `yaml-cfg` the YAML configuration file to be used for the reorganisation. Examples are provided in the `examples/` folder, documentation provided on the [wiki](https://github.com/Sylordis/csv-reorganiser/wiki).
* `csv-sources` the base CSV files to be reorganised. If multiple ones are provided, they will all be processed sequentially and the results compiled into the same target file. Each source file must have the same structure.
* `csv-target` the target file to be written. If existing, it will be overwritten.

Run with `--help` for full documentation and options.

#### Engines

There are 2 engines in the CSV reorganiser. For each engine, the configuration file is written in a slight different way.
* The first one (id `1` or `chess`), is using a simple and clear structure and performs only simple operations.
* The second one (id `2` or `hyde`), is a text & column references based definition of operations, with text filters directly included in the latter. It is easier to use, more permissive and faster than its predecessor.

You can change which engine you are using by using the option flag `-engine <engine-id>` or `-e <engine-id>` where `<engine-id>` is the engine name or number.
You can also specify the engine in the configuration file via the header, using the tag `engine` which behaves the same as the option.

If no engine is specified, `hyde` will be assumed.

Check the [wiki](https://github.com/sylordis/csv-reorganiser/wiki/) for more information.

### Executing unit tests

A simple `gradle test` should suffice. This project has been built with the idea of achieving at least 80% of code coverage through unit tests.

If you want to skip unit tests to build, run command for installing (see before) with option `gradle build -x test`.

## Help

Check out the [wiki](https://github.com/Sylordis/csv-reorganiser/wiki).

## Authors

* Sylvain Domenjoud aka "[Sylordis](https://github.com/Sylordis)" (creator and maintainer)

## License

This project is licensed under the Apache License v2 - see the LICENSE file for details

## Links

Project website: <https://github.com/sylordis/csv-reorganiser>

# Known issues
Check the [wiki/Known issues](https://github.com/sylordis/csv-reorganiser/wiki/Known-issues).
