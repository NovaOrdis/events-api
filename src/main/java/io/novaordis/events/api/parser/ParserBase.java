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

import io.novaordis.events.api.event.EndOfStreamEvent;
import io.novaordis.events.api.event.Event;
import io.novaordis.utilities.parsing.ParsingException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public abstract class ParserBase implements Parser {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private volatile boolean closed;

    private AtomicLong lineNumber;

    // Constructors ----------------------------------------------------------------------------------------------------

    protected ParserBase() {

        lineNumber = new AtomicLong(0);
    }

    // Parser implementation -------------------------------------------------------------------------------------------

    @Override
    public List<Event> parse(String line) throws ParsingException {

        if (closed) {

            throw new IllegalStateException(this + " is closed");
        }

        return parse(lineNumber.incrementAndGet(), line);
    }

    @Override
    public List<Event> close() throws ParsingException {

        //
        // protect against redundant calls, we already returned our EndOfStream on the first close() call
        //

        if (closed) {

            return Collections.emptyList();
        }

        List<Event> result = close(lineNumber.get());

        List<Event> resultWithEndOfStream = new ArrayList<>(result.size() + 1);

        for(Event e: result) {

            if (e instanceof EndOfStreamEvent) {

                throw new IllegalStateException("the sub-class must not return EndOfStream");
            }

            resultWithEndOfStream.add(e);
        }

        resultWithEndOfStream.add(new EndOfStreamEvent());

        closed = true;

        return resultWithEndOfStream;
    }

    @Override
    public long getLineNumber() {

        return lineNumber.get();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return getClass().getSimpleName() + "[" + Integer.toHexString(System.identityHashCode(this)) + "]";
    }

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
     * @param lineNumber the line number, as managed by superclass.
     */
    protected abstract List<Event> parse(long lineNumber, String line) throws ParsingException;

    /**
     * Process the remaining accumulated state. The super close() will actually close the parser and issue the
     * EnoOfStream as the last event in the event stream.
     *
     * The invocation may return an empty list, but never null.
     *
     * Also, the implementation MUST not return an EndOfStream among events, handling EndOfStream is THIS layer's job.
     *
     * @param lineNumber the line number of the last line in the text stream, when close() is called externally.
     *
     * @see Parser#close()
     */
    protected abstract List<Event> close(long lineNumber) throws ParsingException;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
