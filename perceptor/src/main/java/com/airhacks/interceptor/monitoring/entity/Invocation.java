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

import java.lang.reflect.Method;

/**
 * Represents a single method call. Is not a part of the public API.
 *
 * @author adam-bien.com
 */
public class Invocation {

    private String methodName;
    private long recentDuration;
    private String exception;

    private long start;

    public Invocation(Method methodName) {
        this.methodName = methodName.toGenericString();
    }

    public Invocation(String methodName, long recentDuration, String exception, long start) {
        this.methodName = methodName;
        this.recentDuration = recentDuration;
        this.exception = exception;
        this.start = start;
    }

    public void start() {
        this.start = System.currentTimeMillis();
    }

    public void end() {
        this.recentDuration = System.currentTimeMillis() - this.start;
    }

    public long getCreationTime() {
        return this.start;
    }

    public void setException(Exception exception) {
        this.exception = exception.toString();
    }

    public String getMethodName() {
        return methodName;
    }

    public long getRecentDuration() {
        return recentDuration;
    }

    public long getStart() {
        return start;
    }

    public String getException() {
        return exception;
    }

    public boolean hasException() {
        return this.exception != null;
    }

    public void setRecentDuration(long recentDuration) {
        this.recentDuration = recentDuration;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.methodName != null ? this.methodName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Invocation other = (Invocation) obj;
        if ((this.methodName == null) ? (other.methodName != null) : !this.methodName.equals(other.methodName)) {
            return false;
        }
        return true;
    }

    public boolean slowerThan(Invocation other) {
        if (other == null) {
            return true;
        }
        return this.recentDuration > other.recentDuration;
    }

    @Override
    public String toString() {
        return "Invocation{" + "methodName=" + methodName + ", recentDuration=" + recentDuration + ", exception=" + exception + ", start=" + start + '}';
    }

}
