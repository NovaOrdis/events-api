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

import io.novaordis.events.api.metric.MockOSAddress;
import io.novaordis.ssh.SshConnection;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressException;
import io.novaordis.utilities.os.NativeExecutionException;
import io.novaordis.utilities.os.NativeExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/8/17
 */
public class MockSshConnection extends MockNativeExecutor implements SshConnection {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(MockSshConnection.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Address address;
    private MockNativeExecutor delegate;
    private boolean connected;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockSshConnection() throws AddressException {

        this("mock-ssh-server");
    }

    /**
     * "host:port" or "host" expected.
     */
    public MockSshConnection(String address) throws AddressException {

        this.address = new MockOSAddress(address);
        this.delegate = new MockNativeExecutor();

        //
        // we teach the ssh server at the other end to respond to "hostname"
        //
        delegate.putNativeExecutionResult("hostname", new NativeExecutionResult(0, address, null, true, true));
    }

    // MockNativeExecutor overrides ------------------------------------------------------------------------------------

    @Override
    public void setStdout(String s) {

        delegate.setStdout(s);
    }

    @Override
    public void setStderr(String s) {

        delegate.setStderr(s);
    }

    @Override
    public List<String> getCommandExecutionHistory() {

        return delegate.getCommandExecutionHistory();
    }

    // SshConnection implementation ------------------------------------------------------------------------------------

    @Override
    public Address getAddress() {

        return address;
    }

    @Override
    public NativeExecutionResult execute(String command) throws NativeExecutionException {

        return delegate.execute(command);
    }

    @Override
    public NativeExecutionResult execute(File directory, String command) throws NativeExecutionException {

        return delegate.execute(directory, command);
    }

    // Lifecycle -------------------------------------------------------------------------------------------------------

    @Override
    public void connect() {

        if (connected) {

            log.debug(this + " already connected");
        }

        connected = true;

        log.info(this + " connected");
    }

    @Override
    public boolean isConnected() {

        return connected;
    }

    @Override
    public void disconnect() {

        if (!connected) {

            log.debug(this + " already disconnected");
        }

        connected = false;

        log.info(this + " disconnected");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
