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

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.measure.MeasureUnit;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
        assertEquals(d.getClass().getSimpleName(), d.getId());
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

        MeasureUnit mu = d.getBaseUnit();

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

        try {

            MetricDefinitionParser.parse("we are pretty sure there's no such metric");
            fail("should throw exception");
        }
        catch(MetricDefinitionException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("no known parser can understand"));
        }
    }

    // buildProperty() -------------------------------------------------------------------------------------------------

    @Test
    public void buildProperty_Null() throws Exception {

        MetricDefinition d = getMetricDefinitionToTest();

        String id = d.getId();
        Class type = d.getType();
        MeasureUnit mu = d.getBaseUnit();

        Property p = d.buildProperty(null);

        String name = p.getName();
        Class pType = p.getType();
        MeasureUnit pMu = p.getMeasureUnit();
        Object pValue = p.getValue();

        assertNotNull(p);

        assertEquals(name, id);
        assertEquals(pType, type);
        assertEquals(pMu, mu);
        assertNull(pValue);
    }

    @Test
    public void buildProperty() throws Exception {

        MetricDefinition d = getMetricDefinitionToTest();

        String id = d.getId();
        Class type = d.getType();
        MeasureUnit mu = d.getBaseUnit();

        Object testValue = generateTestValue(type);

        Property p = d.buildProperty(testValue);

        String name = p.getName();
        Class pType = p.getType();
        MeasureUnit pMu = p.getMeasureUnit();
        Object pValue = p.getValue();

        assertNotNull(p);

        assertEquals(name, id);
        assertEquals(pType, type);
        assertEquals(pMu, mu);
        assertEquals(pValue, testValue);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract MetricDefinition getMetricDefinitionToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    private Object generateTestValue(Class type) {

        if (type == null) {

            throw new IllegalArgumentException("null type");
        }

        if (Integer.class.equals(type)) {

            return 7;
        }
        else if (Long.class.equals(type)) {

            return 7L;
        }
        else if (String.class.equals(type)) {

            return "test";
        }
        else if (Double.class.equals(type)) {

            return 7.0d;
        }
        else if (Float.class.equals(type)) {

            return 7.0f;
        }
        else if (Boolean.class.equals(type)) {

            return true;
        }
        else {

            throw new RuntimeException("NOT YET IMPLEMENTED: extend this to support " + type);
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
