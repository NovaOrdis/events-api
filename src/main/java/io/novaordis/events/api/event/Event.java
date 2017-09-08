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

import java.util.List;

/**
 *
 * https://kb.novaordis.com/index.php/Events-api_Concepts#Event
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 1/24/16
 */
public interface Event {

    // Constants -------------------------------------------------------------------------------------------------------

    //
    // where it makes sense, it is maintained as a LongProperty
    //
    String LINE_NUMBER_PROPERTY_NAME = "line-number";

    //
    // if it makes sense, the whole event is maintained in its raw (unparsed) format, as a StringProperty. Some
    // implementations may decide this is inefficient, so expect to get null when querying for it.
    //
    String RAW_PROPERTY_NAME = "raw";

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return true for timed events (whose timestamps can then be accessed via
     * TimedEvent.getTimestamp()/TimedEvent.getTime() invocations) and false for non-timed events.
     */
    boolean isTimed();

    /**
     * Stores the property, replacing the old property with the same name, if it exists. The order in which the
     * properties are set will be reflected in the order in which getProperties() returns them.
     *
     * If it is a map and the map already exists, the contents will be merged.
     *
     * If the implementation supports it, the order in which the properties are set is remembered, so methods
     * like getPropertyList() make sense for those.
     *
     * @return the old property with the same name, or null if such a property does not exist.
     *
     * @exception IllegalArgumentException on null argument.
     *
     * @see Event#getProperties()
     */
    Property setProperty(Property property);

    /**
     * Query the event and return the property with the given name, if it is carried by the event.
     *
     * @return null if there is no such property.
     *
     * @exception IllegalArgumentException if the name is null.
     */
    Property getProperty(String name);

    /**
     * Query the event and return the property with the given index, if it is carried by the event. Properties indexes
     * are 0-based.
     *
     * @return null if there is no such property.
     *
     * @exception IllegalArgumentException on negative indexes.
     */
    Property getProperty(int i);

    /**
     * https://kb.novaordis.com/index.php/Events-api_Concepts#Property_Setting_Order
     *
     * @return the List of properties set on this event, in the order in which they were set. The implementations must
     * guarantee that no two properties in the list have the same name. Implementations should advise whether they
     * return the actual storage (efficient but not safe) or a copy of it (less efficient but safe).
     *
     * @see Event#setProperty(Property)
     */
    List<Property> getProperties();

    /**
     * @return the List of properties that carry the specified type, set on this event, in the order in which they were
     * set. The implementations must guarantee that no two properties in the list have the same name. Implementations
     * should advise whether they return the actual storage (efficient but not safe) or a copy of it (less efficient but
     * safe).
     *
     * @param type the type of the property payload. If the property has a payload that extends or implements the
     *             type, it will be returned.
     *
     * @see Event#setProperty(Property)
     * @see Property#getType()
     */
    List<Property> getProperties(Class type);

    /**
     * Remove the specified property.
     *
     * @return null if a property with the name AND type does not exist, or the property instance that was just removed
     * otherwise.
     */
    Property removeProperty(String name, Class type);

    void clearProperties();

    // Convenience typed accessors/mutators ----------------------------------------------------------------------------

    // StringProperty Accessors/Mutators -------------------------------------------------------------------------------

    /**
     * @return the current StringProperty that is being replaced by StringProperty passed as argument, if it exists, or
     * null otherwise.
     */
    StringProperty setStringProperty(String name, String value);

    /**
     * Query the event and return the string property with the given name.
     *
     * @return the corresponding StringProperty or null if there is no such StringProperty. Note that the method will
     * return null if a property with the given name exists, but it is not a StringProperty.
     *
     * @see Event#getProperty(String)
     */
    StringProperty getStringProperty(String stringPropertyName);

    /**
     * Remove the StringProperty with the given name, if it exists.
     *
     * @return the StringProperty that was removed, or null if no such property exists.
     */
    StringProperty removeStringProperty(String stringPropertyName);

    // EventProperty Accessors/Mutators --------------------------------------------------------------------------------

    /**
     * @return the current EventProperty that is being replaced by EventProperty passed as argument, if it exists, or
     * null otherwise.
     */
    EventProperty setEventProperty(String name, Event event);

    /**
     * Query the event and return the event property with the given name.
     *
     * @return the corresponding EventProperty or null if there is no such EventProperty. Note that the method will
     * return null if a property with the given name exists, but it is not an EventProperty.
     *
     * @see Event#getProperty(String)
     */
    EventProperty getEventProperty(String eventPropertyName);

    /**
     * Remove the EventProperty with the given name, if it exists.
     *
     * @return the EventProperty that was removed, or null if no such property exists.
     */
    EventProperty removeEventProperty(String eventPropertyName);

    // LongProperty Accessors/Mutators ---------------------------------------------------------------------------------

    /**
     * @return the current LongProperty that is being replaced by LongProperty passed as argument, if it exists, or
     * null otherwise.
     */
    LongProperty setLongProperty(String name, Long value);

    /**
     * Query the event and return the Long property with the given name.
     *
     * @return the corresponding LongProperty or null if there is no such LongProperty. Note that the method will
     * return null if a property with the given name exists, but it is not a LongProperty.
     *
     * @see Event#getProperty(String)
     */
    LongProperty getLongProperty(String longPropertyName);

    /**
     * Remove the LongProperty with the given name, if it exists.
     *
     * @return the LongProperty that was removed, or null if no such property exists.
     */
    LongProperty removeLongProperty(String longPropertyName);

    // IntegerProperty Accessors/Mutators ------------------------------------------------------------------------------

    /**
     * Query the event and return the Integer property with the given name.
     *
     * @return the corresponding IntegerProperty or null if there is no such IntegerProperty. Note that the method will
     * return null if a property with the given name exists, but it is not a IntegerProperty.
     *
     * @see Event#getProperty(String)
     */
    IntegerProperty getIntegerProperty(String integerPropertyName);

    /**
     * @return the current IntegerProperty that is being replaced by IntegerProperty passed as argument, if it exists,
     * or null otherwise.
     */
    IntegerProperty setIntegerProperty(String name, Integer value);

    /**
     * Remove the IntegerProperty with the given name, if it exists.
     *
     * @return the IntegerProperty that was removed, or null if no such property exists.
     */
    IntegerProperty removeIntegerProperty(String name);

    // FloatProperty Accessors/Mutators --------------------------------------------------------------------------------

    /**
     * Query the event and return the Float property with the given name.
     *
     * @return the corresponding FloatProperty or null if there is no such FloatProperty. Note that the method will
     * return null if a property with the given name exists, but it is not a FloatProperty.
     *
     * @see Event#getProperty(String)
     */
    FloatProperty getFloatProperty(String floatPropertyName);

    /**
     * @return the current FloatProperty that is being replaced by FloatProperty passed as argument, if it exists,
     * or null otherwise.
     */
    FloatProperty setFloatProperty(String name, Float value);

    /**
     * Remove the FloatProperty with the given name, if it exists.
     *
     * @return the FloatProperty that was removed, or null if no such property exists.
     */
    FloatProperty removeFloatProperty(String name);

    // BooleanProperty Accessors/Mutators ------------------------------------------------------------------------------

    /**
     * Query the event and return the Boolean property with the given name.
     *
     * @return the corresponding BooleanProperty or null if there is no such BooleanProperty. Note that the method will
     * return null if a property with the given name exists, but it is not a BooleanProperty.
     *
     * @see Event#getProperty(String)
     */
    BooleanProperty getBooleanProperty(String booleanPropertyName);

    /**
     * Query the event and return the Map property with the given name.
     *
     * @return the corresponding MapProperty or null if there is no such MapProperty. Note that the method will
     * return null if a property with the given name exists, but it is not a MapProperty.
     *
     * @see Event#getProperty(String)
     */
    MapProperty getMapProperty(String mapPropertyName);

    /**
     * Query the event and return the Lit property with the given name.
     *
     * @return the corresponding ListProperty or null if there is no such ListProperty. Note that the method will
     * return null if a property with the given name exists, but it is not a ListProperty.
     *
     * @see Event#getProperty(String)
     */
    ListProperty getListProperty(String listPropertyName);

    // Typed accessors -------------------------------------------------------------------------------------------------

    /**
     * @return the line number associated with this event, in case of a single-line event, or the line number of the
     * first line of the event, in case of a multi-line event). Internally, it is maintained as a Long property with the
     * "line-number" name.
     *
     * If an internal value that cannot be converted to a long is found, the method returns null, and a warning
     * is logged.
     */
    Long getLineNumber();

    /**
     * @param lineNumber may be null, in which case the line number information is removed.
     */
    void setLineNumber(Long lineNumber);

    /**
     * Return the preferred representation, as presented by the event. This method is usually overridden by subclasses.
     * May return null.
     */
    String getPreferredRepresentation(String fieldSeparator);

    /**
     * Return the header line of the preferred representation, as presented by the event. This method is usually
     * overridden by subclasses. May return null.
     */
    String getPreferredRepresentationHeader(String fieldSeparator);

    /**
     * Return the raw representation of the event: if it makes sense, the whole event is maintained in its raw
     * (unparsed) format, as a StringProperty. Some implementations may decide this is inefficient, so this method
     * may return null. The representation may also contain multiple lines.
     */
    String getRawRepresentation();

}
