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
import io.novaordis.utilities.address.AddressImpl;

/**
 * It correctly implements equals() and hashCode(): two MockAddresses based on the same string are equal.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/12/17
 */
public class MockAddress extends AddressImpl {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockAddress() {

        super();

        setHost("mock-address");
    }

    public MockAddress(String address) throws AddressException {

        super(address);
    }

    // Address implementation ------------------------------------------------------------------------------------------

    @Override
    public String getProtocol() {

        return "mock";
    }

    @Override
    public String getLiteral() {

        return super.getLiteral();
    }

    @Override
    public MockAddress copy() {

        MockAddress ma = new MockAddress();
        ma.setHost(getHost());
        return ma;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return getLiteral() == null ? "UNINITIALIZED" : getLiteral();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
