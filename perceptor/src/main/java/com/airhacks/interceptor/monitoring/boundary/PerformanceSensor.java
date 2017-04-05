package com.airhacks.interceptor.monitoring.boundary;

/*-
 * #%L
 * perceptor
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
