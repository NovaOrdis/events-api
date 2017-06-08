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
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricDefinitionTest;
import io.novaordis.events.api.metric.os.mdefs.LocalOS;
import io.novaordis.events.api.metric.os.mdefs.PhysicalMemoryTotal;
import io.novaordis.events.api.metric.os.mdefs.PhysicalMemoryUsed;
import io.novaordis.events.api.parser.ParsingException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        LocalOS localOs = (LocalOS) d.getSource();

        List<Property> measurements = localOs.collectMetrics(Collections.singletonList(d));

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

    // static scope ----------------------------------------------------------------------------------------------------

    @Test
    public void preRefactoring_StaticScopeAfterTwoDifferentClassesAreLoaded() throws Exception {

        PhysicalMemoryTotal pmt = new PhysicalMemoryTotal(new MockOSSource());
        PhysicalMemoryUsed pmu = new PhysicalMemoryUsed(new MockOSSource());

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

    // getCommand() ----------------------------------------------------------------------------------------------------

    @Test
    public void getCommand_Linux() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        try {

            //
            // set the "current" OS to Linux
            //

            OSType.current = OSType.LINUX;

            String s = d.getCommand();
            assertEquals(d.getLinuxCommand(), s);

        }
        finally {

            //
            // restore the "current" system
            //

            OSType.reset();
        }
    }

    @Test
    public void getCommand_Mac() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        try {

            //
            // set the "current" OS to Mac
            //

            OSType.current = OSType.MAC;

            String s = d.getCommand();
            assertEquals(d.getMacCommand(), s);

        }
        finally {

            //
            // restore the "current" system
            //

            OSType.reset();
        }
    }

    @Test
    public void getCommand_Windows() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        try {

            //
            // set the "current" OS to Windows
            //

            OSType.current = OSType.WINDOWS;

            String s = d.getCommand();
            assertEquals(d.getWindowsCommand(), s);

        }
        finally {

            //
            // restore the "current" system
            //

            OSType.reset();
        }
    }

    @Test
    public void getCommand_UnsupportedOS() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        try {

            //
            // set the "current" OS to something that is not supported
            //

            OSType.current = OSType.TEST;

            d.getCommand();

            fail("should have thrown exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("not supported yet"));
        }
        finally {

            //
            // restore the "current" system
            //

            OSType.reset();
        }
    }

    // per-OS parse*CommandOutput() ------------------------------------------------------------------------------------

    @Test
    public void parseLinuxCommandOutput_InvalidReading() throws Exception {

        OSMetricDefinitionBase d = (OSMetricDefinitionBase)getMetricDefinitionToTest();

        if (d.getLinuxCommand() == null) {

            //
            // we don't read this metric on Linux, parseLinuxCommandOutput() should not be invoked
            //

            return;
        }

        String output = "invalid output";

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

        if (d.getMacCommand() == null) {

            //
            // we don't read this metric on Mac, getMacCommand() should not be invoked
            //

            return;
        }

        String output = "invalid output";

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

        if (d.getWindowsCommand() == null) {

            //
            // we don't read this metric on Windows, getWindowsCommand() should not be invoked
            //

            return;
        }

        String output = "invalid output";

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

        try {

            OSType.current = OSType.LINUX;
            p = d.parseCommandOutput(output);
        }
        finally {

            OSType.reset();
        }

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

        try {

            OSType.current = OSType.MAC;
            p = d.parseCommandOutput(output);
        }
        finally {

            OSType.reset();
        }

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

        try {

            OSType.current = OSType.WINDOWS;
            p = d.parseCommandOutput(output);
        }
        finally {

            OSType.reset();
        }

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertNull(p.getValue());
    }

    @Test
    public void parseCommandOutput_Null_Linux() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        Property p;

        try {

            OSType.current = OSType.LINUX;
            p = d.parseCommandOutput(null);
        }
        finally {

            OSType.reset();
        }

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertNull(p.getValue());
    }

    @Test
    public void parseCommandOutput_Null_Mac() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        Property p;

        try {

            OSType.current = OSType.MAC;
            p = d.parseCommandOutput(null);
        }
        finally {

            OSType.reset();
        }

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
        assertNull(p.getValue());
    }

    @Test
    public void parseCommandOutput_Null_Windows() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        Property p;

        try {

            OSType.current = OSType.WINDOWS;
            p = d.parseCommandOutput(null);
        }
        finally {

            OSType.reset();
        }

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

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------


    // Inner classes ---------------------------------------------------------------------------------------------------

}
