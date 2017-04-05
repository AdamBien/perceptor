package com.airhacks.interceptor.fastandslow.boundary;

import com.airhacks.interceptor.monitoring.boundary.PerformanceSensor;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Interceptors(PerformanceSensor.class)
public class Invoker {

    String blockingMethod(long callDuration) {
        try {
            Thread.sleep(callDuration);
        } catch (InterruptedException ex) {
            return "-";
        }
        return "+";
    }

    public String slow() {
        return this.blockingMethod(1000);
    }

    public String fast() {
        return this.blockingMethod(500);
    }

    public String lightning() {
        return this.blockingMethod(10);
    }

    public String exceptional() {
        throw new IllegalStateException("Don't call me!");
    }

    public String noOp() {
        return "+";
    }

}
