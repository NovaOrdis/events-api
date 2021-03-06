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


import io.novaordis.events.api.event.PropertyFactory;
import io.novaordis.utilities.address.Address;

/**
 * Invoked via reflection.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
@SuppressWarnings("unused")
public class MockMetricDefinition2 extends MockMetricDefinitionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Invoked via reflection.
     *
     * @param metricSourceAddress must always have a non-null source.
     */
    @SuppressWarnings("unused")
    protected MockMetricDefinition2(PropertyFactory f, Address metricSourceAddress) {
        super(f, metricSourceAddress);
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

    @Override
    public String getId() {
        return "Mock Metric Definition 2";
    }

    @Override
    public String getSimpleLabel() {

        throw new RuntimeException("getSimpleLabel() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
