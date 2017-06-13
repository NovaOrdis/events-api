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

package io.novaordis.events.api.metric.os.mdefs;

import io.novaordis.events.api.event.MockProperty;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.metric.MockMetricDefinition;
import io.novaordis.events.api.metric.os.OSMetricDefinition;
import io.novaordis.utilities.address.OSAddress;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class MockOSMetricDefinition extends MockMetricDefinition implements OSMetricDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String command;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Invoked by reflection.
     */
    @SuppressWarnings("unused")
    public MockOSMetricDefinition(OSAddress osMetricSourceAddress) {

        this(MockOSMetricDefinition.class.getSimpleName(), osMetricSourceAddress, null);
    }

    public MockOSMetricDefinition(String id, OSAddress osMetricSourceAddress, String command) {

        super(osMetricSourceAddress);

        setId(id);

        this.command = command;
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

    @Override
    public String getCommand() {

        return command;
    }

    @Override
    public String getLinuxCommand() {
        throw new RuntimeException("getLinuxCommand() NOT YET IMPLEMENTED");
    }

    @Override
    public String getMacCommand() {
        throw new RuntimeException("getMacCommand() NOT YET IMPLEMENTED");
    }

    @Override
    public String getWindowsCommand() {
        throw new RuntimeException("getWindowsCommand() NOT YET IMPLEMENTED");
    }

    @Override
    public Property parseCommandOutput(String commandExecutionStdout) {

        //
        // we return the command execution stdout as value of the property, to allow for extra consistency testing
        //
        return new MockProperty(getId(), commandExecutionStdout);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
