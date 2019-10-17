# Reflective Object Inspector

Reflective Object Inspector performs complete introspective of Java objects at a runtime using reflection

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* Ensure you're using Java 8
* (Optional) Install [gradle](https://gradle.org)

### Eclipse support

After importing the project to Eclipse, ensure that Gradle is configured. To configure it, do the following

- Right click on the project in the eclipse project explorer
- Select "Configure"
- Select "Add Gradle Nature"

### Installing

A step by step series of examples that tell you how to get a development env running

Building the project

```bash
./gradlew build
```

Running the project

```bash
./gradlew run --console=plain
```

Running the project with command line arguments

```bash
./gradlew run --console=plain --args="[useRecursion] [useStdOut]"
```

Supported command line arguments:

* `useRecursion: true|false` - specifies whether to inspect objects recursively, `false` by default
* `useStdOut: true|false` - specifies whether to use standard output, by default will write to files


## Running the tests

The project contains some tests. Testing is done via JUnit5. To run the tests, run the following command in the project directory:

```bash
./gradlew clean test
```

## Built With

* [JUnit 5](https://junit.org/junit5) - Testing framework
* [gradle](https://gradle.org) - Build System

## Authors

* **Artem Golovin** - [awave1](https://github.com/awave1)
