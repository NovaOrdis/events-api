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

package io.novaordis.events.api.metric.os;

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.events.api.measure.MemoryMeasureUnit;
import io.novaordis.events.api.metric.MetricDefinitionParser;
import io.novaordis.events.api.metric.MetricSourceRepositoryImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class PhysicalMemoryUsedTest extends OSMetricTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void parse() throws Exception {

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        PhysicalMemoryUsed m =
                (PhysicalMemoryUsed)MetricDefinitionParser.parse(r, "PhysicalMemoryUsed");

        assertNotNull(m);
        assertEquals(m.getSource(), r.getSources(LocalOS.class).iterator().next());
    }

    // defaults --------------------------------------------------------------------------------------------------------

    @Test
    public void getDefaultMeasureUnit() throws Exception {

        PhysicalMemoryUsed mmd = getMetricDefinitionToTest();
        MeasureUnit mm = mmd.getMeasureUnit();
        assertEquals(MemoryMeasureUnit.BYTE, mm);
    }

    @Test
    public void getDefaultType() throws Exception {

        PhysicalMemoryUsed mmd = getMetricDefinitionToTest();
        Class c = mmd.getType();
        assertEquals(Long.class, c);
    }

    @Test
    public void getSimpleLabel() throws Exception {

        PhysicalMemoryUsed m = new PhysicalMemoryUsed(new LocalOS());
        assertEquals("Used Physical Memory", m.getSimpleLabel());
    }

    // mac() -----------------------------------------------------------------------------------------------------------

    @Test
    public void mac_production1() throws Exception {

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

        Property p = PhysicalMemoryUsed.mac(output);

        String name = p.getName();
        assertEquals(getMetricDefinitionToTest().getDefinition(), name);

        Class type = p.getType();
        assertEquals(Long.class, type);

        MeasureUnit u = p.getMeasureUnit();
        assertEquals(MemoryMeasureUnit.BYTE, u);

        long expected = 15L * 1024 * 1024 * 1024;
        assertEquals(expected, ((Long) p.getValue()).longValue());
    }

    // linux() ---------------------------------------------------------------------------------------------------------

    @Test
    public void linux_production1() throws Exception {

        String output =

                "top - 11:10:11 up 0 min,  1 user,  load average: 0.15, 0.04, 0.02\n" +
                        "Tasks:   1 total,   1 running,   0 sleeping,   0 stopped,   0 zombie\n" +
                        "%Cpu(s):  2.8 us,  8.1 sy,  0.0 ni, 88.7 id,  0.4 wa,  0.0 hi,  0.1 si,  0.0 st\n" +
                        "KiB Mem :   999936 total,   735636 free,   117680 used,   146620 buff/cache\n" +
                        "KiB Swap:        0 total,        0 free,        0 used.   715840 avail Mem\n" +
                        "\n" +
                        "   PID USER      PR  NI    VIRT    RES    SHR S %CPU %MEM     TIME+ COMMAND\n" +
                        "  1094 ansible   20   0  157440   1928   1484 R  0.0  0.2   0:00.00 top";

        Property p = PhysicalMemoryUsed.linux(output);

        String name = p.getName();
        assertEquals(getMetricDefinitionToTest().getDefinition(), name);

        Class type = p.getType();
        assertEquals(Long.class, type);

        MeasureUnit u = p.getMeasureUnit();
        assertEquals(MemoryMeasureUnit.BYTE, u);

        long expected = 117680L * 1024;
        assertEquals(expected, ((Long) p.getValue()).longValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected PhysicalMemoryUsed getMetricDefinitionToTest() throws Exception {
        return new PhysicalMemoryUsed(new LocalOS());
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
