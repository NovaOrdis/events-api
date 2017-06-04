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

import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.events.api.measure.MemoryMeasureUnit;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricDefinitionTest;
import io.novaordis.events.api.metric.source.MetricSource;
import io.novaordis.events.api.metric.source.OSCommand;
import io.novaordis.utilities.os.OS;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class PhysicalMemoryFreeTest extends MetricDefinitionTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // getMeasureUnit() ------------------------------------------------------------------------------------------------

    @Test
    public void getDefaultMeasureUnit() throws Exception {

        PhysicalMemoryFree mmd = getMetricDefinitionToTest();
        MeasureUnit mm = mmd.getMeasureUnit();
        assertEquals(MemoryMeasureUnit.BYTE, mm);
    }

    // getDefaultType() ------------------------------------------------------------------------------------------------

    @Test
    public void getDefaultType() throws Exception {

        PhysicalMemoryFree mmd = getMetricDefinitionToTest();
        Class c = mmd.getType();
        assertEquals(Long.class, c);
    }

    // getInstance() ---------------------------------------------------------------------------------------------------

    @Test
    public void getInstance() throws Exception {

        PhysicalMemoryFree m = (PhysicalMemoryFree) MetricDefinition.getInstance("PhysicalMemoryFree");
        assertNotNull(m);
    }

    @Test
    public void getSimpleLabel() throws Exception {

        PhysicalMemoryFree m = new PhysicalMemoryFree();
        assertEquals("Free Physical Memory", m.getSimpleLabel());
    }

    // sources ---------------------------------------------------------------------------------------------------------

    @Test
    public void sourcesLinux() throws Exception {

        PhysicalMemoryFree m = getMetricDefinitionToTest();

        List<MetricSource> linuxSources = m.getSources(OS.Linux);
        assertEquals(1, linuxSources.size());
        OSCommand c = (OSCommand) linuxSources.get(0);
        assertEquals("top", c.getCommand());
    }

    @Test
    public void sourcesMac() throws Exception {

        PhysicalMemoryFree m = getMetricDefinitionToTest();

        List<MetricSource> macSources = m.getSources(OS.MacOS);
        assertEquals(1, macSources.size());
        OSCommand c = (OSCommand) macSources.get(0);
        assertEquals("top", c.getCommand());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected PhysicalMemoryFree getMetricDefinitionToTest() throws Exception {
        return new PhysicalMemoryFree();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
