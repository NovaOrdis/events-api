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
     * Installs a copy of the given address
     * @param a
     */
    protected void setAddress(Address a) {

        if (a == null) {

            this.address = null;
        }
        else {

            this.address = a.copy();
        }
    }

    protected void insureAllMetricDefinitionsAreAssociatedWithThisSource(List<MetricDefinition> metricDefinitions)
            throws MetricSourceException {

        Address thisAddress = getAddress();

        for(MetricDefinition d: metricDefinitions) {

            if (!thisAddress.equals(d.getMetricSourceAddress())) {

                throw new MetricSourceException(d + " has a different source than " + this);
            }
        }
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
