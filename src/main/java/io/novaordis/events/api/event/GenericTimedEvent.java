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

import java.util.ArrayList;
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

    /**
     * One of these properties MUST be a TimestampProperty, otherwise the constructor will throw an
     * IllegalArgumentException.
     *
     * @param properties must contain at least one TimestampProperty.
     *
     * @exception IllegalArgumentException if the properties do not include a TimestampProperty.
     */
    public GenericTimedEvent(List<Property> properties) {

        this(null, insureTimestampPropertyExists(properties));
    }

    public GenericTimedEvent(long timestampUTC, List<Property> properties) {

        this(new TimestampImpl(timestampUTC), properties);
    }

    /**
     * @param properties the implementation makes an internal shallow copy.
     *
     * @see Timestamp
     *
     * @exception IllegalArgumentException if the timestamp is set both with the direct argument and with a property
     * and the values conflict
     */
    public GenericTimedEvent(Timestamp timestamp, List<Property> properties) {

        super(properties);

        //
        // protection against the situation when the timestamp is set with a property and as a direct argument
        // and they have different values.
        //

        if (timestamp == null) {

            //
            // noop
            //

            return;
        }

        if (this.timestamp != null && this.timestamp.getTime() != timestamp.getTime()) {

            throw new IllegalArgumentException(
                    "conflicting timestamp values: " + timestamp + ", " + timestamp.getTime());
        }

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
     * Override required to handle TimestampProperties. Everything else should be handled by superclass.
     */
    @Override
    public Property setProperty(Property p) {

        if (!(p instanceof TimestampProperty)) {

            return super.setProperty(p);
        }

        //
        // handling timestamp property
        //

        TimestampProperty tp = (TimestampProperty)p;

        Timestamp previous = timestamp;

        setTimestamp(new TimestampImpl((Long)tp.getValue()));

        if (previous == null) {

            return null;
        }

        return new TimestampProperty(previous.getTime());
    }

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

    /**
     * We override this because the timestamp is also exposed as a property, so we need to generate one and insert
     * it among the properties maintained by superclass.
     *
     * @return a shallow copy of the internal storage.
     */
    @Override
    public List<Property> getProperties() {

        List<Property> shallow = super.getProperties();

        //
        // TODO currently we do not account for the case where the timestamp is on a different position than 0
        // in the list; we will need to return to this
        //

        Long time = getTime();

        if (time == null) {

            return shallow;

        }


        List<Property> result = new ArrayList<>(shallow.size() + 1);

        result.add(new TimestampProperty(time));
        result.addAll(shallow);

        return result;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return getTimestamp() + " event";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    /**
     * @throws IllegalArgumentException if no TimestampProperty exists
     */
    private static List<Property> insureTimestampPropertyExists(List<Property> properties)
            throws IllegalArgumentException {

        for(Property p: properties) {

            if (p instanceof TimestampProperty) {

                return properties;
            }
        }

        throw new IllegalArgumentException("no timestamp property found");
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
