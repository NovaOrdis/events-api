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

import java.util.List;

import io.novaordis.events.api.event.Event;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/30/17
 */
public class MockQuery implements Query {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private boolean selectsBroken;

    // Constructors ----------------------------------------------------------------------------------------------------

    // Query implementation --------------------------------------------------------------------------------------------

    @Override
    public boolean offerLexicalToken(String literal) throws QueryException {
        throw new RuntimeException("offerLexicalToken() NOT YET IMPLEMENTED");
    }

    @Override
    public Query negate() throws QueryException {
        throw new RuntimeException("negate() NOT YET IMPLEMENTED");
    }

    @Override
    public void compile() throws QueryException {
        throw new RuntimeException("compile() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isCompiled() {
        throw new RuntimeException("isCompiled() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean selects(Event e) {

        if (selectsBroken) {

            throw new RuntimeException("SYNTHETIC");
        }

        throw new RuntimeException("selects() NOT YET IMPLEMENTED");
    }

    @Override
    public List<Event> filter(List<Event> events) {
        throw new RuntimeException("filter() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void breakSelects() {

        this.selectsBroken = true;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
