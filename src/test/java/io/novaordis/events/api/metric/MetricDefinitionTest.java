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

package io.novaordis.events.api.metric;

import io.novaordis.events.api.measure.MeasureUnit;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public abstract class MetricDefinitionTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void getDefinition() throws Exception {

        MetricDefinition d = getMetricDefinitionToTest();

        //
        // default behavior
        //
        assertEquals(d.getClass().getSimpleName(), d.getDefinition());
    }

    @Test
    public void getDescription() throws Exception {

        MetricDefinition d = getMetricDefinitionToTest();

        String desc = d.getDescription();
        assertNotNull(desc);
        assertFalse(desc.isEmpty());
    }

    // getLabel() ------------------------------------------------------------------------------------------------------

    @Test
    public void getLabel_WithoutMeasureUnit() throws Exception {

        MetricDefinition d = getMetricDefinitionToTest();

        String label = d.getLabel();

        assertNotNull(label);
    }

    @Test
    public void getLabel_WithMeasureUnit() throws Exception {

        MetricDefinition d = getMetricDefinitionToTest();

        String label = d.getLabel(LabelAttribute.MEASURE_UNIT);

        assertNotNull(label);

        //
        // if the metric definition includes a measure unit, the measure unit must be present in the label,
        // parantheses-enclosed
        //

        MeasureUnit mu = d.getMeasureUnit();

        if (mu == null) {

            //
            // no measure unit in label
            //

            assertFalse(label.contains("("));
            assertFalse(label.contains(")"));
            assertEquals(label, d.getLabel());
        }
        else {

            String muLabelFragment = " (" + mu.getLabel() + ")";
            assertTrue(label.endsWith(muLabelFragment));
            assertTrue(label.contains(d.getLabel()));
        }
    }

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void getInstance_UnknownInstance() throws Exception {

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();

        try {

            MetricDefinitionParser.parse(r, "we are pretty sure there's no such metric");
            fail("should throw exception");
        }
        catch(MetricDefinitionException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("no known parser can understand"));
        }

        assertTrue(r.isEmpty());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract MetricDefinition getMetricDefinitionToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
