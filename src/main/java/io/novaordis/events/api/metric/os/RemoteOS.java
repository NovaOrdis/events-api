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

/**
 * See https://kb.novaordis.com/index.php/Events-api_Concepts#Remote_OS
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/1/17
 */
public class RemoteOS extends OSSourceBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public RemoteOS(String address) throws Exception {

        if (address == null) {

            throw new IllegalArgumentException("null address");
        }

        initializeSshConnection(address);
    }

    public RemoteOS(SshConnection c) throws Exception {

        if (c == null) {

            throw new IllegalArgumentException("null connection");
        }

        setSshConnection(c);
    }

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public String getAddress() {

        SshConnection connection = (SshConnection)getNativeExecutor();

        if (connection == null) {

            return null;
        }

        return connection.getAddress();
    }

    @Override
    public boolean hasAddress(String address) {

        //
        // TODO more complete implementation to follow
        //

        String thisAddress = getAddress();

        return thisAddress != null && thisAddress.equals(address);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        String address = getAddress();

        if (address == null) {

            return "null";
        }

        return address;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    void setSshConnection(SshConnection c) {

        setNativeExecutor(c);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    private void initializeSshConnection(String address) {

        SshConnection c = null; // build it here

        setSshConnection(c);
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
