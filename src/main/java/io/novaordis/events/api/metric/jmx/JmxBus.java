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
import io.novaordis.events.api.metric.MetricException;
import io.novaordis.events.api.metric.MetricSourceBase;
import io.novaordis.events.api.metric.MetricSourceException;
import io.novaordis.jmx.JmxAddress;
import io.novaordis.jmx.JmxClient;
import io.novaordis.jmx.JmxClientFactory;
import io.novaordis.jmx.JmxClientFactoryImpl;
import io.novaordis.jmx.JmxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents access to a JMX bus running on the local or a remote JVM.
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
    private JmxClient jmxClient;

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

        //
        // the default client factory, produces a regular JmxClient instance
        //

        this.clientFactory = new JmxClientFactoryImpl();

        log.debug(this + " constructed");
    }

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public synchronized void start() throws MetricSourceException {

        if (isStarted()) {

            log.debug(this + " already started");
            return;
        }

        log.debug(this + " starting ...");

        try {

            //
            // lazy instantiation
            //

            if (jmxClient == null) {

                JmxClient c = clientFactory.build(getAddress());
                setJmxClient(c);
            }

            jmxClient.connect();

            log.debug(this + " started");
        }
        catch(Exception e) {

            throw new MetricSourceException(e);
        }
    }

    @Override
    public synchronized boolean isStarted() {

        return jmxClient != null && jmxClient.isConnected();
    }

    @Override
    public synchronized void stop() {

        if (!isStarted()) {

            log.debug(this + " is already stopped");
            return;
        }

        jmxClient.disconnect();
        jmxClient = null;

        log.debug(this + " stopped");
    }

    // MetricSourceBase overrides --------------------------------------------------------------------------------------

    @Override
    public JmxAddress getAddress() {

        return (JmxAddress)super.getAddress();
    }

    @Override
    public List<Property> collect(List<MetricDefinition> metricDefinitions) throws MetricException {

        if (!isStarted()) {

            throw new IllegalStateException(this + " not started");
        }

        if (metricDefinitions.isEmpty()) {

            return Collections.emptyList();
        }

        List<Property> properties = new ArrayList<>();

        for(MetricDefinition md : metricDefinitions) {

            if (!(md instanceof JmxMetricDefinition)) {

                throw new IllegalArgumentException(this + " does not handle non-JMX metric " + md);
            }

            Object value = null;

            JmxMetricDefinition jbmd = (JmxMetricDefinition)md;

            try {

                ObjectName on = jbmd.getObjectName();
                MBeanServerConnection mBeanServerConnection = jmxClient.getMBeanServerConnection();
                String attributeName = jbmd.getAttributeName();
                value = mBeanServerConnection.getAttribute(on, attributeName);

                if (log.isTraceEnabled()) {

                    log.trace(getAddress() + " returned " +
                            value + (value == null ? "" : " (" + value.getClass().getSimpleName() + ")") +
                            " for \"" + attributeName + "\"");
                }
            }
            catch(MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException e) {

                //
                // these types of exceptions denote a problem with an individual metric, not with the source as a whole;
                // we will continue to collect the other metrics and log the failure as warning. The entire call does
                // NOT fail
                //

                log.warn("failed to collect " + md.getId() + " from " + this, e);
            }
            catch(JmxException | IOException e) {

                //
                // this is an indication that the JMX source, and not an individual metric, failed as a whole. An
                // example of such occurrence is when a JBoss instance goes down: we get
                // "org.jboss.remoting3.NotOpenException: Writes closed" here. When we are in this situation, we
                // stop the source, in the hope this is a transient failure and the next attempt to start will clear
                // it, and bubble the exception up.
                //

                log.warn(this + " detected source-wide failure: " + e.getMessage());

                stop();

                throw new MetricSourceException(e);
            }

            Property p = md.buildProperty(value);
            properties.add(p);
        }

        return properties;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return "" + getAddress();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    void setJmxClientFactory(JmxClientFactory factory) {

        this.clientFactory = factory;
    }

    void setJmxClient(JmxClient c) {

        if (log.isTraceEnabled()) {

            log.trace(this + " sets the JMX client " + c);
        }

        this.jmxClient = c;

        if(jmxClient != null) {

            setAddress(jmxClient.getAddress());
        }
    }

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
