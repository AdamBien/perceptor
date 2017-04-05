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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class InvocationStatisticsTest {

    InvocationStatistics cut;

    @Before
    public void init() {
        this.cut = new InvocationStatistics();
    }

    @Test
    public void addInvocation() {
        Invocation first = new Invocation(null, 15, null, 1);
        Invocation second = new Invocation(null, 15, null, 2);
        Invocation another = new Invocation(null, 30, null, 2);
        this.cut.addInvocation(first);
        this.cut.addInvocation(second);
        this.cut.addInvocation(another);
        long averageDuration = this.cut.getAverageDuration();
        assertThat(averageDuration, is(20l));

        long maxDuration = this.cut.getMaxDuration();
        assertThat(maxDuration, is(30l));

        long minDuration = this.cut.getMinDuration();
        assertThat(minDuration, is(15l));

        long lastInvocation = this.cut.getLastInvocationTimestamp();
        assertThat(lastInvocation, is(2l));

        long totalDuration = this.cut.getTotalDuration();
        assertThat(totalDuration, is(60l));

        long recentDuration = this.cut.getRecentDuration();
        assertThat(recentDuration, is(30l));

    }

}
