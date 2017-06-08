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

package io.novaordis.events.api.metric.os;

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricException;

import java.util.List;

/**
 * See https://kb.novaordis.com/index.php/Events-api_Concepts#Remote_OS
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/1/17
 */
public class RemoteOS extends OSSourceBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String address;

    // Constructors ----------------------------------------------------------------------------------------------------

    public RemoteOS(String address) throws Exception {

        super();

        if (address == null) {

            throw new IllegalArgumentException("null address");
        }

        this.address = address;
    }

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public String getAddress() {

        return address;
    }

    @Override
    public boolean hasAddress(String address) {

        //
        // TODO more complete implementation to follow
        //

        return this.address.equals(address);
    }

    @Override
    public List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricException {

        insureAllMetricDefinitionsAreAssociatedWithThisSource(metricDefinitions);

        throw new RuntimeException("NYE");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return address;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
