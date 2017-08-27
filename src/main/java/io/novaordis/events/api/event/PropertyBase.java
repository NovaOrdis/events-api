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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/1/16
 */
public abstract class PropertyBase implements Property, Comparable<Property> {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String name;
    private MeasureUnit measureUnit;
    private Format format;

    protected Object value;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param name the property name
     */
    protected PropertyBase(String name, Object value) {

        this(name, value, null, null);
    }

    /**
     * @param name the property name
     * @param measureUnit may be null.
     */
    protected PropertyBase(String name, Object value, MeasureUnit measureUnit) {

        this(name, value, null, measureUnit);
    }

    /**
     * @param name the property name
     * @param format the format, may be null.
     */
    protected PropertyBase(String name, Object value, Format format) {

        this(name, value, format, null);
    }

    /**
     * @param name the property name
     * @param format the format, may be null.
     * @param measureUnit may be null.
     */
    protected PropertyBase(String name, Object value, Format format, MeasureUnit measureUnit) {

        setName(name);

        this.value = value;
        this.format = format;
        this.measureUnit = measureUnit;
    }

    // Comparable implementation ---------------------------------------------------------------------------------------

    public int compareTo(@SuppressWarnings("NullableProblems") Property o) {

        if (o == null) {

            throw new NullPointerException("null property");
        }

        return name.compareTo(o.getName());
    }

    // Property implementation -----------------------------------------------------------------------------------------

    @Override
    public String getName() {

        return name;
    }

    @Override
    public Object getValue() {

        return value;
    }

    @Override
    public void setValue(Object value) {

        if (value == null) {

            this.value = null;
        }
        else {

            this.value = value;
        }
    }

    @Override
    public MeasureUnit getMeasureUnit() {

        return measureUnit;
    }

    @Override
    public Format getFormat() {

        return format;
    }

    @Override
    public String externalizeValue() {

        if (value == null) {

            return null;
        }

        Format format = getFormat();
        return format == null ? value.toString() : format.format(value);
    }

    @Override
    public String externalizeType() {

        return getName();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * TODO this must be added to the Property interface.
     */
    public void setMeasureUnit(MeasureUnit measureUnit) {

        this.measureUnit = measureUnit;
    }

    public void setFormat(Format format) {

        this.format = format;
    }

    @Override
    public String toString() {

        if (value == null) {

            return name;
        }

        return name + "=" + value;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * Some subclasses may want to change the name of the property after creation. TimestampProperty is an example.
     */
    protected void setName(String s) {

        this.name = s;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
