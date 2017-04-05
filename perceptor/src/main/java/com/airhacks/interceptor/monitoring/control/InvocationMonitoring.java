package com.airhacks.interceptor.monitoring.control;

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
import com.airhacks.interceptor.monitoring.entity.InvocationStatistics;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

/**
 * Receives, stores, manages, exposes (to REST) and extracts statistics from
 * {@link Invocation}
 *
 * This singleton is intended to implement and expose all monitoring statistics
 *
 * @author adam-bien.com
 */
@ApplicationScoped
public class InvocationMonitoring {

    private ConcurrentHashMap<String, InvocationStatistics> statistics;

    @PostConstruct
    public void init() {
        this.statistics = new ConcurrentHashMap<>();
    }

    public void onNewCall(@Observes Invocation invocation) {
        final String methodName = invocation.getMethodName();
        this.statistics.putIfAbsent(methodName, new InvocationStatistics(methodName));
        InvocationStatistics stats = this.statistics.get(methodName);
        stats.addInvocation(invocation);
    }

    public List<InvocationStatistics> getExceptional() {
        return this.statistics.values().stream().
                filter(i -> i.isExceptional()).
                collect(Collectors.toList());
    }

    public List<InvocationStatistics> getUnstable() {
        return this.statistics.values().stream().
                filter(i -> i.isExceptional()).
                sorted(InvocationStatistics.unstable()).
                collect(Collectors.toList());
    }

    public List<InvocationStatistics> getSlowest() {
        return this.statistics.values().stream().
                sorted(InvocationStatistics.slowest()).
                collect(Collectors.toList());
    }

    public List<InvocationStatistics> getRecent() {
        return this.statistics.values().stream().
                sorted(InvocationStatistics.recent()).
                collect(Collectors.toList());
    }

    public void reset() {
        this.statistics.clear();
    }

}
