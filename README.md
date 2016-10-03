**NMT = Native Memory Tracking** (https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr007.html)

**Important : Native Memory Tracking is enabled from Java 8**

# Local installation

To install this dependency to your local maven repo run this : 

```
mvn clean install
```

then you can use it as dependency in your `pom.xml` : 

```
<dependency>
    <groupId>com.marekcabaj</groupId>
    <artifactId>nmt-metrics</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

# Usage

This dependency is meant to be used in Spring Boot application so you need to have this parent declaration in your `pom.xml` 

```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.4.1.RELEASE</version>
</parent>
```

### Using scheduled NMT properties handling

If you want to read NMT properties in scheduled manner then you need to define 2 properties in your Spring Boot `application.properties` : 

```
nmt.scheduler.enabled=true
nmt.scheduler.delay=5000
```

* By default (when `nmt.scheduler.enabled` property is missing) the scheduled NMT properties reading is disabled
* By default (when `nmt.scheduler.delay` property is missing) delay is 60000 (1 minute) 
* Delay should be specified in miliseconds. 

To handle NMT properties reads you need to implement `NMTPropertiesHandler` and mark your class with `@Component` : 

```
@Component
public class Handler implements NMTPropertiesHandler {

    @Override
    public void handleNMTProperties(Map<String, Map<String, Integer>> properties) {
        // handle properties here
    }
}
```

`properties` map has following structure : 

```
categoryName1:
	property1:100
	property2:300
categoryName2:
	property1:500
	property2:0

...

```

Real example : 

```
java.heap:
	committed:286720
	reserved:4167680
thread:
	committed:39659
	reserved:39659

...

```


### Adding NMT properties to Spring Boot actuator `/metrics` endpoint

You don't need to write any code to add NMT properties to `/metrics` endpoint. You just need to add following dependecies : 

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

By default NMT properties are added to `/metrics` endpoint. If you want to disable them you need to add this to your `application.properties` : 

```
nmt.metrics.enabled=false
```
