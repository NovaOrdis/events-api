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

package io.novaordis.ssh;

import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressException;
import io.novaordis.utilities.address.OSAddressImpl;
import io.novaordis.utilities.os.NativeExecutionException;
import io.novaordis.utilities.os.NativeExecutionResult;

import java.io.File;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/8/17
 */
public class SshConnectionImpl implements SshConnection {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Address address;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param address the address, including optionally the ssh:// protocol
     */
    public SshConnectionImpl(String address) throws AddressException {

        this.address = new OSAddressImpl(address);
        this.address.setProtocol("ssh");
    }

    // NativeExecutor implementation -----------------------------------------------------------------------------------

    @Override
    public NativeExecutionResult execute(String command) throws NativeExecutionException {
        throw new RuntimeException("execute() NOT YET IMPLEMENTED");
    }

    @Override
    public NativeExecutionResult execute(File directory, String command) throws NativeExecutionException {
        throw new RuntimeException("execute() NOT YET IMPLEMENTED");
    }

    // SshConnection implementation ------------------------------------------------------------------------------------

    @Override
    public Address getAddress() {

        return address;
    }

    @Override
    public void connect() {
        throw new RuntimeException("connect() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isConnected() {
        throw new RuntimeException("isConnected() NOT YET IMPLEMENTED");
    }

    @Override
    public void disconnect() {
        throw new RuntimeException("disconnect() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
