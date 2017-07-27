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
import io.novaordis.utilities.address.Address;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * A metric acquired from a JMX bus.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
public class JmxMetricDefinitionImpl extends MetricDefinitionBase implements JmxMetricDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Package protected static ----------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String domainName;
    private String keyValuePairs;
    private String attributeName;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @throws MetricDefinitionException in case an invalid metric definition is encountered. The error message
     *  must be human-readable, as it will most likely end up in error messages.
     *
     * @throws IllegalArgumentException
     */
    public JmxMetricDefinitionImpl(Address metricSourceAddress, String objectNameDomain,
                                   String objectNameKeyValuePairs, String attributeName) throws MetricDefinitionException {

        super(metricSourceAddress);

        //
        // we use the ObjectName constructor to parse, but we don't cache it
        //

        String objectNameString = objectNameDomain + ":" + objectNameKeyValuePairs;

        try {

            ObjectName on = new ObjectName(objectNameString);
            log.debug("ObjectName: " + on);
        }
        catch(MalformedObjectNameException e) {

            throw new MetricDefinitionException("invalid ObjectName: \"" + objectNameString + "\"", e);
        }

        this.domainName = objectNameDomain;
        this.keyValuePairs = objectNameKeyValuePairs;
        this.attributeName = attributeName;
    }

    // JmxMetricDefinition implementation ------------------------------------------------------------------------------

    @Override
    public String getDomainName() {

        return domainName;
    }

    @Override
    public String getKeyValuePairs() {

        return keyValuePairs;

    }

    @Override
    public String getAttributeName() {

        return attributeName;
    }

    // MetricDefinitionBase overrides ----------------------------------------------------------------------------------

    /**
     * @return the definition where keys are rendered in the order in which they were introduced
     */
    @Override
    public String getId() {

        return domainName + ":" + keyValuePairs + "/" + attributeName;
    }

    @Override
    public String getSimpleLabel() {

        return getMetricSourceAddress().getLiteral() + "/" + getId();
    }

    @Override
    public MeasureUnit getBaseUnit() {

        return null;
    }

    @Override
    public String getDescription() {

        return getId();
    }

    @Override
    public Class getType() {

        return null;
    }

    //
    // we need to override all source-related methods, as we override the storage
    //

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return getMetricSourceAddress() + "/" + getId();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
