package com.airhacks.interceptor.fastandslow.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Path("invocation")
public class InvocationsResource {

    @Inject
    Invoker invoker;

    @GET
    @Path("slow")
    public String slow() {
        return this.invoker.slow();
    }

    @GET
    @Path("fast")
    public String fast() {
        return this.invoker.fast();
    }

    @GET
    @Path("lightning")
    public String lightning() {
        return this.invoker.lightning();
    }

    @GET
    @Path("exceptional")
    public String exceptional() {
        return this.invoker.exceptional();
    }
}
