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
public class PhysicalMemoryUsedTest extends OSMetricDefinitionTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // accessors -------------------------------------------------------------------------------------------------------

    @Test
    public void getId() throws Exception {

        PhysicalMemoryUsed md = new PhysicalMemoryUsed(new PropertyFactory(), new LocalOSAddress());
        assertEquals("PhysicalMemoryUsed", md.getId());
    }

    @Test
    public void getType() throws Exception {

        PhysicalMemoryUsed md = new PhysicalMemoryUsed(new PropertyFactory(), new LocalOSAddress());
        assertEquals(Long.class, md.getType());
    }

    @Test
    public void getBaseUnit() throws Exception {

        PhysicalMemoryUsed md = new PhysicalMemoryUsed(new PropertyFactory(), new LocalOSAddress());
        assertEquals(MemoryMeasureUnit.BYTE, md.getBaseUnit());
    }

    @Test
    public void getSimpleLabel() throws Exception {

        PhysicalMemoryUsed m = new PhysicalMemoryUsed(new PropertyFactory(), new LocalOSAddress());
        assertEquals("Used Physical Memory", m.getSimpleLabel());
    }

    @Test
    public void getDescription() throws Exception {

        PhysicalMemoryUsed m = new PhysicalMemoryUsed(new PropertyFactory(), new LocalOSAddress());
        assertTrue(m.getDescription().toLowerCase().contains("physical"));
        assertTrue(m.getDescription().toLowerCase().contains("memory"));
        assertTrue(m.getDescription().toLowerCase().contains("used"));
    }

    @Test
    public void getLinuxCommand() throws Exception {

        String expected = "/usr/bin/top -b -n 1 -p 0";

        PhysicalMemoryUsed m = new PhysicalMemoryUsed(new PropertyFactory(), new LocalOSAddress());
        assertEquals(expected, m.getCommand(OSType.LINUX));
    }

    @Test
    public void getMacCommand() throws Exception {

        String expected = "/usr/bin/top -l 1 -n 0";

        PhysicalMemoryUsed m = new PhysicalMemoryUsed(new PropertyFactory(), new LocalOSAddress());
        assertEquals(expected, m.getCommand(OSType.MAC));
    }

    @Test
    public void getWindowsCommand() throws Exception {

        PhysicalMemoryUsed m = new PhysicalMemoryUsed(new PropertyFactory(), new LocalOSAddress());
        assertNull(m.getCommand(OSType.WINDOWS));
    }

    // getSourceFile() -------------------------------------------------------------------------------------------------

    @Test
    public void getSourceFile() throws Exception {

        PhysicalMemoryUsed m = getMetricDefinitionToTest();
        assertNull(m.getSourceFile(OSType.LINUX));
        assertNull(m.getSourceFile(OSType.MAC));
        assertNull(m.getSourceFile(OSType.WINDOWS));
    }

    // parseSourceFileContent() ----------------------------------------------------------------------------------------

    @Override
    public void parseSourceFileContent_ValidLinuxOutput() throws Exception {
        throw new RuntimeException("parseSourceFileContent_ValidLinuxOutput() NOT YET IMPLEMENTED");
    }

    @Override
    public void parseSourceFileContent_ValidMacOutput() throws Exception {
        throw new RuntimeException("parseSourceFileContent_ValidMacOutput() NOT YET IMPLEMENTED");
    }

    @Override
    public void parseSourceFileContent_ValidWindowsOutput() throws Exception {
        throw new RuntimeException("parseSourceFileContent_ValidWindowsOutput() NOT YET IMPLEMENTED");
    }

    // parseCommandOutput() --------------------------------------------------------------------------------------------

    @Test
    @Override
    public void parseCommandOutput_ValidLinuxOutput() throws Exception {

        String output =

                "top - 11:10:11 up 0 min,  1 user,  load average: 0.15, 0.04, 0.02\n" +
                        "Tasks:   1 total,   1 running,   0 sleeping,   0 stopped,   0 zombie\n" +
                        "%Cpu(s):  2.8 us,  8.1 sy,  0.0 ni, 88.7 id,  0.4 wa,  0.0 hi,  0.1 si,  0.0 st\n" +
                        "KiB Mem :   999936 total,   735636 free,   117680 used,   146620 buff/cache\n" +
                        "KiB Swap:        0 total,        0 free,        0 used.   715840 avail Mem\n" +
                        "\n" +
                        "   PID USER      PR  NI    VIRT    RES    SHR S %CPU %MEM     TIME+ COMMAND\n" +
                        "  1094 ansible   20   0  157440   1928   1484 R  0.0  0.2   0:00.00 top";

        PhysicalMemoryUsed d = getMetricDefinitionToTest();

        Property p = d.parseCommandOutput(OSType.LINUX, output, null);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());

        long expected = 117680L * 1024;
        assertEquals(expected, ((Long) p.getValue()).longValue());
    }

    @Test
    @Override
    public void parseCommandOutput_ValidMacOutput() throws Exception {

        String output =

                "Processes: 263 total, 2 running, 5 stuck, 256 sleeping, 1421 threads \n" +
                        "2017/06/05 15:04:51\n" +
                        "Load Avg: 2.29, 2.02, 1.90 \n" +
                        "CPU usage: 2.73% user, 10.95% sys, 86.30% idle \n" +
                        "SharedLibs: 13M resident, 18M data, 0B linkedit.\n" +
                        "MemRegions: 59430 total, 5948M resident, 147M private, 4665M shared.\n" +
                        "PhysMem: 15G used (1447M wired), 1424M unused.\n" +
                        "VM: 699G vsize, 1064M framework vsize, 0(0) swapins, 0(0) swapouts.\n" +
                        "Networks: packets: 224778/181M in, 135478/17M out.\n" +
                        "Disks: 229646/5001M read, 113968/3311M written.\n" +
                        "\n" +
                        "\n";

        PhysicalMemoryUsed d = getMetricDefinitionToTest();

        Property p = d.parseCommandOutput(OSType.MAC, output, null);

        assertEquals(d.getId(), p.getName());
        assertEquals(d.getType(), p.getType());
        assertEquals(d.getBaseUnit(), p.getMeasureUnit());

        long expected = 15L * 1024 * 1024 * 1024;
        assertEquals(expected, ((Long) p.getValue()).longValue());
    }

    @Test
    @Override
    public void parseCommandOutput_ValidWindowsOutput() throws Exception {

        //
        // TODO noop for the time being, revisit when implementing Windows support
        //

//        String output =
//
//                "\"(PDH-CSV 4.0)\",\"\\\\NOMBP2W10-1\\Memory\\Page Faults/sec\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Available Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Committed Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Commit Limit\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Write Copies/sec\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Transition Faults/sec\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Cache Faults/sec\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Demand Zero Faults/sec\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Pages/sec\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Pages Input/sec\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Page Reads/sec\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Pages Output/sec\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Pool Paged Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Pool Nonpaged Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Page Writes/sec\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Pool Paged Allocs\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Pool Nonpaged Allocs\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Free System Page Table Entries\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Cache Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Cache Bytes Peak\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Pool Paged Resident Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\System Code Total Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\System Code Resident Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\System Driver Total Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\System Driver Resident Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\System Cache Resident Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\% Committed Bytes In Use\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Available KBytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Available MBytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Transition Pages RePurposed/sec\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Free & Zero Page List Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Modified Page List Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Standby Cache Reserve Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Standby Cache Normal Priority Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Standby Cache Core Bytes\"," +
//                        "\"\\\\NOMBP2W10-1\\Memory\\Long-Term Average Standby Cache Lifetime (s)\"\n" +
//                        "\"06/06/2017 22:59:09.095\",\"352.723452\",\"7177633792.000000\",\"1294233600.000000\"," +
//                        "\"9931640832.000000\",\"0.000000\",\"62.245315\",\"0.000000\",\"303.322408\",\"0.000000\"," +
//                        "\"0.000000\",\"0.000000\",\"0.000000\",\"238923776.000000\",\"67751936.000000\"," +
//                        "\"0.000000\",\"263166.000000\",\"163590.000000\",\"12272334.000000\",\"52121600.000000\"," +
//                        "\"155287552.000000\",\"199090176.000000\",\"0.000000\",\"0.000000\",\"15192064.000000\"," +
//                        "\"11460608.000000\",\"0.000000\",\"13.031418\",\"7009408.000000\",\"6845.000000\"," +
//                        "\"0.000000\",\"4636155904.000000\",\"167489536.000000\",\"2065068032.000000\"," +
//                        "\"305713152.000000\",\"170696704.000000\",\"14400.000000\"\n";
//
//        PhysicalMemoryTotal d = getMetricDefinitionToTest();
//
//        Property p;
//
//        try {
//
//            OSType.current = OSType.WINDOWS;
//
//            p = d.parseCommandOutput(output);
//
//        }
//        finally {
//
//            OSType.reset();
//        }
//
//        assertEquals(d.getId(), p.getName());
//        assertEquals(d.getType(), p.getType());
//        assertEquals(d.getBaseUnit(), p.getMeasureUnit());
//
//        long expected = 1024L * 1024;
//        assertEquals(expected, ((Long) p.getValue()).longValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected PhysicalMemoryUsed getMetricDefinitionToTest() throws Exception {
        return new PhysicalMemoryUsed(new PropertyFactory(), new LocalOSAddress());
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
