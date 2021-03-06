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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Factory that creates the appropriate property instance based on type and value. If the type is not known (null), but
 * the value is known and typed, first level heuristics are applied. If the value is a String, second level heuristics
 * are applied by attempting to convert to known types.
 *
 * Must be kept thread safe.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/2/16
 */
public class PropertyFactory {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(PropertyFactory.class);

    // Static ----------------------------------------------------------------------------------------------------------

    private static final SimpleDateFormat[] TIME_FORMATS = {

            new SimpleDateFormat("MM/dd/yy HH:mm:ss"),
    };

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public PropertyFactory() {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @see PropertyFactory#createInstance(String, Class, Object, Double, MeasureUnit)
     */
    public Property createInstance(String name, Class type, Object value, MeasureUnit measureUnit) {

        return createInstance(name, type, value, null, measureUnit);
    }

    /**
     * Creates a property instance with the given name, based on the given value and type. If the value and the type
     * match, the property is created right away. If the value is a String, and the type is not a String.class, a
     * conversion is attempted first, and if the conversion is successful, the property is created. Otherwise, an
     * IllegalArgumentException is thrown.
     *
     * @param type - may be null, in which case type heuristics apply.
     *
     * @param value - may be null, in which case "missing value" typed properties or UndefinedTypeProperty will be
     *              built.
     *
     * @param conversionFactor the double to multiply the given value to obtain the value to write into the property.
     *                         May be null, in which case it is ignored.
     *
     * @param measureUnit null is acceptable
     *
     * @exception IllegalArgumentException if the value and the type do not match, or a conversion from String to the
     * type in question fails.
     */
    public Property createInstance(
            String name, Class type, Object value, Double conversionFactor, MeasureUnit measureUnit) {

        PropertyBase result;

        if (type == null) {

            //
            // if the value is not null, attempt type heuristics. If the value is null, attempt to create an
            // UndefinedTypeProperty instance
            //

            if (value != null) {

                return createTypeHeuristicsInstance(name, value, conversionFactor, measureUnit);
            }

            if (measureUnit != null) {

                throw new IllegalArgumentException(
                        "cannot create an " + UndefinedTypeProperty.class.getSimpleName() +
                                " instance when the measure unit is specified");
            }

            return new UndefinedTypeProperty(name);
        }
        else if (String.class.equals(type)) {

            if (value != null && !(value instanceof String)) {

                throw new IllegalArgumentException(
                        "cannot create a " + type + " property with a " + value.getClass().getSimpleName() + " value");
            }

            result = new StringProperty(name, (String)value);
        }
        else if (Integer.class.equals(type)) {

            Integer i;

            if (value == null || value instanceof Integer) {

                i = (Integer)value;
            }
            else if (value instanceof String) {

                try {

                    i = Integer.parseInt((String) value);
                }
                catch(Exception e) {

                    throw new IllegalArgumentException("cannot convert \"" + value + "\" to an integer");
                }
            }
            else {

                throw new IllegalArgumentException(
                        "cannot create a " + type + " property with a " + value.getClass().getSimpleName() + " value");
            }

            if (conversionFactor != null && i != null) {

                i = (int)(i * conversionFactor);
            }

            result = new IntegerProperty(name, i);
        }
        else if (Long.class.equals(type)) {

            Long l;

            if (value == null || value instanceof Long) {

                l = (Long)value;
            }
            else if (value instanceof String) {

                try {

                    l = Long.parseLong((String) value);
                }
                catch(Exception e) {

                    throw new IllegalArgumentException("cannot convert \"" + value + "\" to a long");
                }
            }
            else {

                throw new IllegalArgumentException(
                        "cannot create a " + type + " property with a " + value.getClass().getSimpleName() + " value");
            }

            if (conversionFactor != null && l != null) {

                l = (long)(l * conversionFactor);
            }

            result = new LongProperty(name, l);
        }
        else if(Double.class.equals(type)) {

            Double d;

            if (value == null || value instanceof Double) {

                d = (Double)value;
            }
            else if (value instanceof String) {

                try {

                    d = Double.parseDouble((String) value);
                }
                catch(Exception e) {

                    throw new IllegalArgumentException("cannot convert \"" + value + "\" to a double");
                }
            }
            else {

                throw new IllegalArgumentException(
                        "cannot create a " + type + " property with a " + value.getClass().getSimpleName() + " value");
            }

            if (conversionFactor != null && d != null) {

                d = d * conversionFactor;
            }

            result = new DoubleProperty(name, d);
        }
        else if(Float.class.equals(type)) {

            Float f;

            if (value == null || value instanceof Float) {

                f = (Float)value;
            }
            else if (value instanceof String) {

                try {

                    f = Float.parseFloat((String) value);
                }
                catch(Exception e) {

                    throw new IllegalArgumentException("cannot convert \"" + value + "\" to a float");
                }
            }
            else {

                throw new IllegalArgumentException(
                        "cannot create a " + type + " property with a " + value.getClass().getSimpleName() + " value");
            }

            if (conversionFactor != null && f != null) {

                f = (float)(f * conversionFactor);
            }

            result = new FloatProperty(name, f);
        }
        else if(Date.class.equals(type)) {

            Long l;

            if (value == null || value instanceof Long) {

                l = (Long)value;
            }
            else if (value instanceof String) {

                try {

                    l = Long.parseLong((String) value);
                }
                catch(Exception e) {

                    throw new IllegalArgumentException("cannot convert \"" + value + "\" into a long");
                }
            }
            else {

                throw new IllegalArgumentException(
                        "cannot create a " + type + " property with a " + value.getClass().getSimpleName() + " value");
            }

            if (conversionFactor != null) {

                throw new RuntimeException("NOT YET IMPLEMENTED: conversion factor");
            }

            result = new DateProperty(name, l == null ? null : new Date(l));
        }
        else if(Map.class.equals(type)) {

            if (value != null && !(value instanceof Map)) {

                throw new IllegalArgumentException(
                        "cannot create a " + type + " property with a " + value.getClass().getSimpleName() + " value");
            }

            //noinspection unchecked
            result = new MapProperty(name, (Map)value);
        }
        else {

            throw new RuntimeException("createInstance() for " + type + " NOT YET IMPLEMENTED");
        }

        result.setMeasureUnit(measureUnit);
        return result;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    static Property createTypeHeuristicsInstance(
            String name, Object value, Double conversionFactor, MeasureUnit measureUnit) {

        if (log.isDebugEnabled()) {

            log.debug("type heuristics: " + name + ", " + value + ", " + conversionFactor + ", " + measureUnit);
        }

        if (value == null) {

            throw new IllegalArgumentException("null value, cannot infer type");
        }

        if (value instanceof Integer) {

            return new IntegerProperty(name, (Integer)value);
        }
        else if (value instanceof Long) {

            return new LongProperty(name, (Long)value);
        }
        else if (value instanceof Float) {

            return new FloatProperty(name, (Float)value);
        }
        else if (value instanceof Double) {

            return new DoubleProperty(name, (Double)value);
        }
        else if (value instanceof String) {

            //
            // start with the most complex patterns and devolve as heuristics fail
            //

            String s = (String)value;

            //
            // time
            //

            for(SimpleDateFormat f: TIME_FORMATS) {

                try {

                    Date d = f.parse(s);

                    //
                    // heuristics succeeded
                    //

                    return new TimestampProperty(name, d.getTime(), f);
                }
                catch (Exception e) {

                    //
                    // conversion failed, heuristics did not work, go to next
                    //
                }
            }

            //
            // float
            //

            if (s.contains(".")) {

                try {

                    float f = Float.parseFloat(s);

                    //
                    // heuristics succeeded
                    //

                    return new FloatProperty(name, f);

                }
                catch(Exception e) {

                    //
                    // conversion failed, heuristics did not work, go to next
                    //
                }
            }

            //
            // integer
            //

            try {

                int i = Integer.parseInt(s);

                //
                // heuristics succeeded
                //

                return new IntegerProperty(name, i);

            }
            catch(Exception e) {

                //
                // conversion failed, heuristics did not work, go to next
                //
            }

            //
            // catch all
            //

            return new StringProperty(name, (String)value);
        }
        else {

            throw new RuntimeException(
                    "NOT YET IMPLEMENTED: support for type inference from " + value + " (" + value.getClass() + ")");
        }
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
