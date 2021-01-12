[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ru.oleg-cherednik.utils/gson-utils/badge.svg)](https://maven-badges.herokuapp.com/maven-central/ru.oleg-cherednik.utils/gson-utils)
[![javadoc](https://javadoc.io/badge2/ru.oleg-cherednik.utils/gson-utils/javadoc.svg)](https://javadoc.io/doc/ru.oleg-cherednik.utils/gson-utils)
[![java8](https://badgen.net/badge/java/8+/blue)](https://badgen.net/)
[![travis-ci](https://travis-ci.com/oleg-cherednik/GsonUtils.svg?branch=dev)](https://travis-ci.com/oleg-cherednik/GsonUtils)
[![circle-ci](https://circleci.com/gh/oleg-cherednik/GsonUtils/tree/dev.svg?style=shield)](https://app.circleci.com/pipelines/github/oleg-cherednik/GsonUtils)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![codecov](https://codecov.io/gh/oleg-cherednik/GsonUtils/branch/dev/graph/badge.svg?token=UnqBOd1gbA)](https://codecov.io/gh/oleg-cherednik/GsonUtils)
[![Known Vulnerabilities](https://snyk.io/test/github/oleg-cherednik/GsonUtils/badge.svg?targetFile=build.gradle)](https://snyk.io/test/github/oleg-cherednik/GsonUtils?targetFile=build.gradle)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ba0faab92b44432491376ee5d331a63e)](https://www.codacy.com/gh/oleg-cherednik/GsonUtils/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=oleg-cherednik/GsonUtils&amp;utm_campaign=Badge_Grade)     
# GsonUtils
> a java tool to make working with [Gson](https://github.com/google/gson) more comfortable

## Features
*   Encapsulate all checked exceptions from Gson with custom runtime exception;
*   A central place for configuration;
*   A central place for holding `Gson` instances;
*   Utility class to make most common operations much more comfortable to use;
*   Ability to change `Zone` to save `ZonedDateTime` independently of original zone;
*   `InputStream` support for objects, lists and maps;
*   Lazy read support for list from `InputStream`;
*   Read `Integer` value as `Integer` but not `Double`. 

## Gradle

```groovy
compile 'ru.oleg-cherednik.utils:gson-utils:2.8.6.1'
```

## Maven

```xml
<dependency>
    <groupId>ru.oleg-cherednik.utils</groupId>
    <artifactId>gson-utils</artifactId>
    <version>2.8.6.1</version>
</dependency>
```                                                    

In the version, first 3 places are the version of `Gson` that is used in this utils.
The last section is the `gson-utils` version. This number is unique. 

## Usage 

*   [GsonUtils](#gsonutils-class) - utility class with set of method to use json transformation;

### GsonUtils class

#### Read json from `String`

##### `String` to a custom object type (but not a collection)

```java
class Data {
    int intVal;
    String strVal;
}
```
```java
String json = """
              {
                  "intVal" : 666,
                  "strVal" : "omen"
              }
              """;
Data data = GsonUtils.readValue(json, Data.class);
```

##### `String` to a list of custom object type

```java
class Data {
    int intVal;
    String strVal;
}
```
```java
String json = """
              [
                  {
                      "intVal" : 555,
                      "strVal" : "victory"
                  },
                  {
                      "intVal" : 666,
                      "strVal" : "omen"
                  }
              ]
              """;
List<Data> res = GsonUtils.readList(json, Data.class);
```

##### `String` to a map of custom object type

###### Map with `String` keys and `Map` or primitive types as values

```java
String json = """
              {
                  "victory" : {
                      "intVal" : 555,
                      "strVal" : "victory"
                  },
                  "omen" : {
                      "intVal" : 666,
                      "strVal" : "omen"
                  }
              }
              """;
Map<String, ?> map = GsonUtils.readMap(json);
```
**Note:** `map` values have either primitive type or `Map` or `List`.

###### `String` to a map with `String` keys and given type as value

```java
class Data {
    int intVal;
    String strVal;
}
```
```java
String json = """
              {
                  "victory" : {
                      "intVal" : 555,
                      "strVal" : "victory"
                  },
                  "omen" : {
                      "intVal" : 666,
                      "strVal" : "omen"
                  }
              }
              """;
Map<String, Data> map = GsonUtils.readMap(json, Data.class);
```

###### `String` to a map with `Integer` keys and given type as value

```java
class Data {
    int intVal;
    String strVal;
}
```
```java
String json = """
              {
                  "1" : {
                      "intVal" : 555,
                      "strVal" : "victory"
                  },
                  "2" : {
                      "intVal" : 666,
                      "strVal" : "omen"
                  }
              }
              """;
Map<Integer, Data> map = GsonUtils.readMap(json, Integer.class, Data.class);
```

#### Read json from `InputStream`

##### `InputStream` to a custom object type (but not a collection)

```java
class Data {
    int intVal;
    String strVal;
}
```
```json                        
{
    "intVal" : 666,
    "strVal" : "omen"
}
```
```java         
try (InputStream in = ...) {
    Data data = GsonUtils.readValue(in, Data.class);
}
```

##### `InputStream` to a list of custom object type

##### Read eager
```java
class Data {
    int intVal;
    String strVal;
}
```
```json                        
[
    {
        "intVal" : 555,
        "strVal" : "victory"
    },
    {
        "intVal" : 666,
        "strVal" : "omen"
    }
]
```
```java
try (InputStream in = ...) {
    List<Data> res = GsonUtils.readList(in, Data.class);
}
```

##### Read lazy

```java
class Data {
    int intVal;
    String strVal;
}
```
```json                        
[
    {
        "intVal" : 555,
        "strVal" : "victory"
    },
    {
        "intVal" : 666,
        "strVal" : "omen"
    }
]
```
```java
try (InputStream in = ...) {
    Iterator<Data> it = GsonUtils.readListLazy(in, Data.class);
    
    while (it.hasNext()) {
        Data data = it.next();
    }
}
```
##### `InputStream` to a map of custom object type

###### `InputStream` to a map with `String` keys and `Map` or primitive types as values

```json                        
{
    "victory" : {
        "intVal" : 555,
        "strVal" : "victory"
    },
    "omen" : {
        "intVal" : 666,
        "strVal" : "omen"
    }
}
```
```java
try (InputStream in = ...) {
    Map<String, ?> map = GsonUtils.readMap(in);
}
```
**Note:** `map` values have either primitive type or `Map` or `List`.

###### `InputStream` to a map with `String` keys and given type as value

```java
class Data {
    int intVal;
    String strVal;
}
```
```json                        
{
    "victory" : {
        "intVal" : 555,
        "strVal" : "victory"
    },
    "omen" : {
        "intVal" : 666,
        "strVal" : "omen"
    }
}
```
```java
try (InputStream in = ...) {
    Map<String, ?> map = GsonUtils.readMap(in, Data.class);
}
```

###### Map with `Integer` keys and given type as value

```java
class Data {
    int intVal;
    String strVal;
}
```
```json                        
{
    "1" : {
        "intVal" : 555,
        "strVal" : "victory"
    },
    "2" : {
        "intVal" : 666,
        "strVal" : "omen"
    }
}
```
```java
try (InputStream in = ...) {
    Map<Integer, Data> map = GsonUtils.readMap(in, Integer.class, Data.class);
}
```

#### Write any object to json

##### Write a custom object to json `String` (but not pretty print)

```java
class Data {
    int intVal;
    String strVal;
}
```
```java
Data data = new Data(555, "victory");
String json = GsonUtils.writeValue(data);
```
```json
{"intVal":555,"strVal":"victory"}
```

##### Write a `List` to json `String` (but not pretty print)

```java
class Data {
    int intVal;
    String strVal;
}
```
```java
List<Data> data = ListUtils.of(new Data(555, "victory"), new Data(666, "omen"));
String json = GsonUtils.writeValue(data);
```
```json
[{"intVal":555,"strVal":"victory"},{"intVal":666,"strVal":"omen"}]
```

##### Write a `Map` to json `String` (but not pretty print)

```java
public class Snippet {

    public String mapToJsonString() {
        Map<String, Data> map = MapUtils.of(
                "victory", new Data(555, "victory"),
                "omen", new Data(666, "omen"));
        return GsonUtils.writeValue(map);
    }
                 
    private static class Data {
        int intVal;
        String strVal;
    }
   
}
```
```json
{"victory":{"intVal":555,"strVal":"victory"},"omen":{"intVal":666,"strVal":"omen"}}
```

##### Links

*   Home page: https://github.com/oleg-cherednik/gson-utils

*   Maven:
    *   **central:** https://mvnrepository.com/artifact/ru.oleg-cherednik.utils/gson-utils
    *   **download:** https://repo1.maven.org/maven2/ru/oleg-cherednik/utils/gson-utils/
