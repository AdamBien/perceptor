# perceptor
Java EE Performance Measuring Interceptor


## installation

To gather statistics without automatically exposing them use the following dependency:

```xml
<dependency>
    <groupId>com.airhacks</groupId>
    <artifactId>perceptor</artifactId>
    <version>[RECENT]</version>
</dependency>
```
The statistics become available via injection:

```java
import com.airhacks.interceptor.monitoring.control.InvocationMonitoring;

public class MonitoringResource {

    @Inject
    InvocationMonitoring mes;
}
```

The `perceptor-spy` dependency gathers statistics and exposes them via
the `/monitoring/methods` endpoint:

```xml
<dependency>
    <groupId>com.airhacks</groupId>
    <artifactId>perceptor-spy</artifactId>
    <version>[RECENT]</version>
</dependency>
```

## usage

```java

import com.airhacks.interceptor.monitoring.boundary.PerformanceSensor;

@Interceptors(PerformanceSensor.class)
public class Invoker {


    public String slow() {}

    public String fast() {}

}
```
See you at [Java EE Performance, Monitoring and Troubleshooting](http://workshops.adam-bien.com/performance.htm) and/or [Java EE Microservices](http://workshops.adam-bien.com/microservices.htm) workshops