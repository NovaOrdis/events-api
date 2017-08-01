/*
 * Copyright (c) 2016 Nova Ordis LLC
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
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/1/16
 */
public abstract class TimedEventTest extends EventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // getTime() -------------------------------------------------------------------------------------------------------

    @Test
    public void timestamp_NullTimestamp() throws Exception {

        TimedEvent te = getEventToTest(null);
        assertNull(te.getTime());
    }

    @Test
    public void timestamp() throws Exception {

        TimedEvent te = getEventToTest(1L);
        assertEquals(1L, te.getTime().longValue());
    }

    // getProperty() ---------------------------------------------------------------------------------------------------

    /**
     * The timestamp should be accessible by its conventional property name.
     */
    @Test
    public void getProperty_timestamp() throws Exception {


        TimedEvent te = getEventToTest(125L);

        te.getProperty(TimedEvent.TIMESTAMP_PROPERTY_NAME);


    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected TimedEvent getEventToTest() throws Exception {
        return getEventToTest(0L);
    }

    protected abstract TimedEvent getEventToTest(Long timestamp) throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
