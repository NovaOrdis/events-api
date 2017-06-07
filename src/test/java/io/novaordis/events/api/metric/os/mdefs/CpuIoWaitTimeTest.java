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

import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.events.api.measure.Percentage;
import io.novaordis.events.api.metric.MetricDefinitionParser;
import io.novaordis.events.api.metric.MetricSourceRepositoryImpl;
import io.novaordis.events.api.metric.os.OSMetricDefinitionTest;
import io.novaordis.events.api.metric.os.mdefs.CpuIoWaitTime;
import io.novaordis.events.api.metric.os.mdefs.LocalOS;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class CpuIoWaitTimeTest extends OSMetricDefinitionTest {

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

        CpuIoWaitTime m = (CpuIoWaitTime)MetricDefinitionParser.parse(r, "CpuIoWaitTime");

        assertNotNull(m);
        assertEquals(m.getSource(), r.getSources(LocalOS.class).iterator().next());
    }

    // getBaseUnit() ------------------------------------------------------------------------------------------------

    @Test
    public void measureUnitIsPercentage() throws Exception {

        CpuIoWaitTime m = getMetricDefinitionToTest();

        MeasureUnit mu = m.getBaseUnit();

        assertEquals(Percentage.getInstance(), mu);
    }

    // getType() -------------------------------------------------------------------------------------------------------

    @Test
    public void typeIsFloat() throws Exception {

        CpuIoWaitTime m = getMetricDefinitionToTest();

        Class t = m.getType();

        assertEquals(Float.class, t);
    }

    @Test
    public void getSimpleLabel() throws Exception {

        CpuIoWaitTime m = new CpuIoWaitTime(new LocalOS());
        assertEquals("CPU I/O Wait Time", m.getSimpleLabel());
    }

    // parseCommandOutput() --------------------------------------------------------------------------------------------

    @Test
    @Override
    public void parseCommandOutput_ValidLinuxOutput() throws Exception {

        fail("parseCommandOutput_ValidLinuxOutput() NOT YET IMPLEMENTED");
    }

    @Test
    @Override
    public void parseCommandOutput_ValidMacOutput() throws Exception {
        fail("parseCommandOutput_ValidMacOutput() NOT YET IMPLEMENTED");
    }

    @Test
    @Override
    public void parseCommandOutput_ValidWindowsOutput() throws Exception {
        fail("parseCommandOutput_ValidWindowsOutput() NOT YET IMPLEMENTED");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected CpuIoWaitTime getMetricDefinitionToTest() throws Exception {

        return new CpuIoWaitTime(new LocalOS());
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
