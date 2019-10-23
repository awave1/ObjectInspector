# Reflective Object Inspector

Reflective Object Inspector performs complete introspective of Java objects at a runtime using reflection.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. Project can also be found on GitLab [artem.golovin/ObjectInspector](https://gitlab.cpsc.ucalgary.ca/artem.golovin/ObjectInspector).

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
./gradlew run --console=plain --args="[isRecursive] [useStdOut]"
```

To execute `DynamicReflectiveInspector`

```bash
./gradlew run -PmainClass=DynamicReflectiveInspector --console=plain --args="[inspectorClassPath] [classToInspectPath] [isRecursive]"
```

Supported command line arguments:

**`Driver`**:

* `isRecursive: true|false` - specifies whether to inspect objects recursively, `true` by default
* `useStdOut: true|false` - specifies whether to use standard output, by default will write to files

**`DynamicReflectiveInspector`**

* `inspectorClassPath` - specifies path to class with `inspect` method. Required
* `classToInspectPath` - specifies path to class to inspect. Required
* `isRecursive` - specifies, whether to inspect objects recursively. Optional

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

