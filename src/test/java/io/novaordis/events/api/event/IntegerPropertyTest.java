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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/1/16
 */
public class IntegerPropertyTest extends PropertyTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(IntegerPropertyTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void value() throws Exception {

        IntegerProperty sp = new IntegerProperty("test-name", 1);

        assertEquals("test-name", sp.getName());
        assertEquals(1, sp.getValue());
        assertEquals(1, sp.getInteger().intValue());
        assertEquals(Integer.class, sp.getType());
    }

    @Test
    public void fromString_InvalidValue() throws Exception {

        IntegerProperty ip = new IntegerProperty("test");

        try {
            ip.fromString("not an integer");
            fail("should have thrown Exception");
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void externalizeValue_IntegerProperty() throws Exception {

        IntegerProperty ip = new IntegerProperty("test-name", 1);
        assertEquals("1", ip.externalizeValue());
    }

    @Test
    public void externalizeType_IntegerProperty() throws Exception {

        IntegerProperty ip = new IntegerProperty("test-name", 1);
        assertEquals("test-name", ip.externalizeType());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected IntegerProperty getPropertyToTest(String name) {
        return new IntegerProperty(name, 1);
    }

    @Override
    protected Integer getAppropriateValueForPropertyToTest() {

        return 1;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
