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
import io.novaordis.events.api.metric.MetricSourceBase;
import io.novaordis.events.api.metric.MetricSourceException;
import io.novaordis.jboss.cli.JBossControllerClient;
import io.novaordis.jboss.cli.model.JBossControllerAddress;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    //
    // lazily instantiated and connected
    //
    private JBossControllerClient controllerClient;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Uses the default controller address ("localhost:9999")
     */
    public JBossController() throws AddressException {

        this("jbosscli://localhost:9999");
    }

    public JBossController(String address) throws AddressException {

        this(new JBossControllerAddress(address));
    }

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
    }

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricSourceException {

        insureAllMetricDefinitionsAreAssociatedWithThisSource(metricDefinitions);

        throw new RuntimeException("NOT YET IMPLEMENTED");

//        List<Property> properties = new ArrayList<>();
//
//        for(MetricDefinition md : metricDefinitions) {
//
//            if (!(md instanceof JBossCliMetricDefinition)) {
//
//                log.debug(this + " does not handle non-jboss CLI metrics " + md);
//                properties.add(null);
//                continue;
//            }
//
//            JBossCliMetricDefinition jbmd = (JBossCliMetricDefinition)md;
//
//            JBossController thatSource = jbmd.getMetricSourceAddress();
//
//            if (!this.equals(thatSource)) {
//
//                log.debug(jbmd + " has a different source than " + this + ", ignorning ...");
//                properties.add(null);
//                continue;
//            }
//
//            //
//            // lazy instantiation
//            //
//
//            if (controllerClient == null) {
//
//                JBossControllerAddress address = jbmd.getMetricSourceAddress().getControllerAddress();
//                JBossControllerClient newClient = JBossControllerClient.getInstance(address);
//                setControllerClient(newClient);
//            }
//
//            //
//            // if the client is not connected, attempt to connect it, every time we collect metrics. This is useful if
//            // the JBoss instance is started after os-stats, or if the JBoss instance becomes inaccessible and then
//            // reappears
//            //
//
//            if (!controllerClient.isConnected()) {
//
//                try {
//
//                    log.debug("attempting to connect " + controllerClient);
//
//                    controllerClient.connect();
//                }
//                catch(Exception e) {
//
//                    log.warn(e.getMessage());
//                    log.debug("controller client connection failure", e);
//                    continue;
//                }
//            }
//
//            String path = jbmd.getPath();
//            String attributeName = jbmd.getAttributeName();
//            Object attributeValue = null;
//
//            try {
//
//                attributeValue = controllerClient.getAttributeValue(path, attributeName);
//            }
//            catch (JBossCliException e) {
//
//                log.warn(e.getMessage());
//            }
//
//            if (attributeValue == null) {
//                properties.add(null);
//                continue;
//            }
//
//            String name = jbmd.getId();
//
//            Property p;
//
//            if (attributeValue instanceof String) {
//
//                p = new StringProperty(name, (String)attributeValue);
//            }
//            else if (attributeValue instanceof Integer) {
//
//                p = new IntegerProperty(name, (Integer)attributeValue);
//            }
//            else if (attributeValue instanceof Long) {
//
//                p = new LongProperty(name, (Long)attributeValue);
//            }
//            else {
//                throw new RuntimeException(attributeValue.getClass() + " SUPPORT NOT YET IMPLEMENTED");
//            }
//
//            properties.add(p);
//        }
//
//        return properties;
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

    // MetricSourceBase overrides --------------------------------------------------------------------------------------

    @Override
    public JBossControllerAddress getAddress() {

        Address a = super.getAddress();
        return (JBossControllerAddress)a;
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
