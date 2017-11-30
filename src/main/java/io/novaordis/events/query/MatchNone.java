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

import java.util.Collections;
import java.util.List;

import io.novaordis.events.api.event.Event;

/**
 * A query that selects no event.
 *
 * Equivalent with NullQuery.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
public class MatchNone extends ExpressionElementBase implements Query {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Query implementation --------------------------------------------------------------------------------------------

    @Override
    public boolean selects(Event e) {

        if (e == null) {

            throw new IllegalArgumentException("null event");
        }

        return false;
    }

    @Override
    public List<Event> filter(List<Event> events) {

        if (events == null) {

            throw new IllegalArgumentException("null event list");
        }

        return Collections.emptyList();
    }

    @Override
    public Query negate() throws QueryException {

        return new MatchAll();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
