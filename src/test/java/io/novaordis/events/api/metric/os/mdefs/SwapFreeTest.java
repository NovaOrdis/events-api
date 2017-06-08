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
import io.novaordis.events.api.measure.MemoryMeasureUnit;
import io.novaordis.events.api.metric.MetricDefinitionParser;
import io.novaordis.events.api.metric.MetricSourceRepositoryImpl;
import io.novaordis.events.api.metric.os.OSMetricDefinitionTest;
import io.novaordis.events.api.metric.os.OSType;
import io.novaordis.events.api.metric.os.LocalOS;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class SwapFreeTest extends OSMetricDefinitionTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void metricDefinition_parse() throws Exception {

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();

        assertTrue(r.isEmpty());

        SwapFree m = (SwapFree)MetricDefinitionParser.parse(r, "SwapFree");

        assertNotNull(m);

        assertEquals(m.getSource(), r.getSources(LocalOS.class).iterator().next());
    }

    // accessors -------------------------------------------------------------------------------------------------------

    @Test
    public void getId() throws Exception {

        SwapFree md = new SwapFree(new LocalOS());
        assertEquals("SwapFree", md.getId());
    }

    @Test
    public void getType() throws Exception {

        SwapFree md = new SwapFree(new LocalOS());
        assertEquals(Long.class, md.getType());
    }

    @Test
    public void getBaseUnit() throws Exception {

        SwapFree md = new SwapFree(new LocalOS());
        assertEquals(MemoryMeasureUnit.BYTE, md.getBaseUnit());
    }

    @Test
    public void getSimpleLabel() throws Exception {

        SwapFree m = new SwapFree(new LocalOS());
        assertEquals("Free Swap", m.getSimpleLabel());
    }

    @Test
    public void getDescription() throws Exception {

        SwapFree m = new SwapFree(new LocalOS());
        assertTrue(m.getDescription().toLowerCase().contains("free"));
        assertTrue(m.getDescription().toLowerCase().contains("swap"));
    }

    @Test
    public void getLinuxCommand() throws Exception {

        String expected = "/usr/bin/top -b -n 1 -p 0";

        SwapFree m = new SwapFree(new LocalOS());
        assertEquals(expected, m.getLinuxCommand());

        try {

            //
            // set the "current" OS to Mac
            //

            OSType.current = OSType.LINUX;

            String s = m.getCommand();
            assertEquals(expected, s);

        }
        finally {

            //
            // restore the "current" system
            //

            OSType.reset();
        }
    }

    @Test
    public void getMacCommand() throws Exception {

        SwapFree m = new SwapFree(new LocalOS());

        assertNull(m.getMacCommand());

        try {

            //
            // set the "current" OS to Mac
            //

            OSType.current = OSType.MAC;

            String s = m.getCommand();
            assertNull(s);

        }
        finally {

            //
            // restore the "current" system
            //

            OSType.reset();
        }
    }

    @Test
    public void getWindowsCommand() throws Exception {

        SwapFree m = new SwapFree(new LocalOS());
        assertNull(m.getWindowsCommand());

        try {

            //
            // set the "current" OS to Mac
            //

            OSType.current = OSType.WINDOWS;

            String s = m.getCommand();
            assertNull(s);

        }
        finally {

            //
            // restore the "current" system
            //

            OSType.reset();
        }
    }

    // parseCommandOutput() --------------------------------------------------------------------------------------------

    //
    // invalid readings are tested in superclass
    //

    @Test
    @Override
    public void parseCommandOutput_ValidLinuxOutput() throws Exception {

        String output =

                "top - 11:10:11 up 0 min,  1 user,  load average: 0.15, 0.04, 0.02\n" +
                        "Tasks:   1 total,   1 running,   0 sleeping,   0 stopped,   0 zombie\n" +
                        "%Cpu(s):  1.1 us,  2.2 sy,  3.3 ni, 4.4 id,  5.5 wa,  6.6 hi,  7.7 si,  8.8 st\n" +
                        "KiB Mem :   999936 total,   735636 free,   117680 used,   146620 buff/cache\n" +
                        "KiB Swap:        0 total,      321 free,        0 used.   715840 avail Mem\n" +
                        "\n" +
                        "   PID USER      PR  NI    VIRT    RES    SHR S %CPU %MEM     TIME+ COMMAND\n" +
                        "  1094 ansible   20   0  157440   1928   1484 R  0.0  0.2   0:00.00 top";

        SwapFree d = getMetricDefinitionToTest();

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

        long expected = 321L * 1024;
        assertEquals(expected, ((Long) p.getValue()).longValue());
    }

    @Test
    @Override
    public void parseCommandOutput_ValidMacOutput() throws Exception {

        //
        // there is no valid mac output
        //
    }

    @Test
    @Override
    public void parseCommandOutput_ValidWindowsOutput() throws Exception {

        //
        // TODO noop for the time being, revisit when implementing Windows support
        //
    }

    @Test
    @Override
    public void parseMacCommandOutput_InvalidReading() throws Exception {

        //
        // TODO noop for the time being, revisit when implementing Windows support
        //
    }

    @Test
    @Override
    public void parseWindowsCommandOutput_InvalidReading() throws Exception {

        //
        // TODO noop for the time being, revisit when implementing Windows support
        //
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected SwapFree getMetricDefinitionToTest() throws Exception {
        return new SwapFree(new LocalOS());
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
