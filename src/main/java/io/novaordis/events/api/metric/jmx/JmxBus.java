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
import io.novaordis.jboss.cli.JBossControllerClient;
import io.novaordis.jmx.JmxAddress;
import io.novaordis.jmx.JmxClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

/**
 * Represents a JMX bus running on a local or remote JVM.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
public class JmxBus extends MetricSourceBase {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(JmxBus.class);

    private JmxClientFactory clientFactory;

    //
    // lazily instantiated and connected
    //
    private JBossControllerClient controllerClient;

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * There is no public no-argument constructor because there are no default coordinates for a JMX bus. In order
     * to get a functional JMX bus, we will need to specify an address. This constructor returns a partially
     * initialized instance.
     */
    protected JmxBus() {

        super(null);
    }

    public JmxBus(String address) throws MetricSourceException {

        this(stringToJmxAddress(address));
    }

    public JmxBus(JmxAddress model) throws MetricSourceException  {

        super(model);

//        Address address = getAddress();
//
//        String protocol = address.getProtocol();
//
//        if (protocol == null) {
//
//            address.setProtocol(PROTOCOL);
//        }
//        else if (!PROTOCOL.equals(protocol)) {
//
//            throw new MetricSourceException("invalid protocol " + protocol);
//        }
//
//        Integer port = address.getPort();
//
//        if (port == null) {
//
//            address.setPort(DEFAULT_PORT);
//        }

        log.debug(this + " constructed");
    }

    // MetricSource implementation -------------------------------------------------------------------------------------

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

    // MetricSourceBase overrides --------------------------------------------------------------------------------------

    @Override
    public JmxAddress getAddress() {

        return (JmxAddress)super.getAddress();
    }

    @Override
    public List<Property> collect(List<MetricDefinition> metricDefinitions) throws MetricSourceException {

        if (!isStarted()) {

            throw new IllegalStateException(this + " not started");
        }

        throw new RuntimeException("NOT YET IMPLEMENTED");
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
    private static JmxAddress stringToJmxAddress(String a) throws MetricSourceException {

        try {

            return new JmxAddress(a);
        }
        catch (Exception e) {

            throw new MetricSourceException(e);
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
