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

import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.events.api.metric.MetricDefinitionBase;
import io.novaordis.events.api.metric.MetricDefinitionException;
import io.novaordis.events.api.metric.MetricSource;

/**
 * A metric acquired from a JMX bus.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
public class JmxMetricDefinition extends MetricDefinitionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Package protected static ----------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param definition the metric definition similar to
     *                   "jboss.as:subsystem=messaging,hornetq-server=default,jms-queue=DLQ.messageCount". Must NOT
     *                   include the metric source, which must be parsed separately and provided.
     *
     * @throws MetricDefinitionException in case an invalid metric definition is encountered. The error message
     *  must be human-readable, as it will most likely end up in error messages.
     *
     * @throws IllegalArgumentException
     */
    public JmxMetricDefinition(MetricSource source, String definition) throws MetricDefinitionException {

        super(source);

        throw new RuntimeException("NYE");

    }

    // MetricDefinitionBase overrides ----------------------------------------------------------------------------------

    @Override
    public String getDefinition() {

        throw new RuntimeException("NYE");
    }

    @Override
    public String getSimpleLabel() {

        throw new RuntimeException("NYE");
    }

    @Override
    public MeasureUnit getMeasureUnit() {

        throw new RuntimeException("NYE");
    }

    @Override
    public String getDescription() {

        throw new RuntimeException("NYE");
    }

    @Override
    public Class getType() {
        throw new RuntimeException("getType() NOT YET IMPLEMENTED");
    }

    //
    // we need to override all source-related methods, as we override the storage
    //

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return "?";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
