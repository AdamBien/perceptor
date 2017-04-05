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
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;

@ApplicationScoped
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
