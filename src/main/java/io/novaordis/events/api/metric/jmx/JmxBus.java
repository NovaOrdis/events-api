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

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.metric.MetricException;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricSourceBase;
import io.novaordis.events.api.metric.MetricSourceException;

import java.util.List;

/**
 * Represents a JMX bus of a local or remote JVM.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
public class JmxBus extends MetricSourceBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private JmxServerAddress address;

    // Constructors ----------------------------------------------------------------------------------------------------

    public JmxBus() throws JmxException {

        this.address = new JmxServerAddress();
    }

    public JmxBus(String address) throws JmxException  {

        this.address = new JmxServerAddress(address);
    }

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public String getAddress() {

        return address.getLiteral();
    }

    @Override
    public boolean hasAddress(String address) {

        return getAddress().equals(address);
    }

    @Override
    public List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricException {

        insureAllMetricDefinitionsAreAssociatedWithThisSource(metricDefinitions);

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public void start() throws MetricSourceException {
        throw new RuntimeException("start() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isStarted() {
        throw new RuntimeException("isStarted() NOT YET IMPLEMENTED");
    }

    @Override
    public void stop() {
        throw new RuntimeException("stop() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public JmxServerAddress getJmxServerAddress() {

        return address;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {

            return true;
        }

        if (address == null) {

            return false;
        }

        if (!(o instanceof JmxBus)) {

            return false;
        }

        JmxBus that = (JmxBus)o;

        return address.equals(that.address);
    }

    @Override
    public int hashCode() {

        if (address == null) {

            return 0;
        }

        return address.hashCode();
    }

    @Override
    public String toString() {

        return "" + address;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
