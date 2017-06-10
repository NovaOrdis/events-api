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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
public class RemoteOSTest extends OSSourceBaseTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Override
    public void equalsTest() throws Exception {

        RemoteOS ros = new RemoteOS("ssh://1.2.3.4:55");
        RemoteOS ros2 = new RemoteOS("ssh://1.2.3.4:55");

        assertEquals(ros, ros2);
        assertEquals(ros2, ros2);
    }

    @Override
    public void hashCodeTest() throws Exception {

        RemoteOS ros = new RemoteOS("ssh://1.2.3.4:55");
        assertEquals(ros.hashCode(), ros.getAddress().hashCode());
    }

    /**
     * Will be overridden by the RemoteOSTest, as the execution result is a mock hostname.
     */
    @Test
    @Override
    public void execute_ActualExecutor() throws Exception {

        OSSourceBase oss = getMetricSourceToTest();

        //
        // command will execute and will return non-empty string on all supported OSes
        //
        String stdout = oss.execute("hostname");

        assertEquals("mock-ssh-server", stdout);
    }

    // hasAddress() ----------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected RemoteOS getMetricSourceToTest() throws Exception {

        //
        // we don't test with a real ssh server
        //
        MockSshConnection mc = new MockSshConnection();
        return new RemoteOS(mc);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
