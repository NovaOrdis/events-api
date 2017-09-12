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
import io.novaordis.events.api.event.PropertyFactory;
import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.events.api.metric.os.InternalMetricReadingContainer;
import io.novaordis.events.api.metric.os.OSMetricDefinitionBase;
import io.novaordis.utilities.address.OSAddress;
import io.novaordis.utilities.os.OSType;
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
    protected Property getPropertyInstance(String id, Class c, MeasureUnit u) {

        //
        // needed to produce "blank" properties
        //
        return new MockProperty(id);
    }

    //
    // file content handling overrides ---------------------------------------------------------------------------------
    //

    @Override
    public File getSourceFile(OSType osType) {

        return file;
    }

    @Override
    protected InternalMetricReadingContainer parseSourceFileContent
            (OSType osType, byte[] content, PreParsedContent previousReading) throws ParsingException {

        return parseMockFileContent(content, previousReading);
    }

    //
    // command-handling overrides --------------------------------------------------------------------------------------
    //

    @Override
    protected InternalMetricReadingContainer parseCommandOutput
            (OSType osType, String commandExecutionStdout, PreParsedContent previousReading) throws ParsingException {

        throw new RuntimeException("parseCommandOutput() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private InternalMetricReadingContainer parseMockFileContent(byte[] content, PreParsedContent preParsedContent) {

        return new InternalMetricReadingContainer(new String(content), preParsedContent);
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
