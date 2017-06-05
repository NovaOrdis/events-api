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

package io.novaordis.events.api.metric;

import io.novaordis.events.api.event.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/5/17
 */
public abstract class MetricSourceBase implements MetricSource {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricException {

        //
        // we make sure all the definitions list this as source, otherwise fail
        //

        List<String> metricDefinitionsAsStrings = new ArrayList<>();

        for(MetricDefinition d: metricDefinitions) {

            if (!this.equals(d.getSource())) {

                throw new MetricException(d + " has a different source than " + this);
            }

            metricDefinitionsAsStrings.add(d.getDefinition());
        }

        return collect(metricDefinitionsAsStrings);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract List<Property> collect(List<String> metricDefinitions) throws MetricException;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
