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
import io.novaordis.events.api.metric.os.MetricReading;
import io.novaordis.events.api.metric.os.OSMetricDefinitionBase;
import io.novaordis.utilities.address.OSAddress;
import io.novaordis.utilities.os.OSType;
import io.novaordis.utilities.parsing.ParsingException;
import io.novaordis.utilities.parsing.PreParsedContent;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class MockOSMetricDefinition extends OSMetricDefinitionBase {

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
    public MockOSMetricDefinition(PropertyFactory f, OSAddress osMetricSourceAddress) {

        this(MockOSMetricDefinition.class.getSimpleName(), f, osMetricSourceAddress, null);
    }

    public MockOSMetricDefinition(String id, PropertyFactory f, OSAddress osMetricSourceAddress, String command) {

        super(f, osMetricSourceAddress);

        setId(id);

        this.command = command;

        if (FAIL_IN_CONSTRUCTOR) {

            throw new RuntimeException("SYNTHETIC");
        }
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

    @Override
    public String getCommand(OSType t) {

        return command;
    }

    @Override
    protected MetricReading parseLinuxSourceFileContent(byte[] content, PreParsedContent previousReading)
            throws ParsingException {
        throw new RuntimeException("parseLinuxSourceFileContent() NOT YET IMPLEMENTED");
    }

    @Override
    protected MetricReading parseMacSourceFileContent(byte[] content, PreParsedContent previousReading)
            throws ParsingException {
        throw new RuntimeException("parseMacSourceFileContent() NOT YET IMPLEMENTED");
    }

    @Override
    protected MetricReading parseWindowsSourceFileContent(byte[] content, PreParsedContent previousReading)
            throws ParsingException {
        throw new RuntimeException("parseWindowsSourceFileContent() NOT YET IMPLEMENTED");
    }

//    @Override
//    public Property parseCommandOutput(String commandExecutionStdout) {
//
//        //
//        // we return the command execution stdout as value of the property, to allow for extra consistency testing
//        //
//        return new MockProperty(getId(), commandExecutionStdout);
//    }

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
