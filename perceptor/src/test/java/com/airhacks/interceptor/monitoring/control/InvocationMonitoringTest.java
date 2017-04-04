package com.airhacks.interceptor.monitoring.control;

import com.airhacks.interceptor.monitoring.control.InvocationMonitoring;
import com.airhacks.interceptor.monitoring.entity.Invocation;
import com.airhacks.interceptor.monitoring.entity.InvocationStatistics;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class InvocationMonitoringTest {

    InvocationMonitoring cut;

    @Before
    public void init() {
        this.cut = new InvocationMonitoring();
        this.cut.init();
    }

    @Test
    public void lastCreatedComesFirst() {
        Invocation first = new Invocation("lastCreatedSorting", 2, null, 1);
        Invocation next = new Invocation("lastCreatedSorting", 2, null, 2);
        Invocation last = new Invocation("lastCreatedSorting", 2, null, 3);
        this.cut.onNewCall(first);
        this.cut.onNewCall(next);
        this.cut.onNewCall(last);
        List<InvocationStatistics> recent = this.cut.getRecent();
        assertFalse(recent.isEmpty());
        InvocationStatistics mostRecent = recent.get(0);
        assertThat(mostRecent.getLastInvocationTimestamp(), is(last.getCreationTime()));
    }

    @Test
    public void slowestComesFirst() {
        Invocation lightning = new Invocation("lightning", 1, null, 1);
        Invocation fast = new Invocation("fast", 2, null, 2);
        Invocation ok = new Invocation("ok", 3, null, 3);
        this.cut.onNewCall(lightning);
        this.cut.onNewCall(fast);
        this.cut.onNewCall(ok);
        List<InvocationStatistics> slowest = this.cut.getSlowest();
        assertFalse(slowest.isEmpty());
        InvocationStatistics first = slowest.get(0);
        assertThat(first.getMethodName(), is(ok.getMethodName()));
    }

    @Test
    public void exceptional() {
        Invocation ex1 = new Invocation("unstable", 1, "lazy - don't call me", 1);
        Invocation ex2 = new Invocation("erroneuous", 2, "something happened to me", 2);
        Invocation s = new Invocation("stable", 3, null, 2);
        this.cut.onNewCall(ex1);
        this.cut.onNewCall(ex2);
        this.cut.onNewCall(s);
        List<InvocationStatistics> exceptional = this.cut.getExceptional();
        assertThat(exceptional.size(), is(2));
        long lazyCount = exceptional.stream().
                map(e -> e.getMethodName()).
                filter(m -> m.equals(ex1.getMethodName())).
                count();
        assertThat(lazyCount, is(1l));

        long somethingCount = exceptional.stream().
                map(e -> e.getMethodName()).
                filter(m -> m.equals(ex2.getMethodName())).
                count();
        assertThat(somethingCount, is(1l));
    }

    @Test
    public void reset() {
        Invocation ex1 = new Invocation("unstable", 1, "lazy - don't call me", 1);
        Invocation ex2 = new Invocation("does not matter", 2, null, 2);
        this.cut.onNewCall(ex1);
        this.cut.onNewCall(ex2);
        List<InvocationStatistics> recent = this.cut.getRecent();
        assertThat(recent.size(), is(2));
        this.cut.reset();
        recent = this.cut.getExceptional();
        assertTrue(recent.isEmpty());

    }
}
