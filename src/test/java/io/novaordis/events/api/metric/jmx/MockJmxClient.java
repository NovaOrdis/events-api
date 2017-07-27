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

import io.novaordis.jmx.JmxAddress;
import io.novaordis.jmx.JmxClient;
import io.novaordis.jmx.JmxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServerConnection;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/16/17
 */
public class MockJmxClient implements JmxClient {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(MockJmxClient.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private JmxAddress address;

    private boolean connected;

    private MockMBeanServerConnection mockMBeanServerConnection;

    private boolean failOnGetMBeanServerConnection;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockJmxClient(JmxAddress address) {

        this.address = address;
        this.mockMBeanServerConnection = new MockMBeanServerConnection();
    }

    // JmxClient implementation ----------------------------------------------------------------------------------------

    @Override
    public JmxAddress getAddress() {

        return address;
    }

    @Override
    public void setProtocolProviderPackage(String protocolProviderPackage) {

        throw new RuntimeException("setProtocolProviderPackage() NOT YET IMPLEMENTED");
    }

    @Override
    public void connect() throws JmxException {

        connected = true;

        log.info(this + " connected");
    }

    @Override
    public void disconnect() {

        connected = false;

        log.info(this + " disconnected");
    }

    @Override
    public boolean isConnected() {

        return connected;
    }

    @Override
    public MBeanServerConnection getMBeanServerConnection() throws JmxException {

        if (failOnGetMBeanServerConnection) {

            throw new JmxException("SYNTHETIC", new RuntimeException());
        }

        return mockMBeanServerConnection;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void configureToFailOnGetMBeanServerConnection() {

        this.failOnGetMBeanServerConnection = true;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
