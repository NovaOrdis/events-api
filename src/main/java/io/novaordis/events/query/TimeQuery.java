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
import io.novaordis.utilities.time.Timestamp;
import io.novaordis.utilities.time.TimestampImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public static final SimpleDateFormat[] SUPPORTED_FORMATS = {

            new SimpleDateFormat("MM/dd/yy HH:mm:ss")
    };

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private boolean from;
    private boolean to;

    private Timestamp timestamp;
    private SimpleDateFormat format;

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

    // QueryBase overrides ---------------------------------------------------------------------------------------------

    @Override
    public boolean selects(Event e) {

        throw new RuntimeException("selects() NOT YET IMPLEMENTED");
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

            throw new QueryException("unknown timestamp format: '" + ts + "'");
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
