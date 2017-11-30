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

package io.novaordis.events.api.parser;

import org.junit.Test;

import io.novaordis.events.api.event.GenericEvent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/29/17
 */
public class QueryOnceTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void lifecycle() throws Exception {

        GenericEvent e = new GenericEvent();

        assertFalse(QueryOnce.isQueryOnce(e));

        QueryOnce.set(e, true);

        assertTrue(QueryOnce.isQueryOnce(e));

        QueryOnce.clear(e);

        assertFalse(QueryOnce.isQueryOnce(e));
    }

    @Test
    public void setFalse() throws Exception {

        GenericEvent e = new GenericEvent();

        assertFalse(QueryOnce.isQueryOnce(e));

        QueryOnce.set(e, true);

        assertTrue(QueryOnce.isQueryOnce(e));

        QueryOnce.set(e, false);

        assertFalse(QueryOnce.isQueryOnce(e));
    }


    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
