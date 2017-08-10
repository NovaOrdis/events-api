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

import io.novaordis.events.api.measure.MeasureUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        this.properties = new ArrayList<>();
    }

    /**
     * @param properties the order matters, and it will be preserved as the event is processed downstream. The
     *                   implementation makes an internal shallow copy.

     */
    public GenericEvent(List<Property> properties) {

        this();

        //noinspection Convert2streamapi
        for(Property p: properties) {
            setProperty(p);
        }
    }

    // Event implementation --------------------------------------------------------------------------------------------

    /**
     * Returns a shallow copy of the internal storage.
     */
    @Override
    public List<Property> getProperties() {

        if (properties.isEmpty()) {

            return Collections.emptyList();
        }

        return new ArrayList<>(properties);
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

    /**
     * The most generic implementation is semantically equivalent with getPropertyByName(). We assume the propertyKey
     * is the property name, and we delegate to getPropertyByName(). Subclasses may override for more complex
     * behavior.
     */
    @Override
    public Property getPropertyByKey(Object propertyKey) {

        if (propertyKey == null)  {

            throw new IllegalArgumentException("null property key");
        }

        if (propertyKey instanceof String) {

            //
            // getPropertyByName() semantics
            //

            return getProperty((String) propertyKey);
        }

        log.warn("getProperty() for non-String key (" + propertyKey + ") is usually overridden by subclasses");

        return null;
    }

    // Convenience typed accessors/mutators ----------------------------------------------------------------------------

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
    public LongProperty getLongProperty(String name) {

        Property p = getProperty(name);

        if (p != null && p instanceof LongProperty) {
            return (LongProperty)p;
        }

        return null;
    }

    @Override
    public IntegerProperty getIntegerProperty(String name) {

        Property p = getProperty(name);

        if (p != null && p instanceof IntegerProperty) {
            return (IntegerProperty)p;
        }

        return null;
    }

    @Override
    public BooleanProperty getBooleanProperty(String name) {

        Property p = getProperty(name);

        if (p != null && p instanceof BooleanProperty) {
            return (BooleanProperty)p;
        }

        return null;
    }

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
    public Property setProperty(Property property) {

        if (property == null) {

            throw new IllegalArgumentException("null property");
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

        Property p = getProperty(Event.LINE_NUMBER_PROPERTY_NAME);
        if (p == null) {

            return null;
        }

        if (!(p instanceof LongProperty)) {

            log.warn("\"" + Event.LINE_NUMBER_PROPERTY_NAME + "\" exists, but it is not a Long: " + p.getValue());
            return null;
        }

        LongProperty lp = (LongProperty)p;
        return lp.getLong();
    }

    @Override
    public void setLineNumber(Long lineNumber) {

        if (lineNumber == null) {

            removeLongProperty(Event.LINE_NUMBER_PROPERTY_NAME);
        }
        else {

            setLongProperty(Event.LINE_NUMBER_PROPERTY_NAME, lineNumber);
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

    public StringProperty removeStringProperty(String name) {

        return (StringProperty)removeProperty(name, String.class);
    }

    public void setLongProperty(String name, long value) {

        setLongProperty(name, value, null);
    }

    public void setLongProperty(String name, long value, MeasureUnit mu) {

        setProperty(new LongProperty(name, value, mu));
    }

    public LongProperty removeLongProperty(String name) {

        return (LongProperty)removeProperty(name, Long.class);
    }

    public void setIntegerProperty(String name, int value) {

        setIntegerProperty(name, value, null);
    }

    public void setIntegerProperty(String name, int value, MeasureUnit mu) {

        setProperty(new IntegerProperty(name, value, mu));
    }

    public IntegerProperty removeIntegerProperty(String name) {

        return (IntegerProperty)removeProperty(name, Integer.class);
    }

    public BooleanProperty setBooleanProperty(String name, boolean value) {

        return (BooleanProperty)setProperty(new BooleanProperty(name, value));
    }

    public BooleanProperty removeBooleanProperty(String name) {

        return (BooleanProperty)removeProperty(name, Boolean.class);
    }

    public <T> void setListProperty(String name, List<T> value) {

        setProperty(new ListProperty<>(name, value));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    /**
     * @return null if a property with the name AND type does not exist, not null otherwise.
     */
    Property removeProperty(String name, Class type) {

        int index = -1;

        for(Property p: properties) {

            index ++;

            if (p.getName().equals(name)) {

                if (p.getType().equals(type)) {

                    //
                    // do remove
                    //

                    return properties.remove(index);
                }
            }
        }

        return null;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
