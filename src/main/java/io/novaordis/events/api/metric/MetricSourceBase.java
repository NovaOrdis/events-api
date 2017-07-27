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
import io.novaordis.utilities.address.Address;

import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/5/17
 */
public abstract class MetricSourceBase implements MetricSource {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Address address;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param address - makes a copy of the given address and stores it internally.
     *
     * @see MetricSourceBase#setAddress(Address)
     */
    protected MetricSourceBase(Address address) {

        setAddress(address);
    }

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public Address getAddress() {

        return address;
    }

    @Override
    public final boolean hasAddress(Address address) {

        return this.address != null && this.address.equals(address);
    }

    /**
     * The base implementation of this method insures that all metric definitions are associated with the source
     * the method is invoked on, and it also starts the source, if not started already.
     *
     * @throws MetricSourceException if a metric definition is associated with a different metric source than
     *      this one, if the source is not started and cannot be started.
     *
     * @throws MetricException by other underlying conditions.
     */
    @Override
    public List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricException {

        if (metricDefinitions == null) {

            throw new IllegalArgumentException("null metric definition list");
        }

        insureAllDefinitionsAreAssociatedWithThisAddress(metricDefinitions);

        if (!isStarted()) {

            start();
        }

        return collect(metricDefinitions);
    }

    @Override
    public final boolean equals(Object o) {

        if (this == o) {

            return true;
        }

        if (o == null) {

            return false;
        }

        if (address == null) {

            return false;
        }

        if (!getClass().equals(o.getClass())) {

            return false;
        }

        MetricSource that = (MetricSource)o;

        return address.equals(that.getAddress());
    }

    @Override
    public final int hashCode() {

        if (address == null) {

            return 0;
        }

        return address.hashCode();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * Installs a copy of the given address.
     *
     * @see Address#copy()
     */
    protected void setAddress(Address a) {

        if (a == null) {

            this.address = null;
        }
        else {

            this.address = a.copy();
        }
    }

    /**
     * The caller of this method guarantees that all metric definitions passed as argument apply to this source,
     * meaning that the address part of the definition has been already verified, and it can actually be ignored.
     * The caller of the method also guarantees that the source is started. If the source is not started,
     * the method will throw an IllegalStateExceptions.
     *
     * @return a list of Properties. If one or more metrics could not be collected, because they were not available
     * of the collection failed, a non-null Property with a null value will be returned on the corresponding position
     * in the list. For mre details:
     *
     * @see MetricSource#collectMetrics(List)
     *
     * @throws IllegalStateException if the source is not started. The source must be started by the calling layer.
     * @throws IllegalArgumentException if the metric is not of the correct type.
     * @throws MetricSourceException when the source failed during collection.
     * @throws MetricException when an individual metric processing failed in an unrecoverable way.
     */
    protected abstract List<Property> collect(List<MetricDefinition> metricDefinitions) throws MetricException;

    // Private ---------------------------------------------------------------------------------------------------------

    private void insureAllDefinitionsAreAssociatedWithThisAddress(List<MetricDefinition> metricDefinitions)
            throws MetricSourceException {

        if (metricDefinitions == null) {

            throw new IllegalArgumentException("null metric definition list");
        }

        Address thisAddress = getAddress();

        for(MetricDefinition d: metricDefinitions) {

            if (!thisAddress.equals(d.getMetricSourceAddress())) {

                throw new MetricSourceException(d + " has a different source than " + this);
            }
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
