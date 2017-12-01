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

package io.novaordis.events.query;

import org.junit.Test;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.GenericEvent;
import io.novaordis.events.api.event.GenericTimedEvent;

import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
public class MatchAllTest extends QueryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    // Query Once ------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void selects_QueryOnce() throws Exception {

        // noop, does not apply
    }

    @Test
    public void filter_QueryOnce() throws Exception {

        // noop, does not apply
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void selectsAll() {

        MatchAll q = new MatchAll();

        assertTrue(q.selects(new GenericTimedEvent()));
    }

    @Test
    public void selects_Time_All() {

        MatchAll q = new MatchAll();

        assertTrue(q.selects(-1L));
        assertTrue(q.selects(0L));
        assertTrue(q.selects(1L));
        assertTrue(q.selects(Long.MAX_VALUE));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected MatchAll getQueryToTest() throws Exception {

        return new MatchAll();
    }

    @Override
    protected Event getEventThatMatchesQuery() {

        return new GenericEvent();
    }

    @Override
    protected Event getEventThatDoesNotMatchQuery() {

        return null;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
