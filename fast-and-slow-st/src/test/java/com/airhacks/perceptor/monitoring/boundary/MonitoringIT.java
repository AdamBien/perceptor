package com.airhacks.perceptor.monitoring.boundary;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class MonitoringIT {

    private Client client;
    private WebTarget invocationTarget;
    private WebTarget methodsMonitoringTarget;
    private WebTarget monitoringTarget;

    @Before
    public void init() {
        this.client = ClientBuilder.newClient();
        this.invocationTarget = this.client.target("http://localhost:8080/fast-and-slow/resources/invocation/");
        this.methodsMonitoringTarget = this.client.target("http://localhost:8080/fast-and-slow/resources/monitoring/methods");
        this.monitoringTarget = this.client.target("http://localhost:8080/fast-and-slow/resources/monitoring/");
    }

    @Test
    public void allStatisticsForHost() {
        invokeMethod("noop");
        invokeMethod("fast");
        JsonArray invocationStatistics = this.methodsMonitoringTarget.
                request(MediaType.APPLICATION_JSON).
                get(JsonArray.class);
        assertThat(invocationStatistics.size(), is(2));
    }

    @Test
    public void exceptionalStats() {
        try {
            invokeMethod("exceptional");
        } catch (Exception ex) {
        }
        //[{"hostName":"unknown","methodName":"public java.lang.String com.bmw.eve.fastandslow.boundary.Invoker.exceptional()",
        //"recentDuration":0,"minDuration":0,"maxDuration":0,"averageDuration":0,"numberOfExceptions":1,"numberOfInvocations":1,
        //"exception":"java.lang.IllegalStateException: Don't call me!","lastInvocationTimestamp":1469431094017,"totalDuration":0}]
        JsonArray invocationStatistics = this.methodsMonitoringTarget.
                request(MediaType.APPLICATION_JSON).
                get(JsonArray.class);
        assertThat(invocationStatistics.size(), is(1));
        System.out.println("invocationStatistics = " + invocationStatistics);
        long count = invocationStatistics.stream().map(a -> (JsonObject) a).
                map(i -> i.getString("methodName")).
                filter(m -> !m.contains("exceptional")).
                count();
        assertThat(count, is(0l));
        JsonObject invocation = invocationStatistics.getJsonObject(0);
        int numberOfInvocations = invocation.getInt("numberOfInvocations");
        assertThat(numberOfInvocations, is(1));
        int numberOfExceptions = invocation.getInt("numberOfExceptions");
        assertThat(numberOfExceptions, is(1));
    }

    @Test
    public void slowest() {
        invokeMethod("noop");
        invokeMethod("lightning");
        invokeMethod("fast");
        invokeMethod("slow");
        JsonArray slowest = this.methodsMonitoringTarget.
                path("slowest").
                request(MediaType.APPLICATION_JSON).
                get(JsonArray.class);
        assertThat(slowest.size(), is(4));
        System.out.println("slowest = " + slowest);
        assertTrue(slowest.getJsonObject(0).getString("methodName").contains("slow"));
        assertTrue(slowest.getJsonObject(1).getString("methodName").contains("fast"));
        assertTrue(slowest.getJsonObject(2).getString("methodName").contains("lightning"));
        assertTrue(slowest.getJsonObject(3).getString("methodName").contains("noOp"));
    }

    void invokeMethod(String method) {
        String result = this.invocationTarget.path(method).request().get(String.class);
        assertThat(result, is("+"));
    }

    @After
    public void cleanup() {
        Response response = this.monitoringTarget.
                request().
                delete();
        assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));
    }

}
