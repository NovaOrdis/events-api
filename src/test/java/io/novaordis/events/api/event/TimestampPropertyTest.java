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

package io.novaordis.events.api.event;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/1/16
 */
public class TimestampPropertyTest extends PropertyTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void name() throws Exception {

        TimestampProperty p = getPropertyToTest("test");

        assertEquals(TimedEvent.TIMESTAMP_PROPERTY_NAME, p.getName());
    }

    @Test
    public void externalizeType() throws Exception {

        Property p = getPropertyToTest("hj46hHT3");
        String s = p.externalizeType();
        assertNotNull(s);
        assertEquals(TimedEvent.TIMESTAMP_PROPERTY_NAME, s);
    }

    @Test
    @Override
    public void setName() throws Exception {

        PropertyBase p = getPropertyToTest("some name");

        assertEquals(TimedEvent.TIMESTAMP_PROPERTY_NAME, p.getName());

        p.setName("another name");

        assertEquals("another name", p.getName());
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected TimestampProperty getPropertyToTest(String name) {

        //
        // we ignore the name
        //
        return new TimestampProperty(1000L);
    }

    @Override
    protected Long getAppropriateValueForPropertyToTest() {

        return 1000L;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
