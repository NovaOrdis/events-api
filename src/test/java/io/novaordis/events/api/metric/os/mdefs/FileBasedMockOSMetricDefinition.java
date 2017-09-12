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

import io.novaordis.events.api.event.PropertyFactory;
import io.novaordis.events.api.metric.os.InternalMetricReadingContainer;
import io.novaordis.events.api.metric.os.OSMetricDefinitionBase;
import io.novaordis.utilities.address.OSAddress;
import io.novaordis.utilities.parsing.ParsingException;
import io.novaordis.utilities.parsing.PreParsedContent;

import java.io.File;

/**
 * Use this mock OSMetricDefinition to experiment with metric definitions that create metric values from file content.
 *
 * @see CommandBasedMockOSMetricDefinition
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class FileBasedMockOSMetricDefinition extends OSMetricDefinitionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private File file;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Invoked by reflection.
     */
    @SuppressWarnings("unused")
    public FileBasedMockOSMetricDefinition(PropertyFactory f, OSAddress osMetricSourceAddress) {

        this(FileBasedMockOSMetricDefinition.class.getSimpleName(), f, osMetricSourceAddress, null);
    }

    public FileBasedMockOSMetricDefinition(String id, PropertyFactory f, OSAddress osMetricSourceAddress, File file) {

        super(f, osMetricSourceAddress);

        setId(id);

        this.file = file;
    }

    // Overrides -------------------------------------------------------------------------------------------------------


    @Override
    protected InternalMetricReadingContainer parseLinuxSourceFileContent
            (byte[] content, PreParsedContent previousReading) throws ParsingException {

        //
        // we return the command execution stdout as value of the property, to allow for extra consistency testing
        //
        //return new MockProperty(getId(), commandExecutionStdout);


        throw new RuntimeException("parseLinuxSourceFileContent() NOT YET IMPLEMENTED");
    }

    @Override
    protected InternalMetricReadingContainer parseMacSourceFileContent
            (byte[] content, PreParsedContent previousReading) throws ParsingException {

        throw new RuntimeException("parseMacSourceFileContent() NOT YET IMPLEMENTED");
    }

    @Override
    protected InternalMetricReadingContainer parseWindowsSourceFileContent
            (byte[] content, PreParsedContent previousReading) throws ParsingException {

        throw new RuntimeException("parseWindowsSourceFileContent() NOT YET IMPLEMENTED");
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
