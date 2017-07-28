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

import java.util.Set;

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

    Set<Property> getProperties();

    /**
     * Query the event and return the property with the given name, if it is carried by the event. This approach should
     * work in most cases for simple events, that rely on a flat property namespace. However, in more complex situations
     * when multiple properties with the same name are carried by the same event, use getProperty(Object) method.
     *
     * @return null if there is no such property.
     *
     * @see Event#getPropertyByKey(Object)
     *
     * @exception IllegalArgumentException if the name is null.
     */
    Property getProperty(String name);

    /**
     * Query the event and return the property corresponding to the given key. In most cases, when events carry
     * properties belonging to a flat namespace, it is sufficient to use the property name as key, so
     * getProperty(String) should work. However, in more complex situations when multiple properties with the same name
     * are carried by the same event, a generic Object key can be used to discriminate properties.
     *
     * @return null if there is no such property.
     *
     * @see Event#getProperty(String)
     *
     * @exception IllegalArgumentException if the key is null.
     */
    Property getPropertyByKey(Object propertyKey);

    /**
     * Query the event and return the string property with the given name.
     *
     * @return the corresponding StringProperty or null if there is no such StringProperty. Note that the method will
     * return null if a property with the given name exists, but it is not a StringProperty.
     *
     * @see Event#getProperty(String)
     * @see Event#getPropertyByKey(Object)
     */
    StringProperty getStringProperty(String stringPropertyName);

    /**
     * Query the event and return the Long property with the given name.
     *
     * @return the corresponding LongProperty or null if there is no such LongProperty. Note that the method will
     * return null if a property with the given name exists, but it is not a LongProperty.
     *
     * @see Event#getProperty(String)
     * @see Event#getPropertyByKey(Object)
     */
    LongProperty getLongProperty(String longPropertyName);

    /**
     * Query the event and return the Integer property with the given name.
     *
     * @return the corresponding IntegerProperty or null if there is no such IntegerProperty. Note that the method will
     * return null if a property with the given name exists, but it is not a IntegerProperty.
     *
     * @see Event#getProperty(String)
     * @see Event#getPropertyByKey(Object)
     */
    IntegerProperty getIntegerProperty(String integerPropertyName);

    /**
     * Query the event and return the Boolean property with the given name.
     *
     * @return the corresponding BooleanProperty or null if there is no such BooleanProperty. Note that the method will
     * return null if a property with the given name exists, but it is not a BooleanProperty.
     *
     * @see Event#getProperty(String)
     * @see Event#getPropertyByKey(Object)
     */
    BooleanProperty getBooleanProperty(String booleanPropertyName);

    /**
     * Query the event and return the Map property with the given name.
     *
     * @return the corresponding MapProperty or null if there is no such MapProperty. Note that the method will
     * return null if a property with the given name exists, but it is not a MapProperty.
     *
     * @see Event#getProperty(String)
     * @see Event#getPropertyByKey(Object)
     */
    MapProperty getMapProperty(String mapPropertyName);

    /**
     * Query the event and return the Lit property with the given name.
     *
     * @return the corresponding ListProperty or null if there is no such ListProperty. Note that the method will
     * return null if a property with the given name exists, but it is not a ListProperty.
     *
     * @see Event#getProperty(String)
     * @see Event#getPropertyByKey(Object)
     */
    ListProperty getListProperty(String listPropertyName);

    /**
     * Stores the property, replacing the old one if exists.
     *
     * If it is a map and the map already exists, the contents will be merged.
     *
     * If the implementation supports it, the order in which the properties are set is remembered, so methods
     * like getPropertyList() make sense for those.
     *
     * @return the old property with the same name, or null.
     *
     * @exception IllegalArgumentException on null argument.
     */
    Property setProperty(Property property);

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

}
