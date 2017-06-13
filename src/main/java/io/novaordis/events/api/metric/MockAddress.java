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

import io.novaordis.utilities.address.Address;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/12/17
 */
public class MockAddress implements Address {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String literal;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockAddress(String address) {

        this.literal = address;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Address implementation ------------------------------------------------------------------------------------------


    @Override
    public String getProtocol() {

        return "mock";
    }

    @Override
    public void setProtocol(String protocol) {
        throw new RuntimeException("setProtocol() NOT YET IMPLEMENTED");
    }

    @Override
    public String getHost() {
        throw new RuntimeException("getHost() NOT YET IMPLEMENTED");
    }

    @Override
    public Integer getPort() {
        throw new RuntimeException("getPort() NOT YET IMPLEMENTED");
    }

    @Override
    public void setPort(Integer port) {
        throw new RuntimeException("setPort() NOT YET IMPLEMENTED");
    }

    @Override
    public String getUsername() {
        throw new RuntimeException("getUsername() NOT YET IMPLEMENTED");
    }

    @Override
    public char[] getPassword() {
        throw new RuntimeException("getPassword() NOT YET IMPLEMENTED");
    }

    @Override
    public String getLiteral() {

        return literal;
    }

    @Override
    public MockAddress copy() {

        return new MockAddress(getLiteral());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
