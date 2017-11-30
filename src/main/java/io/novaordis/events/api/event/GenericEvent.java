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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.novaordis.events.api.measure.MeasureUnit;

/**
 * A generic event, contains an arbitrary number of properties.
 *
 * It can be used as such, or it can be subclassed by more specialized events.
 *
 * The current implementation is not thread safe.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/1/16
 */
public class GenericEvent implements Event {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(GenericEvent.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private List<Property> properties;

    // Constructors ----------------------------------------------------------------------------------------------------

    public GenericEvent() {

        this(null, null);
    }

    /**
     * @param lineNumber may be null.
     */
    public GenericEvent(Long lineNumber) {

        this(lineNumber, null);
    }

    /**
     * @param properties the order matters, and it will be preserved as the event is processed downstream. The
     *                   implementation makes an internal shallow copy. Note that TimestampProperties are not allowed
     *                   among these properties, to eliminate confusion. If there's a need to specify a
     *                   TimestampProperty, use a TimedEvent instead.
     *
     * @exception IllegalArgumentException if a TimestampProperty is found.
     */
    public GenericEvent(List<Property> properties) {

        this(null, properties);
    }

    /**
     * @param properties the order matters, and it will be preserved as the event is processed downstream. The
     *                   implementation makes an internal shallow copy. Note that TimestampProperties are not allowed
     *                   among these properties, to eliminate confusion. If there's a need to specify a
     *                   TimestampProperty, use a TimedEvent instead.
     *
     * @exception IllegalArgumentException if a TimestampProperty is found.
     */
    public GenericEvent(Property... properties) {

        this(null, Arrays.asList(properties));
    }

    /**
     * @param lineNumber may be null.
     *
     * @param properties the order matters, and it will be preserved as the event is processed downstream. The
     *                   implementation makes an internal shallow copy. May be null, in which case will be ignored. Note
     *                   that TimestampProperties are not allowed among these properties, to eliminate confusion. If
     *                   there's a need to specify a TimestampProperty, use a TimedEvent instead.
     *
     * @exception IllegalArgumentException if a TimestampProperty is found.
     */
    public GenericEvent(Long lineNumber, List<Property> properties) {

        this.properties = new ArrayList<>();

        if (lineNumber != null) {

            setLineNumber(lineNumber);
        }

        if (properties != null) {

            //noinspection Convert2streamapi
            for (Property p : properties) {

                setProperty(p);
            }
        }
    }

    // Event implementation --------------------------------------------------------------------------------------------

    /**
     * Returns a shallow copy of the internal storage.
     */
    @Override
    public List<Property> getProperties() {

        if (properties == null || properties.isEmpty()) {

            return Collections.emptyList();
        }

        return new ArrayList<>(properties);
    }

    /**
     * Returns a shallow copy of the internal storage.
     */
    @Override
    public List<Property> getProperties(Class type) {

        if (type == null) {

            throw new IllegalArgumentException("null type");
        }

        if (properties.isEmpty()) {

            return Collections.emptyList();
        }

        List<Property> result = null;

        for(Property p: properties) {

            //noinspection unchecked
            if (type.isAssignableFrom(p.getType())) {

                if (result == null) {

                    result = new ArrayList<>();
                }

                result.add(p);
            }
        }

        if (result != null) {

            return result;
        }

        return Collections.emptyList();
    }


    @Override
    public Property getProperty(String name) {

        if (name == null)  {

            throw new IllegalArgumentException("null property name");
        }

        for (Property p : properties) {

            if (name.equals(p.getName())) {

                return p;
            }
        }

        return null;
    }

    @Override
    public Property getProperty(int i) {

        if (i < 0) {

            throw new IllegalArgumentException("invalid property index: " + i);
        }

        if (i >= properties.size()) {

            return null;
        }

        return properties.get(i);
    }

    @Override
    public void clearProperties() {

        properties.clear();
    }

    @Override
    public Property removeProperty(String name, Class type) {

        if (name == null) {

            throw new IllegalArgumentException("null name");
        }

        if (type == null) {

            throw new IllegalArgumentException("null type");
        }

        int index = -1;

        for(Property p: properties) {

            index ++;

            if (p.getName().equals(name)) {

                Class propertyType = p.getType();

                if (propertyType == null) {

                    continue;
                }

                //noinspection unchecked
                if (type.isAssignableFrom(propertyType)) {

                    //
                    // do remove
                    //

                    return properties.remove(index);
                }
            }
        }

        return null;
    }

    // Convenience typed accessors/mutators ----------------------------------------------------------------------------

    // StringProperty Accessors/Mutators -------------------------------------------------------------------------------

    @Override
    public StringProperty setStringProperty(String name, String value) {

        if (value == null) {

            return removeStringProperty(name);
        }
        else {

            return (StringProperty)setProperty(new StringProperty(name, value));
        }
    }

    @Override
    public StringProperty getStringProperty(String name) {

        Property p = getProperty(name);

        if (p != null && p instanceof StringProperty) {
            return (StringProperty)p;
        }

        return null;
    }

    @Override
    public StringProperty removeStringProperty(String name) {

        return (StringProperty)removeProperty(name, String.class);
    }

    // EventProperty Accessors/Mutators --------------------------------------------------------------------------------

    @Override
    public EventProperty setEventProperty(String name, Event value) {

        if (value == null) {

            return removeEventProperty(name);
        }
        else {

            return (EventProperty)setProperty(new EventProperty(name, value));
        }
    }

    @Override
    public EventProperty getEventProperty(String name) {

        Property p = getProperty(name);

        if (p != null && p instanceof EventProperty) {
            return (EventProperty)p;
        }

        return null;
    }

    @Override
    public EventProperty removeEventProperty(String name) {

        return (EventProperty)removeProperty(name, Event.class);
    }

    // Long accessors/mutators -----------------------------------------------------------------------------------------

    @Override
    public LongProperty getLongProperty(String name) {

        Property p = getProperty(name);

        if (p != null && p instanceof LongProperty) {

            return (LongProperty)p;
        }

        return null;
    }

    @Override
    public LongProperty setLongProperty(String name, Long value) {

        if (value == null) {

            return removeLongProperty(name);
        }
        else {

            return (LongProperty)setProperty(new LongProperty(name, value));
        }
    }

    @Override
    public LongProperty removeLongProperty(String name) {

        return (LongProperty)removeProperty(name, Long.class);
    }

    // IntegerProperty Accessors/Mutators ------------------------------------------------------------------------------

    @Override
    public IntegerProperty getIntegerProperty(String name) {

        Property p = getProperty(name);

        if (p != null && p instanceof IntegerProperty) {

            return (IntegerProperty)p;
        }

        return null;
    }

    @Override
    public IntegerProperty setIntegerProperty(String name, Integer value) {

        if (value == null) {

            return removeIntegerProperty(name);
        }
        else {

            return (IntegerProperty)setProperty(new IntegerProperty(name, value));
        }
    }

    @Override
    public IntegerProperty removeIntegerProperty(String name) {

        return (IntegerProperty)removeProperty(name, Integer.class);
    }

    // FloatProperty Accessors/Mutators --------------------------------------------------------------------------------

    @Override
    public FloatProperty getFloatProperty(String name) {

        Property p = getProperty(name);

        if (p != null && p instanceof FloatProperty) {

            return (FloatProperty)p;
        }

        return null;
    }

    @Override
    public FloatProperty setFloatProperty(String name, Float value) {

        if (value == null) {

            return removeFloatProperty(name);
        }
        else {

            return (FloatProperty)setProperty(new FloatProperty(name, value));
        }
    }

    @Override
    public FloatProperty removeFloatProperty(String name) {

        return (FloatProperty)removeProperty(name, Float.class);
    }

    // BooleanProperty Accessors/Mutators ------------------------------------------------------------------------------

    @Override
    public BooleanProperty getBooleanProperty(String name) {

        Property p = getProperty(name);

        if (p != null && p instanceof BooleanProperty) {
            return (BooleanProperty)p;
        }

        return null;
    }

    @Override
    public BooleanProperty setBooleanProperty(String name, boolean value) {

        return (BooleanProperty)setProperty(new BooleanProperty(name, value));
    }

    @Override
    public BooleanProperty removeBooleanProperty(String name) {

        return (BooleanProperty)removeProperty(name, Boolean.class);
    }

    // MapProperty Accessors/Mutators ----------------------------------------------------------------------------------

    @Override
    public MapProperty getMapProperty(String name) {

        Property p = getProperty(name);

        if (p != null && p instanceof MapProperty) {
            return (MapProperty)p;
        }

        return null;
    }

    @Override
    public ListProperty getListProperty(String name) {

        Property p = getProperty(name);

        if (p != null && p instanceof ListProperty) {
            return (ListProperty)p;
        }

        return null;
    }

    @Override
    public boolean isTimed() {

        return false;
    }

    /**
     * @exception IllegalArgumentException if the property is a timestamp property. Timestamp properties are not
     * allowed for non-timed events.
     */
    @Override
    public Property setProperty(Property property) {

        if (property == null) {

            throw new IllegalArgumentException("null property");
        }

        if (property instanceof TimestampProperty) {

            throw new IllegalArgumentException("timestamp property not allowed on a non-timed event");
        }

        String propertyName = property.getName();

        //
        // look to see whether a property with the same name exists already
        //

        Property existent = null;
        int existentIndex = -1;

        for(Property p: properties) {

            existentIndex++;

            if (propertyName.equals(p.getName())) {

                existent = p;
                break;
            }
        }

        //
        // if it is a MapProperty, merge contents
        //

        if (property instanceof MapProperty) {

            if (existent instanceof MapProperty) {

                //
                // merge instead of replacing
                //

                ((MapProperty)existent).getMap().putAll(((MapProperty) property).getMap());
                return existent;
            }
        }

        if (existent != null) {

            //
            // replace
            //

            properties.set(existentIndex, property);
            return existent;
        }

        //
        // add
        //

        properties.add(property);
        return null;
    }

    @Override
    public Long getLineNumber() {

        Property p = getProperty(Event.LINE_PROPERTY_NAME);
        if (p == null) {

            return null;
        }

        if (!(p instanceof LongProperty)) {

            log.warn("\"" + Event.LINE_PROPERTY_NAME + "\" exists, but it is not a Long: " + p.getValue());
            return null;
        }

        LongProperty lp = (LongProperty)p;
        return lp.getLong();
    }

    @Override
    public void setLineNumber(Long lineNumber) {

        if (lineNumber == null) {

            removeLongProperty(Event.LINE_PROPERTY_NAME);
        }
        else {

            setLongProperty(Event.LINE_PROPERTY_NAME, lineNumber);
        }
    }

    @Override
    public String getRawRepresentation() {

        StringProperty sp = getStringProperty(RAW_PROPERTY_NAME);

        if (sp == null) {

            return null;
        }

        return sp.getString();
    }

    @Override
    public String getPreferredRepresentation(String fieldSeparator) {

        //
        // we explicitly return null at this level, we want subclasses to override this to provide specific behavior
        //

        return null;
    }

    @Override
    public String getPreferredRepresentationHeader(String fieldSeparator) {

        //
        // we explicitly return null at this level, we want subclasses to override this to provide specific behavior
        //

        return null;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * API for subclasses to update their internal raw representation, for both the case of a single-line event
     * and a multi-line event. The first invocation sets the raw line representation, successive invocations append
     * successive lines to it.
     */
    public void appendRawLine(String line) {

        StringProperty rawRepresentation = getStringProperty(RAW_PROPERTY_NAME);

        if (rawRepresentation == null) {

            rawRepresentation = new StringProperty(RAW_PROPERTY_NAME, line);
            setProperty(rawRepresentation);
        }
        else {

            String raw = rawRepresentation.getString();

            if (raw == null) {

                //
                // this is unusual, but we work around it by using the existing property instance and setting the
                // value
                //

                rawRepresentation.setValue(line);
            }
            else {

                raw += "\n";
                raw += line;
                rawRepresentation.setValue(raw);
            }
        }
    }

    // Convenience accessors/mutators ----------------------------------------------------------------------------------

    public void setLongProperty(String name, long value, MeasureUnit mu) {

        setProperty(new LongProperty(name, value, mu));
    }

    public void setIntegerProperty(String name, int value, MeasureUnit mu) {

        setProperty(new IntegerProperty(name, value, mu));
    }

    public <T> void setListProperty(String name, List<T> value) {

        setProperty(new ListProperty<>(name, value));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
