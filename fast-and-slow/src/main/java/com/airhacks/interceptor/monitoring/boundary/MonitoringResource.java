package com.airhacks.interceptor.monitoring.boundary;

import com.airhacks.interceptor.monitoring.control.InvocationMonitoring;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;

@Stateless
@Path("monitoring")
public class MonitoringResource {

    @Inject
    InvocationMonitoring mes;

    @Path("methods")
    public MethodsResource methods() {
        return new MethodsResource(mes);
    }

    @DELETE
    public void reset() {
        this.mes.reset();

    }

}
