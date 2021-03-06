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

package io.novaordis.events.api.metric;

import io.novaordis.utilities.address.AddressException;
import io.novaordis.utilities.address.OSAddress;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/12/17
 */
public class MockOSAddress extends MockAddress implements OSAddress {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockOSAddress() {

        super();
        setHost("mock-host");
    }


    public MockOSAddress(String address) throws AddressException {

        super(address);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // MockAddress overrides -------------------------------------------------------------------------------------------

    @Override
    public MockOSAddress copy() {

        MockOSAddress a = new MockOSAddress();
        a.setProtocol(getProtocol());
        a.setHost(getHost());
        a.setPort(getPort());
        a.setUsername(getUsername());
        a.setPassword(getPassword());
        return a;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
