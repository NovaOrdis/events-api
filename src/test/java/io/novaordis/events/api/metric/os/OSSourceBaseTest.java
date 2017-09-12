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
import io.novaordis.events.api.event.PropertyFactory;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricSourceTest;
import io.novaordis.events.api.metric.os.mdefs.CommandBasedMockOSMetricDefinition;
import io.novaordis.events.api.metric.os.mdefs.FileBasedMockOSMetricDefinition;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.OSAddress;
import io.novaordis.utilities.os.NativeExecutionException;
import io.novaordis.utilities.os.NativeExecutionResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.InetAddress;
import java.nio.file.Files;
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

    protected File scratchDirectory;

    protected File baseDirectory;

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Before
    public void before() throws Exception {

        String projectBaseDirName = System.getProperty("basedir");
        scratchDirectory = new File(projectBaseDirName, "target/test-scratch");
        assertTrue(scratchDirectory.isDirectory());

        baseDirectory = new File(System.getProperty("basedir"));
        assertTrue(baseDirectory.isDirectory());
    }

    @After
    public void after() throws Exception {

        //
        // scratch directory cleanup
        //

        assertTrue(io.novaordis.utilities.Files.rmdir(scratchDirectory, false));

    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // collectMetrics() ------------------------------------------------------------------------------------------------

    @Test
    public final void collectMetrics_EmptyMetricDefinitionList() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        List<Property> result = oss.collectMetrics(Collections.emptyList());

        assertTrue(result.isEmpty());
    }

    @Test
    public final void collectMetrics_ValueExtractedFromCommand() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        //
        // a command that executes successfully on all supported OSes and returns a non-empty stdout
        //
        String command = "hostname";

        PropertyFactory f = new PropertyFactory();

        CommandBasedMockOSMetricDefinition md = new CommandBasedMockOSMetricDefinition("metric1", f, oss.getAddress(), command);

        List<MetricDefinition> mds = Collections.singletonList(md);

        List<Property> result = oss.collectMetrics(mds);

        assertEquals(1, result.size());

        MockProperty p = (MockProperty)result.get(0);

        assertEquals("metric1", p.getName());
    }

    @Test
    public final void collectMetrics_ValueExtractedFromCommand_ThreeDefinitions_TwoOfThemShareTheCommand()
            throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        String commandThatWorksOnAllSupportedOSes = "hostname";
        String otherCommandThatWorksOnAllSupportedOSes = "whoami";

        PropertyFactory f = new PropertyFactory();

        CommandBasedMockOSMetricDefinition md = new CommandBasedMockOSMetricDefinition(
                "mock-metric-1", f, oss.getAddress(), commandThatWorksOnAllSupportedOSes);

        CommandBasedMockOSMetricDefinition md2 = new CommandBasedMockOSMetricDefinition(
                "mock-metric-2", f, oss.getAddress(), commandThatWorksOnAllSupportedOSes);

        CommandBasedMockOSMetricDefinition md3 = new CommandBasedMockOSMetricDefinition(
                "mock-metric-3", f, oss.getAddress(), otherCommandThatWorksOnAllSupportedOSes);

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
        // the CommandBasedMockOSMetricDefinition implementation injects the command output into the property it
        // creates, as the/ property value so we can test consistency, properties generated by the same "command"
        // should have the same value
        //

        String v = (String)p.getValue();
        String v3 = (String)p3.getValue();

        assertEquals(v, v3);

        //
        // insure that only two commands were executed by the source
        //
    }

    @Test
    public final void collectMetrics_ValueExtractedFromCommand_InsureCommandsAreExecutedOnceAgainstTheExecutor()
            throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        MockNativeExecutor me = getCorrespondingMockNativeExecutor();

        oss.setNativeExecutor(me);

        PropertyFactory f = new PropertyFactory();

        String command = "mock-command";
        String command2 = "mock-command-2";
        String command3 = "mock-command-3";

        CommandBasedMockOSMetricDefinition md = new CommandBasedMockOSMetricDefinition("mock-metric", f, oss.getAddress(), command);
        CommandBasedMockOSMetricDefinition md2 = new CommandBasedMockOSMetricDefinition("mock-metric-2", f, oss.getAddress(), command2);
        CommandBasedMockOSMetricDefinition md3 = new CommandBasedMockOSMetricDefinition("mock-metric-3", f, oss.getAddress(), command3);
        CommandBasedMockOSMetricDefinition md4 = new CommandBasedMockOSMetricDefinition("mock-metric-4", f, oss.getAddress(), command);
        CommandBasedMockOSMetricDefinition md5 = new CommandBasedMockOSMetricDefinition("mock-metric-5", f, oss.getAddress(), command2);
        CommandBasedMockOSMetricDefinition md6 = new CommandBasedMockOSMetricDefinition("mock-metric-6", f, oss.getAddress(), command);
        CommandBasedMockOSMetricDefinition md7 = new CommandBasedMockOSMetricDefinition("mock-metric-7", f, oss.getAddress(), command2);

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

    @Test
    public final void collectMetrics_ValueExtractedFromFile() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        PropertyFactory f = new PropertyFactory();

        File file = new File(scratchDirectory, "mock-source-file");
        Files.write(file.toPath(), "A".getBytes());
        assertTrue(file.isFile());

        FileBasedMockOSMetricDefinition md =
                new FileBasedMockOSMetricDefinition("test-metric", f, oss.getAddress(), file);

        List<MetricDefinition> mds = Collections.singletonList(md);

        List<Property> result = oss.collectMetrics(mds);

        assertEquals(1, result.size());

        MockProperty p = (MockProperty)result.get(0);

        assertEquals("test-metric", p.getName());
        assertEquals("A", p.getValue());
    }

    @Test
    public final void collectMetrics_ValuesExtractedFromCommandsAndFiles() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        MockNativeExecutor me = getCorrespondingMockNativeExecutor();

        oss.setNativeExecutor(me);

        PropertyFactory f = new PropertyFactory();

        File file = new File(scratchDirectory, "mock-source-file");
        Files.write(file.toPath(), "A".getBytes());
        assertTrue(file.isFile());

        File file2 = new File(scratchDirectory, "mock-source-file-2");
        Files.write(file2.toPath(), "B".getBytes());
        assertTrue(file2.isFile());

        //
        // metrics share files, which are supposed to be read once
        //

        FileBasedMockOSMetricDefinition md =
                new FileBasedMockOSMetricDefinition("test-metric", f, oss.getAddress(), file);

        FileBasedMockOSMetricDefinition md2 =
                new FileBasedMockOSMetricDefinition("test-metric-2", f, oss.getAddress(), file2);

        FileBasedMockOSMetricDefinition md3 =
                new FileBasedMockOSMetricDefinition("test-metric-3", f, oss.getAddress(), file);

        FileBasedMockOSMetricDefinition md4 =
                new FileBasedMockOSMetricDefinition("test-metric-4", f, oss.getAddress(), file2);

        String command = "mock-command-1";
        me.putNativeExecutionResult(command, new NativeExecutionResult(0, "C", "", false, false));

        String command2 = "mock-command-2";
        me.putNativeExecutionResult(command2, new NativeExecutionResult(0, "D", "", false, false));

        //
        // metrics share commands, which are supposed to be executed once
        //

        CommandBasedMockOSMetricDefinition md5 =
                new CommandBasedMockOSMetricDefinition("test-metric-5", f, oss.getAddress(), command);

        CommandBasedMockOSMetricDefinition md6 =
                new CommandBasedMockOSMetricDefinition("test-metric-6", f, oss.getAddress(), command2);

        CommandBasedMockOSMetricDefinition md7 =
                new CommandBasedMockOSMetricDefinition("test-metric-7", f, oss.getAddress(), command);

        CommandBasedMockOSMetricDefinition md8 =
                new CommandBasedMockOSMetricDefinition("test-metric-8", f, oss.getAddress(), command2);

        List<MetricDefinition> mds = Arrays.asList(md, md5, md2, md6, md3, md7, md4, md8);

        List<Property> result = oss.collectMetrics(mds);

        assertEquals(8, result.size());

        MockProperty p = (MockProperty)result.get(0);
        assertEquals("test-metric", p.getName());
        assertEquals("A", p.getValue());

        MockProperty p2 = (MockProperty)result.get(1);
        assertEquals("test-metric-5", p2.getName());
        assertEquals("C", p2.getValue());

        MockProperty p3 = (MockProperty)result.get(2);
        assertEquals("test-metric-2", p3.getName());
        assertEquals("B", p3.getValue());

        MockProperty p4 = (MockProperty)result.get(3);
        assertEquals("test-metric-6", p4.getName());
        assertEquals("D", p4.getValue());

        MockProperty p5 = (MockProperty)result.get(4);
        assertEquals("test-metric-3", p5.getName());
        assertEquals("A", p5.getValue());

        MockProperty p6 = (MockProperty)result.get(5);
        assertEquals("test-metric-7", p6.getName());
        assertEquals("C", p6.getValue());

        MockProperty p7 = (MockProperty)result.get(6);
        assertEquals("test-metric-4", p7.getName());
        assertEquals("B", p7.getValue());

        MockProperty p8 = (MockProperty)result.get(7);
        assertEquals("test-metric-8", p8.getName());
        assertEquals("D", p8.getValue());
    }

    // execute() -------------------------------------------------------------------------------------------------------

    @Test
    public final void execute_executorThrowsUncheckedException_SyntheticExecutor() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        MockNativeExecutor me = getCorrespondingMockNativeExecutor();

        me.failWith(new RuntimeException("SYNTHETIC"));

        oss.setNativeExecutor(me);

        String stdout = oss.execute("mock-command");

        assertNull(stdout);
    }

    @Test
    public final void execute_executorThrowsCheckedException_SyntheticExecutor() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        MockNativeExecutor me = getCorrespondingMockNativeExecutor();

        me.failWith(new NativeExecutionException("SYNTHETIC"));

        oss.setNativeExecutor(me);

        String stdout = oss.execute("mock-command");

        assertNull(stdout);
    }

    @Test
    public final void execute_CommandFailsWithNonZeroExitCode_SyntheticExecutor() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        MockNativeExecutor me = getCorrespondingMockNativeExecutor();

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

        MockNativeExecutor me = getCorrespondingMockNativeExecutor();

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

        MockNativeExecutor me = getCorrespondingMockNativeExecutor();

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

    // read() ----------------------------------------------------------------------------------------------------------

    @Test
    public void read_Null() throws Exception {

        OSSourceBase ms = getMetricSourceToTest();

        assertNull(ms.read(null));
    }

    @Test
    public void read_NonExistentFile() throws Exception {

        OSSourceBase ms = getMetricSourceToTest();

        File f = new File("/I/am/pretty/sure/there/is/no/such/file.txt");

        assertNull(ms.read(f));
    }

    @Test
    public void read() throws Exception {

        OSSourceBase ms = getMetricSourceToTest();

        File f = new File(baseDirectory, "src/test/resources/data/metric/proc-stat-reading-0.txt");
        assertTrue(f.isFile());

        byte[] content = ms.read(f);

        byte[] content2 = Files.readAllBytes(f.toPath());

        assertEquals(new String(content), new String(content2));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected abstract OSSourceBase getMetricSourceToTest(String... addresses) throws Exception;

    @Override
    protected CommandBasedMockOSMetricDefinition getCorrespondingMockMetricDefinition(Address metricSourceAddress) {

        if (metricSourceAddress.getClass().isAssignableFrom(OSAddress.class)) {

            fail("we expect an OS address but we got this: " + metricSourceAddress);
        }

        PropertyFactory f = new PropertyFactory();

        return new CommandBasedMockOSMetricDefinition(f, (OSAddress)metricSourceAddress);
    }

    /**
     * Sub-classes must override to provide a more specialized executor.
     */
    protected MockNativeExecutor getCorrespondingMockNativeExecutor() throws Exception {

        return new MockNativeExecutor();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
