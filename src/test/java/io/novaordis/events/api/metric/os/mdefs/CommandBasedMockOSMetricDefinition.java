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

/**
 * Use this mock OSMetricDefinition to experiment with metric definitions that create metric values from the output
 * of OS commands.
 *
 * @see FileBasedMockOSMetricDefinition
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class CommandBasedMockOSMetricDefinition extends OSMetricDefinitionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    private static boolean FAIL_IN_CONSTRUCTOR = false;

    public static void setFailInConstructor(boolean b) {

        FAIL_IN_CONSTRUCTOR = b;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private String command;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Invoked by reflection.
     */
    @SuppressWarnings("unused")
    public CommandBasedMockOSMetricDefinition(PropertyFactory f, OSAddress osMetricSourceAddress) {

        this(CommandBasedMockOSMetricDefinition.class.getSimpleName(), f, osMetricSourceAddress, null);
    }

    public CommandBasedMockOSMetricDefinition(String id, PropertyFactory f, OSAddress osMetricSourceAddress, String command) {

        super(f, osMetricSourceAddress);

        setId(id);

        this.command = command;

        if (FAIL_IN_CONSTRUCTOR) {

            throw new RuntimeException("SYNTHETIC");
        }
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
    protected InternalMetricReadingContainer parseSourceFileContent
            (OSType osType, byte[] content, PreParsedContent previousReading) throws ParsingException {

        throw new RuntimeException("parseLinuxSourceFileContent() NOT YET IMPLEMENTED");
    }

    //
    // command-handling overrides --------------------------------------------------------------------------------------
    //

    @Override
    public String getCommand(OSType t) {

        return command;
    }

    @Override
    protected InternalMetricReadingContainer parseCommandOutput
            (OSType osType, String commandExecutionStdout, PreParsedContent previousReading) throws ParsingException {

        //
        // we return the command execution stdout as value of the property, to allow for extra consistency testing
        //

        return new InternalMetricReadingContainer(commandExecutionStdout, null);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
