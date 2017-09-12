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

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.event.PropertyFactory;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricDefinitionParser;
import io.novaordis.events.api.metric.MetricDefinitionTest;
import io.novaordis.events.api.metric.os.mdefs.PhysicalMemoryTotal;
import io.novaordis.events.api.metric.os.mdefs.PhysicalMemoryUsed;
import io.novaordis.utilities.address.LocalOSAddress;
import io.novaordis.utilities.os.OSType;
import io.novaordis.utilities.parsing.ParsingException;
import io.novaordis.utilities.parsing.PreParsedContent;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/6/17
 */
public abstract class OSMetricDefinitionTest extends MetricDefinitionTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(OSMetricDefinitionTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    /**
     * These tests will have different results depending on the operating system the test suite is executed on, but
     * they all must pass.
     */
    @Test
    public void collectMetricOnTheLocalSystem() throws Exception {

        MetricDefinition d = getMetricDefinitionToTest();

        LocalOSAddress localOsAddress = (LocalOSAddress)d.getMetricSourceAddress();

        LocalOS localOS = new LocalOS();
        assertEquals(localOS.getAddress(), localOsAddress);

        List<Property> measurements = localOS.collectMetrics(Collections.singletonList(d));

        assertEquals(1, measurements.size());

        Property p = measurements.get(0);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertEquals(d.getType(), p.getType());

        Object value = p.getValue();

        if (value == null) {

            //
            // this is a valid outcome, the metric may not be available on the local system and we're testing
            // it explicitely with DefinitionOfAMetricThatDoesNotExistOnTheLocalSystem; there are other as well
            //

            log.debug(d + " not available on the local system");
        }
        else {

            assertEquals(value.getClass(), d.getType());
        }
    }

    // MetricDefinitionParser.parse() ----------------------------------------------------------------------------------

    @Test
    public void parse() throws Exception {

        OSMetricDefinition osmd = (OSMetricDefinition)getMetricDefinitionToTest();

        String literal = osmd.getClass().getSimpleName();

        PropertyFactory f = new PropertyFactory();

        OSMetricDefinition osmd2 = (OSMetricDefinition)MetricDefinitionParser.parse(f, literal);

        assertEquals(osmd2.getClass(), osmd.getClass());
    }

    @Test
    public void parse_NullRepository() throws Exception {

        OSMetricDefinition osmd = (OSMetricDefinition)getMetricDefinitionToTest();

        String literal = osmd.getClass().getSimpleName();

        PropertyFactory f = new PropertyFactory();

        OSMetricDefinition osmd2 = (OSMetricDefinition)MetricDefinitionParser.parse(f, literal);

        assertEquals(osmd2.getClass(), osmd.getClass());
    }

    // static scope ----------------------------------------------------------------------------------------------------

    @Test
    public void preRefactoring_StaticScopeAfterTwoDifferentClassesAreLoaded() throws Exception {

        PropertyFactory f = new PropertyFactory();

        PhysicalMemoryTotal pmt = new PhysicalMemoryTotal(f, new MockOSSource().getAddress());
        PhysicalMemoryUsed pmu = new PhysicalMemoryUsed(f, new MockOSSource().getAddress());

        assertEquals("Total Physical Memory", pmt.getLabel());
        assertEquals("Used Physical Memory", pmu.getLabel());
    }

    // accessors -------------------------------------------------------------------------------------------------------

    @Test
    public void getId_Generic() throws Exception {

        //
        // For all OS metrics, the ID is conventionally the simple name of the class implementing the metric definition.
        //

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        String id = d.getId();
        String expected = d.getClass().getSimpleName();
        assertEquals(expected, id);
    }

    @Test
    public void getType_NotNull() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        Class c = d.getType();

        assertNotNull(c);
    }

    @Test
    public void getType_KnownType() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        Class c = d.getType();

        if (Integer.class.equals(c) || Long.class.equals(c) || Float.class.equals(c) || Double.class.equals(c))  {

            return;
        }

        fail("invalid type " + c);
    }

    @Test
    public void getSimpleLabel_NotEmpty() throws Exception {

        OSMetricDefinitionBase d = (OSMetricDefinitionBase)getMetricDefinitionToTest();

        String label = d.getSimpleLabel();

        label = label.trim();

        assertFalse(label.isEmpty());
    }

    @Test
    public void getDescription_NotEmpty() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        String description = d.getDescription();

        description = description.trim();

        assertFalse(description.isEmpty());
    }

    // getSourceFile() -------------------------------------------------------------------------------------------------

    @Test
    public void getSourceFile_Generic() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        File linuxSourceFile = d.getSourceFile(OSType.LINUX);

        if (OSType.LINUX.equals(OSType.getCurrent()) && linuxSourceFile != null) {

            assertTrue(linuxSourceFile.isFile());
            assertTrue(linuxSourceFile.canRead());
        }

        File macSourceFile = d.getSourceFile(OSType.MAC);

        if (OSType.MAC.equals(OSType.getCurrent()) && macSourceFile != null) {

            assertTrue(macSourceFile.isFile());
            assertTrue(macSourceFile.canRead());
        }

        File windowsSourceFile = d.getSourceFile(OSType.WINDOWS);

        if (OSType.WINDOWS.equals(OSType.getCurrent()) && windowsSourceFile != null) {

            assertTrue(windowsSourceFile.isFile());
            assertTrue(windowsSourceFile.canRead());
        }
    }

    // parseSourceFileContent() ----------------------------------------------------------------------------------------

    @Test
    public void parseSourceFileContent_InvalidReading() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        List<OSType> osTypes = Arrays.asList(OSType.LINUX, OSType.MAC, OSType.WINDOWS);

        //noinspection Convert2streamapi
        for(OSType osType: osTypes) {

            if (d.getSourceFile(osType) != null) {

                //
                // if we get a null d.getSourceFile(osType), we are not supposed to call parseSourceFileContent(),
                // will throw IllegalStateException
                //


                byte[] c = "I am pretty sure this is not valid source file content we can parse the metric from".
                        getBytes();

                Property p = d.parseSourceFileContent(osType, c);

                assertEquals(d.getId(), p.getName());
                assertEquals(d.getType(), p.getType());
                assertEquals(d.getBaseUnit(), p.getMeasureUnit());
                assertNull(p.getValue());
            }
        }
    }

    @Test
    public void parseSourceFileContent_Null() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        List<OSType> osTypes = Arrays.asList(OSType.LINUX, OSType.MAC, OSType.WINDOWS);

        //noinspection Convert2streamapi
        for(OSType osType: osTypes) {

            Property p = d.parseSourceFileContent(osType, null);

            assertEquals(d.getId(), p.getName());
            assertEquals(d.getType(), p.getType());
            assertEquals(d.getBaseUnit(), p.getMeasureUnit());
            assertNull(p.getValue());
        }
    }

    @Test
    public void parseSourceFileContent_LastReadingTesting() throws Exception {

        OSMetricDefinitionBase d = (OSMetricDefinitionBase)getMetricDefinitionToTest();

        List<OSType> osTypes = Arrays.asList(OSType.LINUX, OSType.MAC, OSType.WINDOWS);

        //noinspection Convert2streamapi
        for(OSType osType: osTypes) {

            if (d.getSourceFile(osType) == null) {

                continue;
            }

            //
            // first reading, there's no history
            //

            PreParsedContent lastReading = d.getLastReading();
            assertNull(lastReading);

            byte[] c = getValidSourceFileContentToTest(osType, 0);

            Property p = d.parseSourceFileContent(osType, c);
            assertNotNull(p);
            assertNotNull(p.getValue());

            PreParsedContent lastReading2 = d.getLastReading();
            assertNotNull(lastReading2);

            //
            // second reading there's history
            //

            byte[] c2 = getValidSourceFileContentToTest(osType, 1);

            Property p2 = d.parseSourceFileContent(osType, c2);
            assertNotNull(p2);
            assertNotNull(p2.getValue());

            PreParsedContent lastReading3 = d.getLastReading();
            assertNotNull(lastReading3);

            assertFalse(lastReading3.equals(lastReading2));

            //
            // third reading there's history
            //

            byte[] c3 = getValidSourceFileContentToTest(osType, 2);

            Property p3 = d.parseSourceFileContent(osType, c3);
            assertNotNull(p3);
            assertNotNull(p3.getValue());

            PreParsedContent lastReading4 = d.getLastReading();
            assertNotNull(lastReading4);

            assertFalse(lastReading4.equals(lastReading2));
            assertFalse(lastReading4.equals(lastReading3));
        }
    }

    @Test
    public abstract void parseSourceFileContent_LINUX_ValidContent() throws Exception;

    @Test
    public abstract void parseSourceFileContent_MAC_ValidContent() throws Exception;

    @Test
    public abstract void parseSourceFileContent_WINDOWS_ValidContent() throws Exception;

    // protected parseSourceFileContent() ------------------------------------------------------------------------------

    @Test
    public void parseSourceFileContent_protected_NotSupposedToCall() throws Exception {

        OSMetricDefinitionBase d = (OSMetricDefinitionBase)getMetricDefinitionToTest();

        List<OSType> osTypes = Arrays.asList(OSType.LINUX, OSType.MAC, OSType.WINDOWS);

        //noinspection Convert2streamapi
        for(OSType osType: osTypes) {

            if (d.getSourceFile(osType) == null) {

                //
                // if we get a null d.getSourceFile(osType), we are not supposed to call parseSourceFileContent(),
                // will throw IllegalStateException, so we test that
                //

                byte[] c = "does not matter".getBytes();

                try {


                    d.parseSourceFileContent(osType, c, null);
                    fail("should have thrown exception");
                }
                catch(IllegalStateException e) {

                    String msg = e.getMessage();
                    assertTrue(msg.contains("cannot be extracted from a file on"));
                    assertTrue(msg.contains(osType.toString()));
                }
            }
        }
    }

    @Test
    public void parseSourceFileContent_protected_InvalidReading() throws Exception {

        OSMetricDefinitionBase d = (OSMetricDefinitionBase)getMetricDefinitionToTest();

        List<OSType> osTypes = Arrays.asList(OSType.LINUX, OSType.MAC, OSType.WINDOWS);

        //noinspection Convert2streamapi
        for(OSType osType: osTypes) {

            if (d.getSourceFile(osType) != null) {

                //
                // if we get a null d.getSourceFile(osType), we are not supposed to call parseSourceFileContent(), will
                // throw IllegalStateException
                //

                byte[] b = "invalid source file content".getBytes();

                try {

                    d.parseSourceFileContent(osType, b, null);
                    fail("should have thrown exception");
                }
                catch(ParsingException e) {

                    String msg = e.getMessage();
                    assertNotNull(msg);
                }
            }
        }
    }

    @Test
    public void parseSourceFileContent_protected_InvalidPreviousReading() throws Exception {

        OSMetricDefinitionBase d = (OSMetricDefinitionBase)getMetricDefinitionToTest();

        List<OSType> osTypes = Arrays.asList(OSType.LINUX, OSType.MAC, OSType.WINDOWS);

        //noinspection Convert2streamapi
        for(OSType osType: osTypes) {

            if (d.getSourceFile(osType) == null) {

                //
                // if we get a null d.getSourceFile(osType), we are not supposed to call parseSourceFileContent(), will
                // throw IllegalStateException
                //

                continue;
            }

            byte[] content = getValidSourceFileContentToTest(osType, 0);

            PreParsedContent invalidPreviousReading = new MockPreParsedContent();

            try {

                d.parseSourceFileContent(osType, content, invalidPreviousReading);
                fail("should have thrown exception");
            }
            catch(IllegalArgumentException e) {

                String msg = e.getMessage();
                assertTrue(msg.matches(".*not a .* instance.*"));
            }
        }
    }

    // parseCommandOutput() --------------------------------------------------------------------------------------------

    @Test
    public void parseCommandOutput_InvalidReading() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        List<OSType> osTypes = Arrays.asList(OSType.LINUX, OSType.MAC, OSType.WINDOWS);

        //noinspection Convert2streamapi
        for(OSType osType: osTypes) {

            if (d.getCommand(osType) != null) {

                //
                // if we get a null d.getCommand(osType), we are not supposed to call parseCommandOutput(), will throw
                // IllegalStateException
                //

                String output = "I am pretty sure this is not valid output we can parse the metric from";

                Property p = d.parseCommandOutput(osType, output);

                assertEquals(d.getId(), p.getName());
                assertEquals(d.getType(), p.getType());
                assertEquals(d.getBaseUnit(), p.getMeasureUnit());
                assertNull(p.getValue());
            }
        }
    }

    @Test
    public void parseCommandOutput_Null() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        List<OSType> osTypes = Arrays.asList(OSType.LINUX, OSType.MAC, OSType.WINDOWS);

        //noinspection Convert2streamapi
        for(OSType osType: osTypes) {

            Property p = d.parseCommandOutput(osType, null);

            assertEquals(d.getId(), p.getName());
            assertEquals(d.getType(), p.getType());
            assertEquals(d.getBaseUnit(), p.getMeasureUnit());
            assertNull(p.getValue());
        }
    }

    @Test
    public abstract void parseCommandOutput_LINUX_ValidOutput() throws Exception;

    @Test
    public abstract void parseCommandOutput_MAC_ValidOutput() throws Exception;

    @Test
    public abstract void parseCommandOutput_WINDOWS_ValidOutput() throws Exception;

    // protected parseCommandOutput() ------------------------------------------------------------------------------

    @Test
    public void parseCommandOutput_protected_NotSupposedToCall() throws Exception {

        OSMetricDefinitionBase d = (OSMetricDefinitionBase)getMetricDefinitionToTest();

        List<OSType> osTypes = Arrays.asList(OSType.LINUX, OSType.MAC, OSType.WINDOWS);

        //noinspection Convert2streamapi
        for(OSType osType: osTypes) {

            if (d.getCommand(osType) == null) {

                //
                // if we get a null d.getCommand(osType), we are not supposed to call parseCommandOutput(), will throw
                // IllegalStateException, so we test that
                //

                String s = "does not matter";

                try {


                    d.parseCommandOutput(osType, s, null);
                    fail("should have thrown exception");
                }
                catch(IllegalStateException e) {

                    String msg = e.getMessage();
                    assertTrue(msg.contains("cannot be extracted from a command output on"));
                    assertTrue(msg.contains(osType.toString()));
                }
            }
        }
    }

    @Test
    public void parseCommandOutput_protected_InvalidReading() throws Exception {

        OSMetricDefinitionBase d = (OSMetricDefinitionBase)getMetricDefinitionToTest();

        List<OSType> osTypes = Arrays.asList(OSType.LINUX, OSType.MAC, OSType.WINDOWS);

        //noinspection Convert2streamapi
        for(OSType osType: osTypes) {

            if (d.getCommand(osType) != null) {

                //
                // if we get a null d.getCommand(osType), we are not supposed to call parseCommandOutput(), will throw
                // IllegalStateException
                //

                String output = "invalid command output";

                try {

                    d.parseCommandOutput(osType, output, null);
                    fail("should have thrown exception");
                }
                catch(ParsingException e) {

                    String msg = e.getMessage();
                    assertTrue(msg.contains("failed to match pattern"));
                }
            }
        }
    }

    @Test
    public void parseCommandOutput_protected_InvalidPreviousReading() throws Exception {

        OSMetricDefinitionBase d = (OSMetricDefinitionBase)getMetricDefinitionToTest();

        List<OSType> osTypes = Arrays.asList(OSType.LINUX, OSType.MAC, OSType.WINDOWS);

        //noinspection Convert2streamapi
        for(OSType osType: osTypes) {

            if (d.getCommand(osType) == null) {

                continue;

                //
                // if we get a null d.getCommand(osType), we are not supposed to call parseCommandOutput(), will throw
                // IllegalStateException
                //
            }


            PreParsedContent invalidPreviousReading = new MockPreParsedContent();

            String s = getValidCommandOutputToTest(osType, 0);

            //
            // for the time being we're not using the previous reading with command outputs, so nothing should happen
            //

            InternalMetricReadingContainer c = d.parseCommandOutput(osType, s, invalidPreviousReading);
            assertNull(c.getPreParsedContent());
            assertNotNull(c.getPropertyValue());
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * @param seed - different seeds should produce different values.
     *
     * May return null if there is no valid content for this OS.
     *
     * The base class returns a value that works in most of situations. If more specialized behavior is needed,
     * override.
     */
    protected byte[] getValidSourceFileContentToTest(OSType osType, int seed) throws Exception {

        if (OSType.LINUX.equals(osType)) {

            File f = new File(System.getProperty("basedir"),
                    "src/test/resources/data/metric/proc-stat-reading-" + seed + ".txt");

            assertTrue(f.isFile());

            return Files.readAllBytes(f.toPath());
        }

        return null;
    }

    /**
     * @param seed - different seeds should produce different values.
     *
     * May return null if there is no valid command for this OS.
     *
     * The base class returns a value that works in most of situations. If more specialized behavior is needed,
     * override.
     */
    protected String getValidCommandOutputToTest(OSType osType, int seed) throws Exception {

        if (OSType.LINUX.equals(osType)) {

            return
                    "top - 13:35:46 up 63 days,  7:50,  1 user,  load average: 0.02, 0.05, 0.05\n" +
                            "Tasks:  84 total,   1 running,  83 sleeping,   0 stopped,   0 zombie\n" +
                            "%Cpu(s):  0.0 us,  0.3 sy,  0.0 ni, 99.7 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st\n" +
                            "KiB Mem :  1016488 total,   142512 free,    76984 used,   796992 buff/cache\n" +
                            "KiB Swap:        0 total,        0 free,        0 used.   709812 avail Mem";

        }
        else if (OSType.MAC.equals(osType)) {

            return
                    "Processes: 244 total, 2 running, 12 stuck, 230 sleeping, 1509 threads                                                                                                                                                                          13:35:02\n" +
                            "Load Avg: 2.06, 2.10, 2.00  CPU usage: 5.0% user, 3.41% sys, 91.58% idle   SharedLibs: 174M resident, 0B data, 24M linkedit. MemRegions: 61258 total, 5831M resident, 132M private, 1641M shared. PhysMem: 12G used (1406M wired), 3913M unused.\n" +
                            "VM: 637G vsize, 1352M framework vsize, 0(0) swapins, 0(0) swapouts. Networks: packets: 10427436/13G in, 4953154/376M out. Disks: 269280/7493M read, 456064/31G written.";
        }
        else {

            return null;
        }
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
