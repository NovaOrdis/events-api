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

package io.novaordis.events.api.metric.jmx;

import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricDefinitionException;
import io.novaordis.jmx.JmxAddress;
import org.junit.Test;

import javax.management.MalformedObjectNameException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class JmxMetricDefinitionImplTest extends JmxMetricDefinitionTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void getDefinition() throws Exception {

        MetricDefinition d = getMetricDefinitionToTest();
        assertEquals("test.domain:service=TestService/testAttribute", d.getId());
    }

    // getId() -------------------------------------------------------------------------------------------------

    @Test
    public void getDefinition_KeysAreRenderedInTheOriginalOrder() throws Exception {

        JmxMetricDefinitionImpl d = new JmxMetricDefinitionImpl(
                new JmxAddress("jmx://test:70"), "test.domain", "C=valC,B=valB,A=valA", "testAttribute");

        String definition = d.getId();

        //
        // we expect the keys to NOT be sorted in lexical order
        //
        assertEquals("test.domain:C=valC,B=valB,A=valA/testAttribute", definition);
    }

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructors_InvalidObjectName() throws Exception {

        try {

            new JmxMetricDefinitionImpl(new JmxAddress("test:80"), "test.domain", "999999", "testAttribute");
            fail("should have thrown exception");
        }
        catch(MetricDefinitionException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("invalid ObjectName"));

            MalformedObjectNameException e2 = (MalformedObjectNameException)e.getCause();

            assertNotNull(e2);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JmxMetricDefinitionImpl getMetricDefinitionToTest() throws Exception {

        JmxBus bus = new JmxBus("jmx://localhost:1234");
        return new JmxMetricDefinitionImpl(bus.getAddress(), "test.domain", "service=TestService", "testAttribute");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
