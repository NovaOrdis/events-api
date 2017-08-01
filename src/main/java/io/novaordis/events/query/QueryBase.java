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

import io.novaordis.events.api.event.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/17
 */
public abstract class QueryBase implements Query {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

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

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
