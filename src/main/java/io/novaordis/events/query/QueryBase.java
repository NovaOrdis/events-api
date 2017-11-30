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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.novaordis.events.api.event.Event;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/17
 */
public abstract class QueryBase extends ExpressionElementBase implements Query {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private boolean validated;

    // Constructors ----------------------------------------------------------------------------------------------------

    // Query implementation --------------------------------------------------------------------------------------------

    /**
     * May be overridden by subclasses for efficiency.
     */
    @Override
    public List<Event> filter(List<Event> events) {

        if (events == null) {

            throw new IllegalArgumentException("null event list");
        }

        if  (events.isEmpty()) {

            return events;
        }

        List<Event> filtered = null;

        for(Event e: events) {

            if (selects(e)) {

                if (filtered == null) {

                    filtered = new ArrayList<>();
                }

                filtered.add(e);
            }
        }

        if (filtered == null) {

            return Collections.emptyList();
        }
        else {

            return filtered;
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    /**
     * Gives a chance to a query instance that may be configured by successive arguments to complain if it did not
     * receive its mandatory arguments. This method should be invoked on all queries at the end of the argument
     * processing cycle.
     *
     * @throws QueryException if mandatory configuration arguments are missing or validation fails in some other way.
     */
    void validate() throws QueryException {

        validate(validated);

        //
        // the invocation returned without throwing an exception, hence we are valid
        //
        validated = true;
    }

    boolean wasValidated() {

        return validated;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * Called on the implementations to do the actual validation.
     *
     * @param validated whether the query was already validated
     */
    protected abstract void validate(boolean validated) throws QueryException;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
