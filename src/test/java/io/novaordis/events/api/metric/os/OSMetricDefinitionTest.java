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

        fail("return here and understand, possibly expand");

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
    public void parseSourceFileContent_InvalidReading_Linux() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        String output = "I am pretty sure this is not valid source file content we can parse the metric from";

        Property p;

        PreParsedContent previousReading = null;

        //noinspection ConstantConditions
        p = d.parseSourceFileContent(OSType.LINUX, output.getBytes(), previousReading);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertNull(p.getValue());
    }

    @Test
    public void parseSourceFileContent_InvalidReading_Mac() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        String output = "I am pretty sure this is not valid source file content we can parse the metric from";

        Property p;

        PreParsedContent previousReading = null;

        //noinspection ConstantConditions
        p = d.parseCommandOutput(OSType.MAC, output, previousReading);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertNull(p.getValue());
    }

    @Test
    public void parseSourceFileContent_InvalidReading_Windows() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        String output = "I am pretty sure this is not valid source file content we can parse the metric from";

        Property p;

        PreParsedContent previousReading = null;

        //noinspection ConstantConditions
        p = d.parseCommandOutput(OSType.WINDOWS, output, previousReading);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertNull(p.getValue());
    }

    @Test
    public void parseSourceFileContent_Null_Linux() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        Property p;

        PreParsedContent previousReading = null;

        //noinspection ConstantConditions
        p = d.parseCommandOutput(OSType.LINUX, null, previousReading);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertNull(p.getValue());
    }

    @Test
    public void parseSourceFileContent_Null_Mac() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        Property p;

        PreParsedContent previousReading = null;

        //noinspection ConstantConditions
        p = d.parseCommandOutput(OSType.MAC, null, previousReading);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertNull(p.getValue());
    }

    @Test
    public void parseSourceFileContent_Null_Windows() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        Property p;

        PreParsedContent previousReading = null;

        //noinspection ConstantConditions
        p = d.parseCommandOutput(OSType.WINDOWS, null, previousReading);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertNull(p.getValue());
    }

    @Test
    public abstract void parseSourceFileContent_ValidLinuxOutput() throws Exception;

    @Test
    public abstract void parseSourceFileContent_ValidMacOutput() throws Exception;

    @Test
    public abstract void parseSourceFileContent_ValidWindowsOutput() throws Exception;

    // parseLinuxSourceFileContent()/parseMacSourceFileContent()/parseWindowsSourceFileContent() -----------------------

    @Test
    public void parseLinuxSourceFileContent_InvalidReading() throws Exception {

        OSMetricDefinitionBase d = (OSMetricDefinitionBase)getMetricDefinitionToTest();

        if (d.getCommand(OSType.LINUX) == null) {

            //
            // we don't read this metric on Linux, parseLinuxCommandOutput() should not be invoked
            //

            return;
        }

        String output = "invalid Linux command output";

        try {

            d.parseLinuxCommandOutput(output);
            fail("should have thrown exception");
        }
        catch(ParsingException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("failed to match pattern"));
        }
    }

    @Test
    public void parseMacSourceFileContent_InvalidReading() throws Exception {

        OSMetricDefinitionBase d = (OSMetricDefinitionBase)getMetricDefinitionToTest();

        if (d.getCommand(OSType.MAC) == null) {

            //
            // we don't read this metric on Mac, getMacCommand() should not be invoked
            //

            return;
        }

        String output = "invalid Mac command output";

        try {

            d.parseMacCommandOutput(output);
            fail("should have thrown exception");
        }
        catch(ParsingException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("failed to match pattern"));
        }
    }

    @Test
    public void parseWindowsSourceFileContent_InvalidReading() throws Exception {

        OSMetricDefinitionBase d = (OSMetricDefinitionBase)getMetricDefinitionToTest();

        if (d.getCommand(OSType.WINDOWS) == null) {

            //
            // we don't read this metric on Windows, getWindowsCommand() should not be invoked
            //

            return;
        }

        String output = "invalid Windows command output";

        try {

            d.parseWindowsCommandOutput(output);
            fail("should have thrown exception");
        }
        catch(ParsingException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("failed to match pattern"));
        }
    }

    // parseCommandOutput() --------------------------------------------------------------------------------------------

    @Test
    public void parseCommandOutput_InvalidReading_Linux() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        String output = "I am pretty sure this is not valid output we can parse the metric from";

        Property p;

        PreParsedContent previousReading = null;

        //noinspection ConstantConditions
        p = d.parseCommandOutput(OSType.LINUX, output, previousReading);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertNull(p.getValue());
    }

    @Test
    public void parseCommandOutput_InvalidReading_Mac() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        String output = "I am pretty sure this is not valid output we can parse the metric from";

        Property p;

        PreParsedContent previousReading = null;

        //noinspection ConstantConditions
        p = d.parseCommandOutput(OSType.MAC, output, previousReading);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertNull(p.getValue());
    }

    @Test
    public void parseCommandOutput_InvalidReading_Windows() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        String output = "I am pretty sure this is not valid output we can parse the metric from";

        Property p;

        PreParsedContent previousReading = null;

        //noinspection ConstantConditions
        p = d.parseCommandOutput(OSType.WINDOWS, output, previousReading);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertNull(p.getValue());
    }

    @Test
    public void parseCommandOutput_Null_Linux() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        Property p;

        PreParsedContent previousReading = null;

        //noinspection ConstantConditions
        p = d.parseCommandOutput(OSType.LINUX, null, previousReading);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertNull(p.getValue());
    }

    @Test
    public void parseCommandOutput_Null_Mac() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        Property p;

        PreParsedContent previousReading = null;

        //noinspection ConstantConditions
        p = d.parseCommandOutput(OSType.MAC, null, previousReading);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertNull(p.getValue());
    }

    @Test
    public void parseCommandOutput_Null_Windows() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        Property p;

        PreParsedContent previousReading = null;

        //noinspection ConstantConditions
        p = d.parseCommandOutput(OSType.WINDOWS, null, previousReading);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertNull(p.getValue());
    }

    @Test
    public abstract void parseCommandOutput_ValidLinuxOutput() throws Exception;

    @Test
    public abstract void parseCommandOutput_ValidMacOutput() throws Exception;

    @Test
    public abstract void parseCommandOutput_ValidWindowsOutput() throws Exception;

    // parseLinuxCommandOutput()/parseMacCommandOutput()/parseWindowsCommandOutput() -----------------------------------

    @Test
    public void parseLinuxCommandOutput_InvalidReading() throws Exception {

        OSMetricDefinitionBase d = (OSMetricDefinitionBase)getMetricDefinitionToTest();

        if (d.getCommand(OSType.LINUX) == null) {

            //
            // we don't read this metric on Linux, parseLinuxCommandOutput() should not be invoked
            //

            return;
        }

        String output = "invalid Linux command output";

        try {

            d.parseLinuxCommandOutput(output);
            fail("should have thrown exception");
        }
        catch(ParsingException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("failed to match pattern"));
        }
    }

    @Test
    public void parseMacCommandOutput_InvalidReading() throws Exception {

        OSMetricDefinitionBase d = (OSMetricDefinitionBase)getMetricDefinitionToTest();

        if (d.getCommand(OSType.MAC) == null) {

            //
            // we don't read this metric on Mac, getMacCommand() should not be invoked
            //

            return;
        }

        String output = "invalid Mac command output";

        try {

            d.parseMacCommandOutput(output);
            fail("should have thrown exception");
        }
        catch(ParsingException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("failed to match pattern"));
        }
    }

    @Test
    public void parseWindowsCommandOutput_InvalidReading() throws Exception {

        OSMetricDefinitionBase d = (OSMetricDefinitionBase)getMetricDefinitionToTest();

        if (d.getCommand(OSType.WINDOWS) == null) {

            //
            // we don't read this metric on Windows, getWindowsCommand() should not be invoked
            //

            return;
        }

        String output = "invalid Windows command output";

        try {

            d.parseWindowsCommandOutput(output);
            fail("should have thrown exception");
        }
        catch(ParsingException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("failed to match pattern"));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
