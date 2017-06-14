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
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricSourceBase;
import io.novaordis.events.api.metric.MetricSourceException;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

/**
 * Represents a JMX bus of a local or remote JVM.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
public class JmxBus extends MetricSourceBase {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String PROTOCOL = "jmx";

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 9999;

    private static final Logger log = LoggerFactory.getLogger(JmxBus.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public JmxBus() throws MetricSourceException {

        this("jmx://" + DEFAULT_HOST + ":" + DEFAULT_PORT);
    }

    public JmxBus(String address) throws MetricSourceException {

        this(stringToAddress(address));
    }

    public JmxBus(Address model) throws MetricSourceException  {

        super(model);

        Address address = getAddress();

        String protocol = address.getProtocol();

        if (protocol == null) {

            address.setProtocol(PROTOCOL);
        }
        else if (!PROTOCOL.equals(protocol)) {

            throw new MetricSourceException("invalid protocol " + protocol);
        }

        Integer port = address.getPort();

        if (port == null) {

            address.setPort(DEFAULT_PORT);
        }

        log.debug(this + " constructed");
    }

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricSourceException {

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

    @Override
    public String toString() {

        return "" + getAddress();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    /**
     * We need this trick to work around the constructor limitation of not allowing a try catch around this();
     */
    private static Address stringToAddress(String a) throws MetricSourceException {

        try {

            return new AddressImpl(a);
        }
        catch (Exception e) {

            throw new MetricSourceException(e);
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
