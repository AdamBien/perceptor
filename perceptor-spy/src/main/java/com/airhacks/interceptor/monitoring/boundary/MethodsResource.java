package com.airhacks.interceptor.monitoring.boundary;

import com.airhacks.interceptor.monitoring.control.InvocationMonitoring;
import com.airhacks.interceptor.monitoring.entity.InvocationStatistics;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * <p>
 * Exposes technical performance and robustness monitoring data
 * ({@link MethodInvocation}) via REST
 *
 * @author adam-bien.com
 */
public class MethodsResource {

    InvocationMonitoring mes;

    public MethodsResource(InvocationMonitoring mes) {
        this.mes = mes;
    }

    @GET
    @Path("exceptional")
    public List<InvocationStatistics> exceptionalMethods() {
        return mes.getExceptional();
    }

    @GET
    @Path("slowest")
    public List<InvocationStatistics> slowestMethods() {
        return mes.getSlowest();
    }

    @GET
    @Path("recent")
    public List<InvocationStatistics> recentMethods() {
        return mes.getRecent();
    }

    @GET
    @Path("unstable")
    public List<InvocationStatistics> unstableMethods() {
        return mes.getUnstable();
    }

    @GET
    public List<InvocationStatistics> all() {
        return this.mes.getRecent();
    }

}
