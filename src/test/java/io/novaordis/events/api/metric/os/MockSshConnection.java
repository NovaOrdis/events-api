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

import io.novaordis.ssh.SshConnection;
import io.novaordis.utilities.os.NativeExecutionException;
import io.novaordis.utilities.os.NativeExecutionResult;

import java.io.File;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/8/17
 */
public class MockSshConnection implements SshConnection {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String address = "mock-ssh-server";
    private MockNativeExecutor delegate;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockSshConnection() {

        this.delegate = new MockNativeExecutor();

        //
        // we teach the ssh server at the other end to respond to "hostname"
        //

        delegate.putNativeExecutionResult("hostname", new NativeExecutionResult(0, address, null, true, true));
    }

    // SshConnection implementation ------------------------------------------------------------------------------------

    @Override
    public NativeExecutionResult execute(String command) throws NativeExecutionException {

        return delegate.execute(command);
    }

    @Override
    public String getAddress() {

        return address;
    }

    @Override
    public NativeExecutionResult execute(File directory, String command) throws NativeExecutionException {

        throw new RuntimeException("execute() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
