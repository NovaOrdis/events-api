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

import io.novaordis.events.api.event.Event;

import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public abstract class ParserBase implements Parser {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private volatile boolean closed;

    // Constructors ----------------------------------------------------------------------------------------------------

    // Parser implementation -------------------------------------------------------------------------------------------

    @Override
    public List<Event> parse(Long lineNumber, String line) throws ParsingException {

        if (closed) {

            throw new IllegalStateException(this + " is closed");
        }

        return parseInternal(lineNumber, line);
    }

    @Override
    public List<Event> close() throws ParsingException {

        List<Event> result = closeInternal();

        closed = true;

        return result;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * Processes the current line, and return all events that could be completely parsed, accumulated so far. Note that
     * the current line may contain the end of an event that started on a previous line, multiple events, or the
     * beginning of an event that may or may not continue on the next lines.
     *
     * The invocation may return an empty list, but never null.
     *
     * The method will never be invoked on a closed parser instance.
     *
     * @param lineNumber may be null if the line number cannot be provided.
     */
    protected abstract List<Event> parseInternal(Long lineNumber, String line) throws ParsingException;

    /**
     * Processes the remaining accumulated state. The super close() will actually close the parser.
     *
     * The invocation may return an empty list, but never null.
     *
     * @see Parser#close()
     */
    protected abstract List<Event> closeInternal() throws ParsingException;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
