/*
 * Copyright (c) 2016 Nova Ordis LLC
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

package io.novaordis.events.api.metric.os.mdefs;

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.event.PropertyFactory;
import io.novaordis.events.api.measure.MemoryMeasureUnit;
import io.novaordis.events.api.metric.os.OSMetricDefinition;
import io.novaordis.events.api.metric.os.OSMetricDefinitionTest;
import io.novaordis.utilities.address.LocalOSAddress;
import io.novaordis.utilities.os.OSType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class SwapUsedTest extends OSMetricDefinitionTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // accessors -------------------------------------------------------------------------------------------------------

    @Test
    public void getId() throws Exception {

        SwapUsed md = new SwapUsed(new PropertyFactory(), new LocalOSAddress());
        assertEquals("SwapUsed", md.getId());
    }

    @Test
    public void getType() throws Exception {

        SwapUsed md = new SwapUsed(new PropertyFactory(), new LocalOSAddress());
        assertEquals(Long.class, md.getType());
    }

    @Test
    public void getBaseUnit() throws Exception {

        SwapUsed md = new SwapUsed(new PropertyFactory(), new LocalOSAddress());
        assertEquals(MemoryMeasureUnit.BYTE, md.getBaseUnit());
    }

    @Test
    public void getSimpleLabel() throws Exception {

        SwapUsed m = new SwapUsed(new PropertyFactory(), new LocalOSAddress());
        assertEquals("Used Swap", m.getSimpleLabel());
    }

    @Test
    public void getDescription() throws Exception {

        SwapUsed m = new SwapUsed(new PropertyFactory(), new LocalOSAddress());
        assertTrue(m.getDescription().toLowerCase().contains("used"));
        assertTrue(m.getDescription().toLowerCase().contains("swap"));
    }

    @Test
    public void getLinuxCommand() throws Exception {

        String expected = "/usr/bin/top -b -n 1 -p 0";

        SwapUsed m = new SwapUsed(new PropertyFactory(), new LocalOSAddress());
        assertEquals(expected, m.getCommand(OSType.LINUX));
    }

    @Test
    public void getMacCommand() throws Exception {

        SwapUsed m = new SwapUsed(new PropertyFactory(), new LocalOSAddress());
        assertNull(m.getCommand(OSType.MAC));
    }

    @Test
    public void getWindowsCommand() throws Exception {

        SwapUsed m = new SwapUsed(new PropertyFactory(), new LocalOSAddress());
        assertNull(m.getCommand(OSType.WINDOWS));
    }

    // getSourceFile() -------------------------------------------------------------------------------------------------

    @Test
    public void getSourceFile() throws Exception {

        SwapUsed m = getMetricDefinitionToTest();
        assertNull(m.getSourceFile(OSType.LINUX));
        assertNull(m.getSourceFile(OSType.MAC));
        assertNull(m.getSourceFile(OSType.WINDOWS));
    }

    // parseSourceFileContent() ----------------------------------------------------------------------------------------

    @Test
    @Override
    public void parseSourceFileContent_LINUX_ValidContent() throws Exception {

        OSMetricDefinition m = getMetricDefinitionToTest();

        //
        // no source file for this OSType yet, test is a noop
        //

        assertNull(m.getSourceFile(OSType.LINUX));
    }

    @Test
    @Override
    public void parseSourceFileContent_MAC_ValidContent() throws Exception {

        OSMetricDefinition m = getMetricDefinitionToTest();

        //
        // no source file for this OSType yet, test is a noop
        //

        assertNull(m.getSourceFile(OSType.MAC));
    }

    @Test
    @Override
    public void parseSourceFileContent_WINDOWS_ValidContent() throws Exception {

        OSMetricDefinition m = getMetricDefinitionToTest();

        //
        // no source file for this OSType yet, test is a noop
        //

        assertNull(m.getSourceFile(OSType.WINDOWS));
    }

    // parseCommandOutput() --------------------------------------------------------------------------------------------

    //
    // invalid readings are tested in superclass
    //

    @Test
    @Override
    public void parseCommandOutput_LINUX_ValidOutput() throws Exception {

        String output =

                "top - 11:10:11 up 0 min,  1 user,  load average: 0.15, 0.04, 0.02\n" +
                        "Tasks:   1 total,   1 running,   0 sleeping,   0 stopped,   0 zombie\n" +
                        "%Cpu(s):  1.1 us,  2.2 sy,  3.3 ni, 4.4 id,  5.5 wa,  6.6 hi,  7.7 si,  8.8 st\n" +
                        "KiB Mem :   999936 total,   735636 free,   117680 used,   146620 buff/cache\n" +
                        "KiB Swap:        0 total,      321 free,        5 used.   715840 avail Mem\n" +
                        "\n" +
                        "   PID USER      PR  NI    VIRT    RES    SHR S %CPU %MEM     TIME+ COMMAND\n" +
                        "  1094 ansible   20   0  157440   1928   1484 R  0.0  0.2   0:00.00 top";

        SwapUsed d = getMetricDefinitionToTest();

        Property p = d.parseCommandOutput(OSType.LINUX, output);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());

        long expected = 5L * 1024;
        assertEquals(expected, ((Long) p.getValue()).longValue());
    }

    @Test
    @Override
    public void parseCommandOutput_MAC_ValidOutput() throws Exception {

        //
        // there is no valid mac output
        //
    }

    @Test
    @Override
    public void parseCommandOutput_WINDOWS_ValidOutput() throws Exception {

        //
        // TODO noop for the time being, revisit when implementing Windows support
        //
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected SwapUsed getMetricDefinitionToTest() throws Exception {

        return new SwapUsed(new PropertyFactory(), new LocalOSAddress());
    }

    @Override
    protected byte[] getValidSourceFileContentToTest(OSType osType, int seed) throws Exception {

        // no source file for this type of metric
        return null;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
