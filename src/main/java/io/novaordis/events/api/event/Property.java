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

import java.text.Format;
import java.util.List;

/**
 * A name/value pair with additional attributes, such as type, measure unit, presentation format, etc., carried by an
 * event. The value usually represents the snapshot in time of a certain runtime value, so that is why the terms
 * "property" and "reading" are interchangeably used. An event cannot have two properties with the same name.
 *
 * As a common use case, Properties are created based on a MetricDefinition.
 *
 * Property instances can be instantiate directly using the respective constructors, or they can be created with the
 * static factory PropertyFactory.createInstance(...).
 *
 * More: https://kb.novaordis.com/index.php/Events-api_Concepts#Property
 *
 * @see PropertyFactory
 *
 * @see io.novaordis.events.api.metric.MetricSource#collectMetrics(List)
 *
 * Also see:
 *
 * @see io.novaordis.events.api.metric.MetricSource
 * @see io.novaordis.events.api.metric.MetricDefinition
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/1/16
 */
public interface Property extends Comparable<Property> {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * A human readable, possibly multi-word, name. Example: "Post-Collection Old Generation Size". When processed
     * for display, the name may optionally be followed by the measure unit, if present. Example:
     * "Post-Collection Old Generation Size (MB)".
     *
     * Case sensitive.
     */
    String getName();

    Object getValue();

    /**
     * Null is acceptable.
     */
    void setValue(Object value);

    /**
     * @return the actual type of the value carried by the property. May return null.
     */
    Class getType();

    /**
     * May return null if the property is non-dimensional (a path, for example)
     */
    MeasureUnit getMeasureUnit();

    //
    // TODO next time I need setMeasureUnit(), refactor.
    //

    /**
     * May return null.
     */
    Format getFormat();

    /**
     * Builds a property similar to this from the given string.
     *
     * @exception IllegalArgumentException if the string cannot be converted into a property of the same kind.
     */
    Property fromString(String s) throws IllegalArgumentException;

    /**
     * @return the externalized value of the property's value - how it would appear in a text file - or null if the
     * value is null.
     */
    String externalizeValue();

    String externalizeType();

}
