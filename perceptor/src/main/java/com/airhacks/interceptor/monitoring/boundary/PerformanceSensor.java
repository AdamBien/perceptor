package com.airhacks.interceptor.monitoring.boundary;

import com.airhacks.interceptor.monitoring.entity.Invocation;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Gathers call duration and records potential exceptions.
 * *
 * @author adam-bien.com
 */
public class PerformanceSensor {

    @Inject
    Event<Invocation> monitoring;

    @AroundInvoke
    public Object monitor(InvocationContext ic) throws Exception {
        Invocation invocation = new Invocation(ic.getMethod());
        try {
            invocation.start();
            return ic.proceed();
        } catch (Exception ex) {
            invocation.setException(ex);
            throw ex;
        } finally {
            invocation.end();
            monitoring.fire(invocation);
        }
    }

}
