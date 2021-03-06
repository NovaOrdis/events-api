/*
 * Copyright (c) 2016 Nova Ordis LLC
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

package io.novaordis.events.api.metric.jboss;

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricException;
import io.novaordis.events.api.metric.MetricSourceBase;
import io.novaordis.events.api.metric.MetricSourceException;
import io.novaordis.jboss.cli.JBossCliException;
import io.novaordis.jboss.cli.JBossControllerClient;
import io.novaordis.jboss.cli.JBossControllerClientFactory;
import io.novaordis.jboss.cli.JBossControllerClientFactoryImpl;
import io.novaordis.jboss.cli.model.JBossControllerAddress;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a JBoss controller, bound to a specific host:port and accessible using a certain user account.
 *
 * A JBossController instance is equal with another JBossController instance if those instances have the
 * same hosts, ports and user.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
public class JBossController extends MetricSourceBase {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(JBossController.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private JBossControllerClientFactory clientFactory;

    //
    // lazily instantiated and connected
    //
    private JBossControllerClient controllerClient;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Uses the default controller address ("localhost:9999"), as exposed by JBossControllerAddress constants.
     */
    public JBossController() throws AddressException {

        this(JBossControllerAddress.PROTOCOL + "://" +
                JBossControllerAddress.DEFAULT_HOST + ":" + JBossControllerAddress.DEFAULT_PORT);
    }

    public JBossController(String address) throws AddressException {

        this(new JBossControllerAddress(address));
    }

    /**
     * @param model the constructor will use the instance as a model and it will copy it locally, it will not use
     *              the reference to point to the given instance.
     */
    public JBossController(JBossControllerAddress model) throws AddressException {

        super(model);

        if (model == null) {

            throw new IllegalArgumentException("null controller address");
        }

        Address address = getAddress();

        String protocol = address.getProtocol();

        if (protocol == null) {

            address.setProtocol(JBossControllerAddress.PROTOCOL);
        }
        else if (!JBossControllerAddress.PROTOCOL.equals(protocol)) {

            throw new AddressException("invalid protocol " + protocol);
        }

        //
        // the default client factory, produces a regular JBossControllerClient instance
        //

        this.clientFactory = new JBossControllerClientFactoryImpl();

        log.debug(this + " constructed");
    }

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public synchronized void start() throws MetricSourceException {

        if (isStarted()) {

            log.debug(this + " is already started");
            return;
        }

        // TODO even if the controller client is present, test connectivity?

        log.debug(this + " starting ...");

        try {

            //
            // lazy instantiation
            //

            if (controllerClient == null) {

                JBossControllerClient c = clientFactory.buildControllerClient(getAddress());
                setControllerClient(c);
            }

            controllerClient.connect();

            log.debug(this + " started");
        }
        catch(Exception e) {

            throw new MetricSourceException(e);
        }
    }

    @Override
    public synchronized boolean isStarted() {

        return controllerClient != null && controllerClient.isConnected();
    }

    @Override
    public synchronized void stop() {

        if (controllerClient == null || !controllerClient.isConnected()) {

            return;
        }

        log.debug(this + " stopping ...");

        controllerClient.disconnect();

        log.debug(this + " stopped");
    }

    // MetricSourceBase overrides --------------------------------------------------------------------------------------

    @Override
    public JBossControllerAddress getAddress() {

        return (JBossControllerAddress)super.getAddress();
    }

    @Override
    public List<Property> collect(List<MetricDefinition> metricDefinitions) throws MetricException {

        if (!isStarted()) {

            throw new IllegalStateException(this + " not started");
        }

        List<Property> properties = null;

        for(MetricDefinition md : metricDefinitions) {

            if (!(md instanceof JBossDmrMetricDefinition)) {

                throw new IllegalArgumentException(this + " does not handle non-JBoss DMR metric " + md);
            }

            JBossDmrMetricDefinition jbmd = (JBossDmrMetricDefinition)md;

            String path = jbmd.getPath();
            String attributeName = jbmd.getAttributeName();
            Object value = null;

            try {

                value = controllerClient.getAttributeValue(path, attributeName);
            }
            catch (JBossCliException e) {

                log.warn(e.getMessage());
            }

            if (properties == null) {

                properties = new ArrayList<>();
            }

            Property p = md.buildProperty(value);
            properties.add(p);
        }

        if (properties == null) {

            return Collections.emptyList();
        }
        else {

            return properties;
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * May return null if the client was not initialized yet.
     */
    public JBossControllerClient getControllerClient() {

        return controllerClient;
    }

    @Override
    public String toString() {

        Address a = getAddress();
        return a == null ? "null" : a.toString();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    void setJBossControllerClientFactory(JBossControllerClientFactory factory) {

        this.clientFactory = factory;
    }

    void setControllerClient(JBossControllerClient controllerClient) {

        log.debug(this + " sets the controller client " + controllerClient);
        this.controllerClient = controllerClient;

        if(controllerClient != null) {

            setAddress(controllerClient.getControllerAddress());
        }
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
