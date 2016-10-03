# NMT = Native Memory Tracking of Container memory for Java Apps

## What does this Library do ?
This library adds custom [native memory tracking](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr007.html) metrics to the `/metrics` spring boot endpoint.  

## Why will I ever need Java Native Memory Tracking ?

When moving Java apps, unlike in the past, with CF there is a hard limit on total system memory before. Deploying to WAS/WebLogic/JBOSS (or any other application container) is going to be done on a system with swap space. That means if they don’t have their memory settings quite right, the worst thing that will happen is that they use a bit of swap space. If it’s just a little swap, they probably won’t even notice it happening. With container based systems, there’s much less forgiveness in the system. Exceeding the memory limit by even a byte will result in your app being killed.


Diagnosing and debugging OOM errors with Java applications in Cloud Foundry or in any container based platform like Kubernetes or Docker is difficult. The OS level metrics are often confusing and don't provide any insight unless you are an expert in Linux system internals. I recommend relying on the diagnostics provided by the JVM to track down OOM memory leaks. The verbose GC logs provide insight into the heap portion of container memory. There are a variety of tools available{ [HeapAnalyzer](http://www.eclipse.org/mat/), [gceasy](http://gceasy.io/), [pmat](http://ibm.co/1pUjktc) } to triage and analyze java heaps and verbose GC logs; however getting any insight into native aka non-heap portion of the memory is very difficult.  The native memory tracking introduced in the JDK from Java 8 provides valuable insight into the portion of the iceberg under water.  The key element of debugging native OOMs is to understand the metrics report and chart the trend-lines to understand the leaking contributor.


## Local installation

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

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

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

### Leveraging NMT in Cloud Foundry

Push your application with the following environment variable and therafter collect the logs via the NMT Property handling code below or via simple shell script that curls the `/metrics` actuator endpoint.
`JAVA_OPTS: -Djava.security.egd=file:///dev/urandom -XX:NativeMemoryTracking=summary -XX:+PrintHeapAtGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps`
