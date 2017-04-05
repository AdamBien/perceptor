package com.airhacks.interceptor.monitoring.boundary;

/*-
 * #%L
 * perceptor-spy
 * %%
 * Copyright (C) 2017 Adam Bien
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.airhacks.interceptor.monitoring.control.InvocationMonitoring;
import com.airhacks.interceptor.monitoring.entity.InvocationStatistics;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * <p>
 * Exposes technical performance and robustness monitoring data via REST
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
