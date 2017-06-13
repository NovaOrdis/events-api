/*
 * Copyright (c) 2017 Nova Ordis LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.novaordis.events.api.metric.os;

import io.novaordis.events.api.event.MockProperty;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricSourceTest;
import io.novaordis.events.api.metric.MockMetricDefinition;
import io.novaordis.events.api.metric.os.mdefs.MockOSMetricDefinition;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.OSAddress;
import io.novaordis.utilities.os.NativeExecutionException;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/7/17
 */
public abstract class OSSourceBaseTest extends MetricSourceTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // collectMetrics() ------------------------------------------------------------------------------------------------

    @Test
    public final void collectMetrics_EmptyMetricDefinitionList() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        List<Property> result = oss.collectMetrics(Collections.emptyList());

        assertTrue(result.isEmpty());
    }

    @Test
    public final void collectMetrics() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        //
        // a command that executes successfully on all supported OSes and returns a non-empty stdout
        //
        String command = "hostname";

        MockOSMetricDefinition md = new MockOSMetricDefinition("metric1", oss.getAddress(), command);

        List<MetricDefinition> mds = Collections.singletonList(md);

        List<Property> result = oss.collectMetrics(mds);

        assertEquals(1, result.size());

        MockProperty p = (MockProperty)result.get(0);

        assertEquals("metric1", p.getName());
    }

    @Test
    public final void collectMetrics_ThreeDefinitions_TwoOfThemShareTheCommand() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        String commandThatWorksOnAllSupportedOSes = "hostname";
        String otherCommandThatWorksOnAllSupportedOSes = "whoami";

        MockOSMetricDefinition md = new MockOSMetricDefinition(
                "mock-metric-1", oss.getAddress(), commandThatWorksOnAllSupportedOSes);

        MockOSMetricDefinition md2 = new MockOSMetricDefinition(
                "mock-metric-2", oss.getAddress(), commandThatWorksOnAllSupportedOSes);

        MockOSMetricDefinition md3 = new MockOSMetricDefinition(
                "mock-metric-3", oss.getAddress(), otherCommandThatWorksOnAllSupportedOSes);

        List<MetricDefinition> mds = Arrays.asList(md, md3, md2);

        List<Property> result = oss.collectMetrics(mds);

        assertEquals(3, result.size());

        MockProperty p = (MockProperty)result.get(0);
        MockProperty p2 = (MockProperty)result.get(1);
        MockProperty p3 = (MockProperty)result.get(2);

        assertEquals("mock-metric-1", p.getName());
        assertEquals("mock-metric-3", p2.getName());
        assertEquals("mock-metric-2", p3.getName());

        //
        // the MockOSMetricDefinition implementation injects the command output into the property it creates, as the
        // property value so we can test consistency, properties generated by the same "command" should have the same
        // value
        //

        String v = (String)p.getValue();
        String v3 = (String)p3.getValue();

        assertEquals(v, v3);

        //
        // insure that only two commands were executed about the source
        //
    }

    @Test
    public final void collectMetrics_InsureCommandsAreExecutedOnceAgainstTheExecutor() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        MockNativeExecutor me = new MockNativeExecutor();

        oss.setNativeExecutor(me);

        String command = "mock-command";
        String command2 = "mock-command-2";
        String command3 = "mock-command-3";

        MockOSMetricDefinition md = new MockOSMetricDefinition("mock-metric", oss.getAddress(), command);
        MockOSMetricDefinition md2 = new MockOSMetricDefinition("mock-metric-2", oss.getAddress(), command2);
        MockOSMetricDefinition md3 = new MockOSMetricDefinition("mock-metric-3", oss.getAddress(), command3);
        MockOSMetricDefinition md4 = new MockOSMetricDefinition("mock-metric-4", oss.getAddress(), command);
        MockOSMetricDefinition md5 = new MockOSMetricDefinition("mock-metric-5", oss.getAddress(), command2);
        MockOSMetricDefinition md6 = new MockOSMetricDefinition("mock-metric-6", oss.getAddress(), command);
        MockOSMetricDefinition md7 = new MockOSMetricDefinition("mock-metric-7", oss.getAddress(), command2);

        // md3/command3 is only requested once
        List<MetricDefinition> mds = Arrays.asList(md, md2, md3, md4, md5, md6, md7, md, md2, md4, md5, md6, md7);

        List<Property> result = oss.collectMetrics(mds);

        assertEquals(13, result.size());

        assertEquals("mock-metric", result.get(0).getName());
        assertEquals("mock-metric-2", result.get(1).getName());
        assertEquals("mock-metric-3", result.get(2).getName());
        assertEquals("mock-metric-4", result.get(3).getName());
        assertEquals("mock-metric-5", result.get(4).getName());
        assertEquals("mock-metric-6", result.get(5).getName());
        assertEquals("mock-metric-7", result.get(6).getName());
        assertEquals("mock-metric", result.get(7).getName());
        assertEquals("mock-metric-2", result.get(8).getName());
        assertEquals("mock-metric-4", result.get(9).getName());
        assertEquals("mock-metric-5", result.get(10).getName());
        assertEquals("mock-metric-6", result.get(11).getName());
        assertEquals("mock-metric-7", result.get(12).getName());

        // make sure only three commands were execute

        List<String> commands = me.getCommandExecutionHistory();
        assertEquals(3, commands.size());
        assertEquals("mock-command", commands.get(0));
        assertEquals("mock-command-2", commands.get(1));
        assertEquals("mock-command-3", commands.get(2));

    }

    // execute() -------------------------------------------------------------------------------------------------------

    @Test
    public final void execute_executorThrowsUncheckedException_SyntheticExecutor() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        MockNativeExecutor me = new MockNativeExecutor();

        me.failWith(new RuntimeException("SYNTHETIC"));

        oss.setNativeExecutor(me);

        String stdout = oss.execute("mock-command");

        assertNull(stdout);
    }

    @Test
    public final void execute_executorThrowsCheckedException_SyntheticExecutor() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        MockNativeExecutor me = new MockNativeExecutor();

        me.failWith(new NativeExecutionException("SYNTHETIC"));

        oss.setNativeExecutor(me);

        String stdout = oss.execute("mock-command");

        assertNull(stdout);
    }

    @Test
    public final void execute_CommandFailsWithNonZeroExitCode_SyntheticExecutor() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        MockNativeExecutor me = new MockNativeExecutor();

        me.failWith(255);

        oss.setNativeExecutor(me);

        String stdout = oss.execute("mock-command");
        assertNull(stdout);
    }

    @Test
    public final void execute_CommandFailsWithNonZeroExitCode_ActualExecutor() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        String stdout = oss.execute("a-command-that-will-surely-fail");
        assertNull(stdout);
    }

    @Test
    public final void execute_CommandDoesNotFailButReturnsNoStdout_SyntheticExecutor() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        MockNativeExecutor me = new MockNativeExecutor();

        me.setStdout(null);
        me.setStderr(null);

        oss.setNativeExecutor(me);

        String stdout = oss.execute("mock-command");
        assertNull(stdout);
    }

    @Test
    public final void execute_CommandDoesNotFailButReturnsNoStdout_ActualExecutor() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        String stdout = oss.execute("true");
        assertNull(stdout);
    }

    @Test
    public final void execute_SyntheticExecutor() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        MockNativeExecutor me = new MockNativeExecutor();

        String stdoutContent = "blah";

        me.setStdout(stdoutContent);
        me.setStderr(null);

        oss.setNativeExecutor(me);

        String stdout = oss.execute("mock-command");

        assertEquals(stdoutContent, stdout);
    }

    /**
     * Will be overridden by the RemoteOSTest, as the execution result is a mock hostname.
     */
    @Test
    public void execute_ActualExecutor() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        //
        // command will execute and will return non-empty string on all supported OSes
        //
        String stdout = oss.execute("hostname");

        assertNotNull(stdout);

        String hostname = InetAddress.getLocalHost().getHostName();
        assertTrue(stdout.startsWith(hostname));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected MockMetricDefinition getCorrespondingMockMetricDefinition(Address metricSourceAddress) {

        if (metricSourceAddress.getClass().isAssignableFrom(OSAddress.class)) {

            fail("we expect an OS address but we got this: " + metricSourceAddress);
        }

        return new MockOSMetricDefinition((OSAddress)metricSourceAddress);
    }

    @Override
    protected abstract OSSourceBase getMetricSourceToTest(String... addresses) throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
