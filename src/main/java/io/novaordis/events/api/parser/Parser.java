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
import io.novaordis.utilities.parsing.ParsingException;

import java.util.List;

/**
 * A parser that expects events to span multiple lines of text stream, as it is the case with garbage collection
 * logs, where a garbage collection event can be reported on successive lines, or a log4j log, where an event may
 * include multi-line stack traces. The implementations of this interface must also handle the case when a single
 * line contains multiple events.
 *
 * An implementation should first consider extending ParserBase, where some of the expected generic behavior is already
 * implemented, and implement parseInternal() and closeInternal().
 *
 * The implementations must count lines internally, and rely on the fact that *all* lines from the external text
 * stream are being provided for processing via parse().
 *
 * @see ParserBase
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public interface Parser {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Processes the current line, and return all events that could be completely parsed, accumulated so far. Note that
     * the current line may contain the end of an event that started on a previous line, multiple events, or the
     * beginning of an event that may or may not continue on the next lines.
     *
     * The invocation may return an empty list, but never null.
     *
     * @exception IllegalStateException if invoked on a closed parser.
     */
    List<Event> parse(String line) throws ParsingException;

//    /**
//     * TODO there was just a single situation when we needed this, and we could handle it in implementation, so we
//     * decided to back off the change. If we ever need this again, we'll reconsider.
//     *
//     * @return all events accumulated so far, without closing the parser.
//     *
//     * @see Parser#close()
//     */
//    List<Event> flush() throws ParsingException;

    /**
     * Processes the remaining accumulated state and closes the parser. A parser that was closed cannot be re-used,
     * an attempt to invoke parse() on it will throw IllegalStateException. The last event returned by the close()
     * invocation is an EndOfStreamEvent.
     *
     * The invocation may return an empty list (if it was redundantly invoked after the parser was closed), but never
     * null.
     */
    List<Event> close() throws ParsingException;

    /**
     * @return the 1-based line number of the last successfully or unsuccessfully parsed line. Return 0 if no lines
     * were processed yet. If the parser is closed, returns the number of the last line in the text stream.
     */
    long getLineNumber();

}
