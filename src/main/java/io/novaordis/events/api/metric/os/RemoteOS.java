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

package io.novaordis.events.api.metric.os;

import io.novaordis.events.api.metric.MetricSourceException;
import io.novaordis.ssh.SshConnection;
import io.novaordis.ssh.SshConnectionImpl;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressException;
import io.novaordis.utilities.address.OSAddress;
import io.novaordis.utilities.os.NativeExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * See https://kb.novaordis.com/index.php/Events-api_Concepts#Remote_OS
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/1/17
 */
public class RemoteOS extends OSSourceBase {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(RemoteOS.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public RemoteOS(String address) throws MetricSourceException {

        super();

        try {

            buildSshConnection(address);
        }
        catch(AddressException e) {

            throw new MetricSourceException(e);
        }
    }

    public RemoteOS(OSAddress address) throws MetricSourceException {

        super(address);

        if (!"ssh".equals(address.getProtocol())) {

            throw new IllegalArgumentException(address + " not an ssh address");
        }

        try {

            buildSshConnection(address.getLiteral());
        }
        catch(AddressException e) {

            throw new MetricSourceException(e);
        }
    }

    public RemoteOS(SshConnection c) {

        super(c.getAddress());
        setSshConnection(c);
    }

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public void start() throws MetricSourceException {

        if (isStarted()) {

            log.debug(this + " already started");
            return;
        }

        SshConnection c = getSshConnection();

        if (c == null) {

            throw new IllegalStateException(this + " incorrectly initialized");
        }

        c.connect();

        log.debug(this + " started");
    }

    @Override
    public boolean isStarted() {

        SshConnection c = getSshConnection();
        return c != null && c.isConnected();
    }

    @Override
    public void stop() {

        if (!isStarted()) {

            log.debug(this + " already stopped");
            return;
        }

        SshConnection c = getSshConnection();

        c.disconnect();

        log.debug(this + " stopped");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        Address address = getAddress();

        if (address == null) {

            return "null";
        }

        return address.toString();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    void setSshConnection(SshConnection c) {

        setNativeExecutor(c);
        setAddress(c.getAddress());
    }

    /**
     * May return null.
     */
    SshConnection getSshConnection() {

        NativeExecutor n = getNativeExecutor();

        if (n == null) {

            return null;
        }

        return (SshConnection)n;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    /**
     * Build the ssh connection, by parsing the address and creating the associated instances, but DO NOT implicitly
     * connect it.
     */
    private void buildSshConnection(String address) throws AddressException {

        //
        // do not connect yet
        //

        SshConnection c = new SshConnectionImpl(address);

        setSshConnection(c);
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
