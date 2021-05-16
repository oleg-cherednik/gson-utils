[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ru.oleg-cherednik.utils/gson-utils/badge.svg)](https://maven-badges.herokuapp.com/maven-central/ru.oleg-cherednik.utils/gson-utils)
[![javadoc](https://javadoc.io/badge2/ru.oleg-cherednik.utils/gson-utils/javadoc.svg)](https://javadoc.io/doc/ru.oleg-cherednik.utils/gson-utils)
[![java8](https://badgen.net/badge/java/8+/blue)](https://badgen.net/)
[![travis-ci](https://travis-ci.com/oleg-cherednik/gson-utils.svg?branch=dev)](https://travis-ci.com/oleg-cherednik/gson-utils)
[![circle-ci](https://circleci.com/gh/oleg-cherednik/GsonUtils/tree/dev.svg?style=shield)](https://app.circleci.com/pipelines/github/oleg-cherednik/gson-utils)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![codecov](https://codecov.io/gh/oleg-cherednik/GsonUtils/branch/dev/graph/badge.svg?token=UnqBOd1gbA)](https://codecov.io/gh/oleg-cherednik/GsonUtils)
[![Known Vulnerabilities](https://snyk.io/test/github/oleg-cherednik/GsonUtils/badge.svg?targetFile=build.gradle)](https://snyk.io/test/github/oleg-cherednik/GsonUtils?targetFile=build.gradle)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ba0faab92b44432491376ee5d331a63e)](https://www.codacy.com/gh/oleg-cherednik/GsonUtils/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=oleg-cherednik/GsonUtils&amp;utm_campaign=Badge_Grade)     

# gson-utils
> a java tool to make working with [Gson](https://github.com/google/gson) more comfortable

## Features
*   Encapsulate all checked exceptions from Gson with custom runtime exception;
*   A central place for configuration;
*   A central place for holding `Gson` instances;
*   Utility class to make most common operations much more comfortable to use;
*   Ability to change `Zone` to save `ZonedDateTime` independently of original zone;
*   `Reader`/`Writer` support for objects, lists and maps;
*   Lazy read support for list from `Writer`;
*   Read numeric as `Integer`, `Long`, `BigInteger` or `Double` (but not only as `Double`). 

## Gradle

```groovy
compile 'ru.oleg-cherednik.utils.gson:gson-utils:2.1'
```

## Maven

```xml
<dependency>
    <groupId>ru.oleg-cherednik.utils.gson</groupId>
    <artifactId>gson-utils</artifactId>
    <version>2.1</version>
</dependency>
```                                                    

In the version, first part is the major version of `Gson` that is used in this utils.
The second part is the `gson-utils` version. This number is unique. 

## Usage 

*   [GsonUtils](#gsonutils-class) - utility class with set of methods to use json transformation;

### GsonUtils class

#### Read json from `String`

##### Read json `String` to a custom object type (but not a collection)

```java
public class Snippet {

    public static Data jsonStringToObj() {
        String json = """
                      {
                        "intVal": 666,
                        "strVal": "omen"
                      }
                      """;
        return GsonUtils.readValue(json, Data.class);
    }
                 
    private static class Data {
        int intVal;
        String strVal;
    }
   
}
```

##### Read json `String` to a list of custom object type

```java
public class Snippet {

    public static List<Data> jsonStringToList() {
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
        return GsonUtils.readList(json, Data.class);
    }
                 
    private static class Data {
        int intVal;
        String strVal;
    }
   
}
```

##### Read json `String` to a map of custom object type

###### Read json `Stirng` to a map with `String` keys and `Map` or primitive types as values

```java
public class Snippet {

    public static Map<String, ?> jsonStringToMap() {
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
        return GsonUtils.readMap(json);
    }
                 
    private static class Data {
        int intVal;
        String strVal;
    }
   
}
```
**Note:** `Map` values have either primitive type or `Map` or `List`.

###### Read json `String` to a map with `String` keys and given type as value

```java
public class Snippet {

    public static Map<String, Data> jsonStringToMap() {
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
        return GsonUtils.readMap(json, Data.class);
    }
                 
    private static class Data {
        int intVal;
        String strVal;
    }
   
}
```

###### Read json `String` to a map with given type for key and value

```java
public class Snippet {

    public static Map<String, Data> jsonStringToMap() {
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
        return GsonUtils.readMap(json, Integer.class, Data.class);
    }
                 
    private static class Data {
        int intVal;
        String strVal;
    }
   
}
```

#### Read json from `Reader`

##### Read json from `Reader` to a custom object type (but not a collection)

```json                        
{
    "intVal" : 666,
    "strVal" : "omen"
}
```
```java
public class Snippet {

    public static Data readJsonFromFileToObj(File file) {
        try (Reader in = new FileReader(file)) {
            return GsonUtils.readValue(in, Data.class);
        }
    }
                 
    private static class Data {
        int intVal;
        String strVal;
    }
   
}
```

##### Read json from `Reader` to a list of custom object type

##### Read json from `Reader` eager to a list of custom object type
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
public class Snippet {

    public static List<Data> readJsonEdgerFromFileToList(File file) {
        try (Reader in = new FileReader(file)) {
            return GsonUtils.readList(in, Data.class);
        }
    }
                 
    private static class Data {
        int intVal;
        String strVal;
    }
   
}
```

##### Read json from `Reader` lazy to a list of custom object type

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
public class Snippet {

    public static List<Data> readJsonLazyFromFileToList(File file) {
        try (Reader in = new FileReader(file)) {
            List<Data> res = new ArrayList<>();
            Iterator<Data> it = GsonUtils.readListLazy(in, Data.class);
            
            while (it.hasNext())
                res.add(it.next());
            
            return res;
        }
    }
                 
    private static class Data {
        int intVal;
        String strVal;
    }
   
}
```
##### Read json from `Reader` to a map of custom object type

###### Read json from `Reader` to a map with `String` keys and `Map` or primitive types as a values

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
public class Snippet {

    public static Map<String, ?> readJsonFromFileToMap(File file) {
        try (Reader in = new FileReader(file)) {
            return GsonUtils.readMap(in);
        }
    }
   
}
```
**Note:** `map` values have either primitive type or `Map` or `List`.

###### Read json from `Reader` to a map with `String` keys and given type as a value

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
public class Snippet {

    public static Map<String, Data> readJsonFromFileToMap(File file) {
        try (Reader in = new FileReader(file)) {
            return GsonUtils.readMap(in, Data.class);
        }
    }
                 
    private static class Data {
        int intVal;
        String strVal;
    }
   
}
```

###### Read json from `Reader` to a map with `Integer` keys and given type as a value

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
public class Snippet {

    public static Map<Integer, Data> readJsonFromFileToMap(File file) {
        try (Reader in = new FileReader(file)) {
            return GsonUtils.readMap(in, Integer.class, Data.class);
        }
    }
                 
    private static class Data {
        int intVal;
        String strVal;
    }
   
}
```

#### Write any object to json

##### Write a custom object to json `String` (but not pretty print)

```java
public class Snippet {

    public static String objToJsonString() {
        Data data = new Data(555, "victory");
        return GsonUtils.writeValue(data);
    }
                 
    private static class Data {
        int intVal;
        String strVal;
    }
   
}

class Data {
    int intVal;
    String strVal;
}
```
```json
{"intVal":555,"strVal":"victory"}
```

##### Write a `List` to json `String` (but not pretty print)

```java
public class Snippet {

    public static String listToJsonString() {
        List<Data> data = List.of(new Data(555, "victory"), new Data(666, "omen"));
        return GsonUtils.writeValue(map);
    }
                 
    private static class Data {
        int intVal;
        String strVal;
    }
   
}
```
```json
[{"intVal":555,"strVal":"victory"},{"intVal":666,"strVal":"omen"}]
```

##### Write a `Map` to json `String` (but not pretty print)

```java
public class Snippet {

    public static String mapToJsonString() {
        Map<String, Data> data = Map.of(
                "victory", new Data(555, "victory"),
                "omen", new Data(666, "omen"));
        return GsonUtils.writeValue(data);
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

##### Write any object to pretty print json `String`

```java
public class Snippet {

    public static String mapToPrettyPrintJsonString() {
        Map<String, Data> data = Map.of(
                "victory", new Data(555, "victory"),
                "omen", new Data(666, "omen"));
        return GsonUtils.prettyPrint().writeValue(data);
    }
                 
    private static class Data {
        int intVal;
        String strVal;
    }
   
}
```
```json
{
  "victory": {
    "intVal": 555,
    "strVal": "victory"
  },
  "omen": {
    "intVal": 666,
    "strVal": "omen"
  }
}
```

##### Links

*   Home page: https://github.com/oleg-cherednik/gson-utils

*   Maven:
    *   **central:** https://mvnrepository.com/artifact/ru.oleg-cherednik.utils/gson-utils
    *   **download:** https://repo1.maven.org/maven2/ru/oleg-cherednik/utils/gson-utils/
   
