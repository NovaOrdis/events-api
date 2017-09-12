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

import io.novaordis.events.api.event.PropertyFactory;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.os.OSType;
import io.novaordis.utilities.parsing.ParsingException;
import io.novaordis.utilities.parsing.PreParsedContent;

/**
 * Simulates a metric that does not exits on the system.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/7/17
 */
public class DefinitionOfAMetricThatDoesNotExistOnTheLocalSystem extends OSMetricDefinitionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    protected DefinitionOfAMetricThatDoesNotExistOnTheLocalSystem(PropertyFactory f, Address metricSourceAddress) {

        super(f, metricSourceAddress);

        this.TYPE = Long.class;

        this.LABEL = "TEST";

        this.BASE_UNIT = null;

        this.DESCRIPTION = "test";

        this.LINUX_COMMAND = null;

        this.LINUX_PATTERN = null;

        this.MAC_COMMAND = null;

        this.MAC_PATTERN = null;

        this.WINDOWS_COMMAND = null;

        this.WINDOWS_PATTERN = null;
    }

    // OSMetricDefinitionBase overrides --------------------------------------------------------------------------------

    @Override
    protected InternalMetricReadingContainer parseSourceFileContent
            (OSType osType, byte[] content, PreParsedContent previousReading) throws ParsingException {

        throw new IllegalStateException(this + " cannot be extracted from a file on " + osType);
    }

    @Override
    protected Object parseMacCommandOutput(String commandOutput) throws ParsingException {
        throw new RuntimeException("parseMacCommandOutput() NOT YET IMPLEMENTED");
    }

    @Override
    protected Object parseLinuxCommandOutput(String commandOutput) throws ParsingException {
        throw new RuntimeException("parseLinuxCommandOutput() NOT YET IMPLEMENTED");
    }

    @Override
    protected Object parseWindowsCommandOutput(String commandOutput) throws ParsingException {
        throw new RuntimeException("parseWindowsCommandOutput() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
