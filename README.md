[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ru.oleg-cherednik.gson/gson-utils/badge.svg)](https://maven-badges.herokuapp.com/maven-central/ru.oleg-cherednik.gson/gson-utils)
[![javadoc](https://javadoc.io/badge2/ru.oleg-cherednik.gson/gson-utils/javadoc.svg)](https://javadoc.io/doc/ru.oleg-cherednik.gson/gson-utils)
[![java8](https://badgen.net/badge/java/8+/blue)](https://badgen.net/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

[![circle-ci](https://circleci.com/gh/oleg-cherednik/gson-utils/tree/master.svg?style=shield)](https://app.circleci.com/pipelines/github/oleg-cherednik/gson-utils)
[![codacy-quality](https://app.codacy.com/project/badge/Grade/ba0faab92b44432491376ee5d331a63e?branch=master)](https://app.codacy.com/gh/oleg-cherednik/gson-utils/dashboard?branch=master)

<details><summary>develop</summary>
<p>

[![circle-ci](https://circleci.com/gh/oleg-cherednik/gson-utils/tree/dev.svg?style=shield)](https://app.circleci.com/pipelines/github/oleg-cherednik/gson-utils)
[![codecov](https://codecov.io/gh/oleg-cherednik/gson-utils/branch/dev/graph/badge.svg?token=2wNFKJhR70)](https://codecov.io/gh/oleg-cherednik/gson-utils)
[![vulnerabilities](https://snyk.io/test/github/oleg-cherednik/gson-utils/badge.svg?targetFile=build.gradle)](https://snyk.io/test/github/oleg-cherednik/gson-utils?targetFile=build.gradle)
[![codacy-quality](https://app.codacy.com/project/badge/Grade/ba0faab92b44432491376ee5d331a63e?branch=dev)](https://app.codacy.com/gh/oleg-cherednik/gson-utils/dashboard?branch=dev)

</p>
</details>  

# gson-utils
> [Gson](https://github.com/google/gson) usability utilities. It's designed to
> add additional features like easy and centralized configuration, builder or
> static method set. Artifact does not include direct `Gson` dependencies. It is
> up to you to add them into your project. 

## Features
*   Encapsulate all checked exceptions from Gson with custom runtime exception;
*   A central place for configuration;
*   A central place for holding `Gson` instances;
*   Utility class to make most common operations much more comfortable to use;
*   Ability to change `Zone` to save `ZonedDateTime` independently of original zone;
*   `Reader`/`Writer` support for objects, lists and maps;
*   Lazy read support for list from `Writer`;
*   Read numeric as `Integer`, `Long`, `BigInteger` or `Double` (but not only as `Double`).
*   Advanced `Reader`/`Writer` support for `enum`. 

## Build Tools

### Gradle

```groovy
compile 'ru.oleg-cherednik.gson:gson-utils:2.2'
```

### Maven

```xml
<dependency>
    <groupId>ru.oleg-cherednik.gson</groupId>
    <artifactId>gson-utils</artifactId>
    <version>2.2</version>
</dependency>
```                                                    

In the version, first part is the major version of `Gson` that is used in this utils.
The second part is the `gson-utils` version. This number is unique.

**Note:** `gson-utils` does not contain dependency to the specific `gson` version, so you have to
add it additionally:

```groovy
compile 'com.google.code.gson:gson:2.8.7'
```
```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.7</version>
</dependency>
```

## Usage 

*   [GsonUtils](#gsonutils-class) - utility class with set of methods to use json transformation;
*   [GsonUtilsBuilder](#gsonutilsbulder-class) - builder for Gson instance contains all configuration properties;
*   [GsonDecorator](#gsondecorator-class) - decorator class to hold Gson instance with additional methods;
*   [GsonUtilsHelper](#gsonutilshelper-class) - helper class is used as a holder for default properties;
*   [EnumId](#work-with-enum) - advanced enum serialization support;

<details><summary>Data class for examples</summary>
<p>

```java   
package ru.olegcherednik.utils.gson.data;

public class Data {

    private int intVal;
    private String strVal;

    public Date() {}

    public Data(int intValue, String strValue) {
        this.intValue = intValue;
        this.strValue = strValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getStrVal() {
        return strValue;
    }

}
```

</p>
</details>

### GsonUtils class

#### Read json

<details><summary>details</summary>
<p>

##### Read json string to a custom object type (but not a collection)

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
}
```

##### Read json string to a list of custom object type

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

}
```

##### Read json string to a map with string as keys and map or primitive types as values

```java
public class Snippet {

    public static Map<String, Object> jsonStringToMap() {
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
                 
}
```
**Note:** `Map` values have either primitive type or `Map` or `List`.

##### Read json string to a map with string as keys and given type as value

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
                 
}
```

##### Read json string to a map with given type for key and value

```java
public class Snippet {

    public static Map<Integer, Data> jsonStringToMap() {
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
                 
}
```

##### Read json from a reader to a custom object type (but not a collection)

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
                 
}
```

##### Read json from a reader eager to a list of custom object type
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
                 
}
```

##### Read json from a reader lazy to a list of custom object type

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
                 
}
```
##### Read json from a reader to a map with string as keys and map or primitive types as a values

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

    public static Map<String, Object> readJsonFromFileToMap(File file) {
        try (Reader in = new FileReader(file)) {
            return GsonUtils.readMap(in);
        }
    }
   
}
```
**Note:** `map` values have either primitive type or `Map` or `List`.

##### Read json from a reader to a map with string as keys and given type as a value

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
                 
}
```

##### Read json from a reader to a map with Integer as keys and given type as a value

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
                 
}
```

</p>
</details>

#### Write json

<details><summary>details</summary>
<p>

##### Write a custom object to json string

```java
public class Snippet {

    public static String objToJsonString() {
        Data data = new Data(555, "victory");
        return GsonUtils.writeValue(data);
    }
                 
}
```
```json
{"intVal":555,"strVal":"victory"}
```

##### Write a list to json string

```java
public class Snippet {

    public static String listToJsonString() {
        List<Data> data = List.of(new Data(555, "victory"), new Data(666, "omen"));
        return GsonUtils.writeValue(map);
    }
                 
}
```
```json
[{"intVal":555,"strVal":"victory"},{"intVal":666,"strVal":"omen"}]
```

##### Write a map to json string

```java
public class Snippet {

    public static String mapToJsonString() {
        Map<String, Data> data = Map.of(
                "victory", new Data(555, "victory"),
                "omen", new Data(666, "omen"));
        return GsonUtils.writeValue(data);
    }
                 
}
```
```json
{"victory":{"intVal":555,"strVal":"victory"},"omen":{"intVal":666,"strVal":"omen"}}
```

##### Write any object to pretty print json string

```java
public class Snippet {

    public static String mapToPrettyPrintJsonString() {
        Map<String, Data> data = Map.of(
                "victory", new Data(555, "victory"),
                "omen", new Data(666, "omen"));
        return GsonUtils.prettyPrint().writeValue(data);
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

</p>
</details>

#### Work with enum

<details><summary>details</summary>
<p>

By default, `Gson` serializes or deserializes enums by the case-sensitive `name`.
It could give a problem as well as sometime it's better to change name of the
constant when serialize it into `json`.

To solve these issues, `GsonUtils` provides an interface `EnumId` with range of
method to serialize or deserialize enums. To use it, you have to declare your
enums according to the following snippet:

```java
public enum Auto implements EnumId {
    AUDI("audi"),
    BMW("bmw"),
    MERCEDES("mercedes");

    private final String id;

    Auto(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @JsonCreator
    public static Auto parseId(String id) {
        return EnumId.parseId(Auto.class, id);
    }
}
```  

Where `@JsonCreator` is an *optional* annotation to mark a single method that
accepts exactly one string parameter to deserialize an enum constant.

That's it! You can use `gson-utils` methods as usual. 

</p>
</details>

### Spring bean

<details><summary>details</summary>
<p>

#### Create gson bean based on the default configuration

`GsonUtilsHelper` class contains *default gson configuration*. This configuration
can be used to create gson bean:

```java
@Configuration
public class AppConfig {

    @Bean
    public GsonDecorator gsonDecorator() {
        return GsonUtilsHelper.createGsonDecorator();
    }

}
```

#### Create gson bean based on the custom configuration

To customize gson configuration, a new instance of `GsonUtilsBuilder` should be
created and configured. Then this instance should be used to create `GsonDecorator`.

```java
@Configuration
public class AppConfig {

    @Bean
    public GsonUtilsBuilder gsonUtilsBuilder() {
        // customize Gson here
        return new GsonUtilsBuilder();
    }

    @Bean
    public GsonDecorator gsonDecorator(GsonUtilsBuilder gsonUtilsBuilder) {
        return GsonUtilsHelper.createGsonDecorator(gsonUtilsBuilder);
    }

}
```

### Using of gson bean

A new `GsonDecorator` should be used to work with json instead of using a `Gson` instance. 

```java
@Service
public class SpringBootService {

    @Autowired
    private GsonDecorator gson;

    public String toJson(Data data) {
        return gson.writeValue(data);
    }

    public Data fromJson(String json) {
        return gson.readValue(json, Data.class);
    }

}
```

</p>
</details>

### GsonUtilsBuilder class

<details><summary>details</summary>
<p>

The class provides ability to customize `Gson` instance and create a new `Gson`
instances with current settings.

#### Use custom type adapter factory

Following snippet shows how to serialize `Date` to `UTC` time zone and format `"HH:mm:ss yyyy-MM-dd"`. 
You have to create a new instance of `GsonUtilsBuilder` and add custom implementation.

```java
public class Snippet {

    public static Gson createCustomGson() {
        UnaryOperator<ZoneId> zoneModifier = GsonUtilsBuilder.ZONE_MODIFIER_TO_UTC;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd");
        TypeAdapter<Date> typeAdapter = new DateTypeAdapter(zoneModifier, dateTimeFormatter);
        GsonUtilsBuilder builder = new GsonUtilsBuilder().registerTypeAdapter(Data.class, typeAdapter);
        return builder.gson();
    }
   
}
```
</p>
</details>

### GsonDecorator class

<details><summary>details</summary>
<p>

This is a decorator over the standard `Gson` class with more additional method. `GsonUtils` chooses required `Gson`
instance and delegates all work to the `GsonDecorator`.

</p>
</details>

### GsonUtilsHelper class

<details><summary>details</summary>
<p>

This class provides set of methods to create a `Gson` instances based on given `GsonUtilsBulder`.

</p>
</details>

### Links

*   Home page: https://github.com/oleg-cherednik/gson-utils

*   Maven:
    *   **central:** https://mvnrepository.com/artifact/ru.oleg-cherednik.gson/gson-utils
    *   **download:** https://repo1.maven.org/maven2/ru/oleg-cherednik/gson/gson-utils/
   
