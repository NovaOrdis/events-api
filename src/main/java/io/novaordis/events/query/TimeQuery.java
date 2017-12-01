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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.TimedEvent;
import io.novaordis.events.api.parser.QueryOnce;
import io.novaordis.utilities.time.Timestamp;
import io.novaordis.utilities.time.TimestampImpl;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/23/17
 */
public class TimeQuery extends QueryBase {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(TimeQuery.class);

    public static final String FROM_KEYWORD = "from:";
    public static final String TO_KEYWORD = "to:";

    //
    // listed in the descending order of preference
    //

    //
    // TODO refactor, not thread safe
    //
    public static final SimpleDateFormat[] SUPPORTED_FORMATS = {

            new SimpleDateFormat("MM/dd/yy HH:mm:ss")
    };

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private boolean from;
    private boolean to;

    private Timestamp timestamp;
    private SimpleDateFormat format;

    private boolean compiled = false;

    // Constructors ----------------------------------------------------------------------------------------------------

    public TimeQuery(String keywordAndPossiblyValue) throws QueryException {

        if (keywordAndPossiblyValue == null) {

            throw new IllegalArgumentException("null keyword");
        }

        String timestampAsString = null;

        if (keywordAndPossiblyValue.startsWith(FROM_KEYWORD)) {

            from = true;

            if (keywordAndPossiblyValue.length() != FROM_KEYWORD.length()) {

                timestampAsString = keywordAndPossiblyValue.substring(FROM_KEYWORD.length());
            }

        }
        else if (keywordAndPossiblyValue.startsWith(TO_KEYWORD)) {

            to = true;

            if (keywordAndPossiblyValue.length() != TO_KEYWORD.length()) {

                timestampAsString = keywordAndPossiblyValue.substring(TO_KEYWORD.length());
            }
        }
        else {

            throw new QueryException("unknown time query keyword: " + keywordAndPossiblyValue);
        }

        if (timestampAsString != null) {

            setTimestamp(timestampAsString);
        }
    }

    public TimeQuery(String keyword, Long timestamp) throws QueryException {

        this(keyword);
        setTimestamp(timestamp);
    }

    // QueryBase overrides ---------------------------------------------------------------------------------------------

    @Override
    public boolean offerLexicalToken(String literal) throws QueryException {

        if (timestamp == null) {

            //
            // we are expecting a timestamp
            //

            setTimestamp(literal);

            //
            // timestamp was correctly parsed, thus accepted
            //

            return true;
        }

        return false;
    }

    @Override
    public TimeQuery negate() throws QueryException {

        //
        // TODO: Negating a query is a problematic concept to implement. Instead of negating the query, evaluate it and
        // then negate the result. Get rid of negation the first time I need to implement negate() on any remaining
        // queries.
        //

        throw new RuntimeException("negate() NOT YET IMPLEMENTED");
    }

    @Override
    public void compile() throws QueryException {

        //
        // makes sure we've been fed a timestamp
        //

        if (timestamp == null) {

            throw new QueryException("missing timestamp");

        }

        compiled = true;
    }

    @Override
    public boolean isCompiled() {

        return compiled;
    }

    // Query implementation --------------------------------------------------------------------------------------------

    /**
     * TimeQueries do not select non-timed events, or time events with a null timestamp, as they don't have the
     * information necessary to make a decision.
     */
    @Override
    public boolean selects(Event e) {

        if (e == null) {

            throw new IllegalArgumentException("null event");
        }

        if (QueryOnce.isQueryOnce(e)) {

            return true;
        }

        if (!e.isTimed()) {

            return false;
        }

        TimedEvent te = (TimedEvent)e;

        Long eventTime = te.getTime();

        if (eventTime == null) {

            return false;
        }

        //
        // timestamp cannot be null, if it is, we'll get NPE which will indicate our own implementation error
        //

        long queryTime = timestamp.getTime();

        if (from) {

            return eventTime >= queryTime;
        }
        else if (to) {

            return eventTime <= queryTime;
        }
        else {

            throw new IllegalArgumentException(this + " was not property initialized");
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setTimestamp(String ts) throws QueryException {

        if (ts == null) {

            throw new IllegalArgumentException("null timestamp");
        }

        this.timestamp = null;

        //
        // try all supported formats and only fail if none is found
        //
        for(SimpleDateFormat f: SUPPORTED_FORMATS) {

            try {

                Date d = f.parse(ts);

                this.timestamp = new TimestampImpl(d.getTime());

                //
                // first match is preferred
                //

                this.format = f;

                break;
            }
            catch (ParseException e) {

                //
                // keep trying
                //

                if (log.isTraceEnabled()) {

                    log.trace("heuristics failure, timestamp " + ts + " does not match possible format " + f.toPattern());
                }
            }
        }

        if (timestamp == null) {

            throw new QueryException("unknown timestamp format or invalid timestamp: '" + ts + "'");
        }
    }

    public void setTimestamp(long posixTimeMs) throws QueryException {

        this.timestamp = new TimestampImpl(posixTimeMs);
    }

    /**
     * @return the timestamp as POSIX time expressed in millisecond, or null if it was not set
     */
    public Long getTimestamp() {

        if (timestamp == null) {

            return null;
        }

        return timestamp.getTime();
    }

    /**
     * The format in which the timestamp was specified. May be null.
     */
    public SimpleDateFormat getFormat() {

        return format;
    }

    public boolean isFrom() {

        return from;
    }

    public boolean isTo() {

        return to;
    }

    @Override
    public String toString() {

        String s;

        if (from) {

            s = FROM_KEYWORD;
        }
        else if (to) {

            s = TO_KEYWORD;
        }
        else {

            return "UNINITIALIZED TimeQuery";
        }

        String ts;

        if (format == null) {

            ts = "" + this.timestamp;
        }
        else {

            if (timestamp == null) {

                ts = " UNINITIALIZED";
            }
            else {

                ts = format.format(timestamp.getTime());
            }
        }

        return s + " " + ts;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
