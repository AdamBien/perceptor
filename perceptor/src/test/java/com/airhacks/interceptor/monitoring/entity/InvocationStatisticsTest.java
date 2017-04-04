package com.airhacks.interceptor.monitoring.entity;

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
