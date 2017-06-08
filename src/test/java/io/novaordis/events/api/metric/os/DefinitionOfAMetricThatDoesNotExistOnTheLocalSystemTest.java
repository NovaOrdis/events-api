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

    @Test
    public void getCommand_Linux() throws Exception {

        DefinitionOfAMetricThatDoesNotExistOnTheLocalSystem d = getMetricDefinitionToTest();

        try {

            //
            // set the "current" OS to Linux
            //

            OSType.current = OSType.LINUX;

            assertNull(d.getCommand());
        }
        finally {

            //
            // restore the "current" system
            //

            OSType.reset();
        }
    }

    @Test
    public void getCommand_Mac() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        try {

            //
            // set the "current" OS to Mac
            //

            OSType.current = OSType.MAC;

            assertNull(d.getCommand());
        }
        finally {

            //
            // restore the "current" system
            //

            OSType.reset();
        }
    }

    @Test
    public void getCommand_Windows() throws Exception {

        OSMetricDefinition d = (OSMetricDefinition)getMetricDefinitionToTest();

        try {

            //
            // set the "current" OS to Windows
            //

            OSType.current = OSType.WINDOWS;

            assertNull(d.getCommand());
        }
        finally {

            //
            // restore the "current" system
            //

            OSType.reset();
        }
    }

    @Test
    @Override
    public void parseLinuxCommandOutput_InvalidReading() throws Exception {

        //
        // noop - the method will never be called, and if it is, we don't care about the result
        //
    }

    @Test
    @Override
    public void parseMacCommandOutput_InvalidReading() throws Exception {

        //
        // noop - the method will never be called, and if it is, we don't care about the result
        //
    }

    @Test
    @Override
    public void parseWindowsCommandOutput_InvalidReading() throws Exception {

        //
        // noop - the method will never be called, and if it is, we don't care about the result
        //
    }

    @Test
    @Override
    public void parseCommandOutput_InvalidReading_Linux() throws Exception {

        //
        // noop - the method will never be called, and if it is, we don't care about the result
        //
    }

    @Test
    @Override
    public void parseCommandOutput_InvalidReading_Mac() throws Exception {

        //
        // noop - the method will never be called, and if it is, we don't care about the result
        //
    }

    @Test
    @Override
    public void parseCommandOutput_InvalidReading_Windows() throws Exception {

        //
        // noop - the method will never be called, and if it is, we don't care about the result
        //
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

        return new DefinitionOfAMetricThatDoesNotExistOnTheLocalSystem(new LocalOS());
    }


    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
