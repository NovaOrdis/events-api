/*
 * Copyright (c) 2016 Nova Ordis LLC
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

package io.novaordis.events.api.event;

import io.novaordis.utilities.time.Timestamp;
import io.novaordis.utilities.time.TimestampImpl;

import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/1/16
 */
public class GenericTimedEvent extends GenericEvent implements TimedEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    /**
     * @see Timestamp
     */
    private Timestamp timestamp;

    // Constructors ----------------------------------------------------------------------------------------------------

    public GenericTimedEvent() {

        this((Timestamp)null);
    }

    public GenericTimedEvent(Long timestampUTC) {

        this(timestampUTC == null ? null : new TimestampImpl(timestampUTC));
    }

    /**
     * @see Timestamp
     */
    public GenericTimedEvent(Timestamp timestamp) {

        this.timestamp = timestamp;
    }

    public GenericTimedEvent(List<Property> properties) {

        this(null, properties);
    }

    public GenericTimedEvent(long timestampUTC, List<Property> properties) {

        this(new TimestampImpl(timestampUTC), properties);
    }

    /**
     * @param properties the implementation makes an internal shallow copy.
     *
     * @see Timestamp
     */
    public GenericTimedEvent(Timestamp timestamp, List<Property> properties) {

        super(properties);
        this.timestamp = timestamp;
    }

    // TimedEvent implementation ---------------------------------------------------------------------------------------

    @Override
    public Timestamp getTimestamp() {

        return timestamp;
    }

    @Override
    public Long getTime() {

        if (timestamp == null) {

            return null;
        }

        return timestamp.getTime();
    }

    @Override
    public void setTimestamp(Timestamp timestamp) {

        this.timestamp = timestamp;
    }

    // GenericEvent overrides ------------------------------------------------------------------------------------------

    /**
     * We expose the timestamp as a dedicated property. However, if we are lacking a timestamp, we return null.
     */
    @Override
    public Property getProperty(String name) {

        if (TimedEvent.TIMESTAMP_PROPERTY_NAME.equals(name)) {

            //
            // we call getTimestamp() and not use "timestamp" directly because some sub-classes
            // (io.novaordis.events.api.gc.GCEventBase for example) mess with it.
            //
            // TODO review this and refactor
            //
            Timestamp ts = getTimestamp();

            if (ts == null) {

                return null;
            }

            return new TimestampProperty(ts.getTime());
        }

        return super.getProperty(name);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return getTimestamp() + " event";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
