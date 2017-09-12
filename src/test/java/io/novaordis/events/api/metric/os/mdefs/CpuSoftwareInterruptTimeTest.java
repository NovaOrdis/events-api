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
import io.novaordis.events.api.measure.Percentage;
import io.novaordis.events.api.metric.os.InternalMetricReadingContainer;
import io.novaordis.events.api.metric.os.OSMetricDefinition;
import io.novaordis.events.api.metric.os.OSMetricDefinitionTest;
import io.novaordis.linux.ProcStat;
import io.novaordis.utilities.address.LocalOSAddress;
import io.novaordis.utilities.os.OSType;
import io.novaordis.utilities.parsing.PreParsedContent;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class CpuSoftwareInterruptTimeTest extends OSMetricDefinitionTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // accessors -------------------------------------------------------------------------------------------------------

    @Test
    public void getId() throws Exception {

        CpuSoftwareInterruptTime md = new CpuSoftwareInterruptTime(new PropertyFactory(), new LocalOSAddress());
        assertEquals("CpuSoftwareInterruptTime", md.getId());
    }

    @Test
    public void getType() throws Exception {

        CpuSoftwareInterruptTime md = new CpuSoftwareInterruptTime(new PropertyFactory(), new LocalOSAddress());
        assertEquals(Float.class, md.getType());
    }

    @Test
    public void getBaseUnit() throws Exception {

        CpuSoftwareInterruptTime md = new CpuSoftwareInterruptTime(new PropertyFactory(), new LocalOSAddress());
        assertEquals(Percentage.getInstance(), md.getBaseUnit());
    }

    @Test
    public void getSimpleLabel() throws Exception {

        CpuSoftwareInterruptTime m = new CpuSoftwareInterruptTime(new PropertyFactory(), new LocalOSAddress());
        assertEquals("CPU Software Interrupt Time", m.getSimpleLabel());
    }

    @Test
    public void getDescription() throws Exception {

        CpuSoftwareInterruptTime m = new CpuSoftwareInterruptTime(new PropertyFactory(), new LocalOSAddress());
        assertTrue(m.getDescription().toLowerCase().contains("cpu"));
        assertTrue(m.getDescription().toLowerCase().contains("time"));
        assertTrue(m.getDescription().toLowerCase().contains("percentage"));
        assertTrue(m.getDescription().toLowerCase().contains("software"));
        assertTrue(m.getDescription().toLowerCase().contains("interrupt"));
    }

    @Test
    public void getLinuxCommand() throws Exception {

        String expected = "/usr/bin/top -b -n 1 -p 0";

        CpuSoftwareInterruptTime m = new CpuSoftwareInterruptTime(new PropertyFactory(), new LocalOSAddress());
        assertEquals(expected, m.getCommand(OSType.LINUX));
    }

    @Test
    public void getMacCommand() throws Exception {

        CpuSoftwareInterruptTime m = new CpuSoftwareInterruptTime(new PropertyFactory(), new LocalOSAddress());
        assertNull(m.getCommand(OSType.MAC));
    }

    @Test
    public void getWindowsCommand() throws Exception {

        CpuSoftwareInterruptTime m = new CpuSoftwareInterruptTime(new PropertyFactory(), new LocalOSAddress());
        assertNull(m.getCommand(OSType.WINDOWS));
    }

    // getSourceFile() -------------------------------------------------------------------------------------------------

    @Test
    public void getSourceFile() throws Exception {

        CpuSoftwareInterruptTime m = getMetricDefinitionToTest();
        assertEquals(new File("/proc/stat"), m.getSourceFile(OSType.LINUX));
        assertNull(m.getSourceFile(OSType.MAC));
        assertNull(m.getSourceFile(OSType.WINDOWS));
    }

    // parseSourceFileContent() ----------------------------------------------------------------------------------------

    @Test
    @Override
    public void parseSourceFileContent_ValidLinuxContent() throws Exception {

        CpuSoftwareInterruptTime m = getMetricDefinitionToTest();

        assertNotNull(m.getSourceFile(OSType.LINUX));

        // simulate the first reading, valid content

        String firstFileContentReading =
                "cpu  1 2 3 4 5 6 7 8 9 10\n" +
                        "cpu0 1 2 3 4 5 6 7 8 9 10\n" +
                        "intr 5784556411 57 10 0 0 1010\n" +
                        "ctxt 9216607429\n" +
                        "btime 1499371654\n" +
                        "processes 8116472\n" +
                        "procs_running 1\n" +
                        "procs_blocked 0\n" +
                        "softirq 3995572696 7 1762568953 35455 456569428 0 0 12771 767863139 0 1008522943\n";


        PreParsedContent previousReading = null;

        //noinspection ConstantConditions
        InternalMetricReadingContainer r =
                m.parseLinuxSourceFileContent(firstFileContentReading.getBytes(), previousReading);

        Float f = (Float)r.getPropertyValue();

        //
        // current reading becomes the previous reading
        //

        previousReading = r.getPreParsedContent();
        assertNotNull(previousReading);
        assertEquals(55L, ((ProcStat)previousReading).getCumulativeCPUStatistics().getTotalTime());

        //
        // the statistics are calculated since the beginning
        //

        assertEquals(7f / 55, f.floatValue(), 0.00001);

        // simulate the second reading, valid content

        String secondFileContentReading =
                "cpu  11 22 33 44 55 66 77 88 99 110\n" +
                        "cpu0 11 22 33 44 55 66 77 88 99 110\n" +
                        "intr 5784556411 57 10 0 0 1010\n" +
                        "ctxt 9216607429\n" +
                        "btime 1499371654\n" +
                        "processes 8116472\n" +
                        "procs_running 1\n" +
                        "procs_blocked 0\n" +
                        "softirq 3995572696 7 1762568953 35455 456569428 0 0 12771 767863139 0 1008522943\n";

        InternalMetricReadingContainer r2 =
                m.parseLinuxSourceFileContent(secondFileContentReading.getBytes(), previousReading);

        Float f2 = (Float)r2.getPropertyValue();
        previousReading = r2.getPreParsedContent();

        //
        // the statistics are calculated for the last interval
        //

        assertEquals(70f / 550, f2.floatValue(), 0.00001);

        assertEquals(605L, ((ProcStat)previousReading).getCumulativeCPUStatistics().getTotalTime());
    }

    @Test
    @Override
    public void parseSourceFileContent_ValidMacContent() throws Exception {

        OSMetricDefinition m = getMetricDefinitionToTest();

        //
        // no source file for this OSType yet, test is a noop
        //

        assertNull(m.getSourceFile(OSType.MAC));
    }

    @Test
    @Override
    public void parseSourceFileContent_ValidWindowsContent() throws Exception {

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
    public void parseCommandOutput_ValidLinuxOutput() throws Exception {

        String output =

                "top - 11:10:11 up 0 min,  1 user,  load average: 0.15, 0.04, 0.02\n" +
                        "Tasks:   1 total,   1 running,   0 sleeping,   0 stopped,   0 zombie\n" +
                        "%Cpu(s):  1.1 us,  2.2 sy,  3.3 ni, 4.4 id,  5.5 wa,  6.6 hi,  7.7 si,  8.8 st\n" +
                        "KiB Mem :   999936 total,   735636 free,   117680 used,   146620 buff/cache\n" +
                        "KiB Swap:        0 total,        0 free,        0 used.   715840 avail Mem\n" +
                        "\n" +
                        "   PID USER      PR  NI    VIRT    RES    SHR S %CPU %MEM     TIME+ COMMAND\n" +
                        "  1094 ansible   20   0  157440   1928   1484 R  0.0  0.2   0:00.00 top";

        CpuSoftwareInterruptTime d = getMetricDefinitionToTest();

        Property p = d.parseCommandOutput(OSType.LINUX, output, null);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());

        float expected = 7.7f;
        assertEquals(expected, ((Float) p.getValue()).floatValue(), 0.000001);
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
    protected CpuSoftwareInterruptTime getMetricDefinitionToTest() throws Exception {

        return new CpuSoftwareInterruptTime(new PropertyFactory(), new LocalOSAddress());
    }

    @Override
    protected byte[] getValidSourceFileContentToTest(OSType osType) throws Exception {

        if (OSType.LINUX.equals(osType)) {

            File f = new File(System.getProperty("basedir"), "src/test/resources/data/metric/proc-stat-reading-0.txt");
            assertTrue(f.isFile());
            return Files.readAllBytes(f.toPath());
        }

        return null;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
