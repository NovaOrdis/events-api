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

import static org.junit.Assert.assertFalse;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
public class MatchNoneTest extends QueryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    // Query Once ------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void selects_queryOnce() throws Exception {

        // noop, does not apply
    }

    @Test
    public void filter_queryOnce() throws Exception {

        // noop, does not apply
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void selectsNone() {

        MatchNone q = new MatchNone();

        assertFalse(q.selects(new GenericTimedEvent()));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected MatchNone getQueryToTest() throws Exception {

        return new MatchNone();
    }

    @Override
    protected Event getEventThatMatchesQuery() {

        return null;
    }

    @Override
    protected Event getEventThatDoesNotMatchQuery() {

        return new GenericEvent();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
