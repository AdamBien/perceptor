package com.airhacks.interceptor.monitoring.entity;

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

import java.util.Comparator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author airhacks.com
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class InvocationStatistics {

    private String methodName;
    private long recentDuration;
    private long minDuration;
    private long maxDuration;
    private long averageDuration;
    private long numberOfExceptions;
    private long numberOfInvocations;
    private String exception;
    private long lastInvocationTimestamp;

    private long totalDuration;

    public InvocationStatistics(String methodName) {
        this.methodName = methodName;
    }

    public InvocationStatistics() {
        this.minDuration = Long.MAX_VALUE;
        this.maxDuration = Long.MIN_VALUE;
    }

    public void addInvocation(Invocation invocation) {
        numberOfInvocations++;
        if (this.methodName != null) {
            this.methodName = invocation.getMethodName();
        }
        this.recentDuration = invocation.getRecentDuration();
        this.lastInvocationTimestamp = invocation.getCreationTime();
        String currentException = invocation.getException();
        if (currentException != null) {
            this.exception = currentException;
            numberOfExceptions++;
        }
        this.recalculate();
    }

    void recalculate() {
        this.totalDuration += this.recentDuration;
        this.minDuration = Math.min(this.minDuration, this.recentDuration);
        this.maxDuration = Math.max(this.maxDuration, this.recentDuration);
        this.averageDuration = this.totalDuration / this.numberOfInvocations;
    }

    public String getMethodName() {
        return methodName;
    }

    public long getRecentDuration() {
        return recentDuration;
    }

    public long getMinDuration() {
        return minDuration;
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public long getAverageDuration() {
        return averageDuration;
    }

    public long getNumberOfExceptions() {
        return numberOfExceptions;
    }

    public boolean isExceptional() {
        return this.exception != null && !this.exception.isEmpty();
    }

    public String getException() {
        return exception;
    }

    public long getLastInvocationTimestamp() {
        return lastInvocationTimestamp;
    }

    public long getTotalDuration() {
        return this.totalDuration;
    }

    public static Comparator<InvocationStatistics> recent() {
        return (first, second) -> new Long(second.getLastInvocationTimestamp()).compareTo(first.getLastInvocationTimestamp());
    }

    public static Comparator<InvocationStatistics> unstable() {
        return (first, second) -> new Long(second.getNumberOfExceptions()).compareTo(first.getNumberOfExceptions());
    }

    public static Comparator<InvocationStatistics> slowest() {
        return (first, second) -> new Long(second.getMaxDuration()).compareTo(first.getMaxDuration());
    }

}
