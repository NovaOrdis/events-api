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

import io.novaordis.utilities.address.Address;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class MockMetricDefinition extends MockMetricDefinitionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String simpleLabel;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockMetricDefinition(Address metricSourceAddress) {

        this(null, metricSourceAddress);
        setId(getClass().getSimpleName());
    }

    public MockMetricDefinition(String id, Address metricSourceAddress) {

        this(id, null, metricSourceAddress);
    }

    public MockMetricDefinition(String id, Class type, Address metricSourceAddress) {

        super(id, type, metricSourceAddress);
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

    @Override
    public String getSimpleLabel() {

        return simpleLabel;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setSimpleLabel(String s) {

        this.simpleLabel = s;
    }

    @Override
    public String toString() {

        return "" + getId();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
