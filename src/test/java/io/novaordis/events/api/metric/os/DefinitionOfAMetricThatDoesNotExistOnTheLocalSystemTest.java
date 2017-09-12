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
import io.novaordis.utilities.address.LocalOSAddress;
import io.novaordis.utilities.os.OSType;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/7/17
 */
public class DefinitionOfAMetricThatDoesNotExistOnTheLocalSystemTest extends OSMetricDefinitionTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void parse() throws Exception {

        //
        // noop
        //
    }

    @Test
    @Override
    public void parse_NullRepository() throws Exception {

        //
        // noop
        //
    }

    @Test
    @Override
    public void parseSourceFileContent_ValidLinuxContent() throws Exception {

        //
        // noop
        //
    }

    @Test
    @Override
    public void parseSourceFileContent_ValidMacContent() throws Exception {

        //
        // noop
        //
    }

    @Test
    @Override
    public void parseSourceFileContent_ValidWindowsContent() throws Exception {

        //
        // noop
        //
    }

    // others ----------------------------------------------------------------------------------------------------------

    @Test
    public void getCommand() throws Exception {

        DefinitionOfAMetricThatDoesNotExistOnTheLocalSystem d = getMetricDefinitionToTest();
        assertNull(d.getCommand(OSType.LINUX));
        assertNull(d.getCommand(OSType.MAC));
        assertNull(d.getCommand(OSType.WINDOWS));
    }

    @Override
    public void parseCommandOutput_ValidLinuxOutput() throws Exception {

        //
        // noop - the method will never be called, and if it is, we don't care about the result
        //
    }

    @Override
    public void parseCommandOutput_ValidMacOutput() throws Exception {

        //
        // noop - the method will never be called, and if it is, we don't care about the result
        //
    }

    @Override
    public void parseCommandOutput_ValidWindowsOutput() throws Exception {

        //
        // noop - the method will never be called, and if it is, we don't care about the result
        //
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected DefinitionOfAMetricThatDoesNotExistOnTheLocalSystem getMetricDefinitionToTest() throws Exception {

        PropertyFactory f = new PropertyFactory();
        LocalOSAddress a = new LocalOSAddress();

        return new DefinitionOfAMetricThatDoesNotExistOnTheLocalSystem(f, a);
    }

    @Override
    protected byte[] getValidSourceFileContentToTest(OSType osType, int seed) throws Exception {

        return null;
    }

    @Override
    protected String getValidCommandOutputToTest(OSType osType, int seed) throws Exception {

        throw new RuntimeException("getValidCommandOutputToTest() NOT YET IMPLEMENTED");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
