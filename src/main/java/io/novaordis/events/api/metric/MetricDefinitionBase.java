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

package io.novaordis.events.api.metric;

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.event.PropertyFactory;
import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.utilities.address.Address;

/**
 * Not thread-safe, access synchronization must be implemented externally.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public abstract class MetricDefinitionBase implements MetricDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String id;

    private Address source;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param source must always have a non-null source.
     */
    protected MetricDefinitionBase(Address source) {

        if (source == null) {

            throw new IllegalArgumentException("null metric source address");
        }

        this.source = source;
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

    @Override
    public String getId() {

        return id;
    }

    @Override
    public final String getLabel(LabelAttribute... attributes) {

        String s = getSimpleLabel();

        if (attributes.length == 1) {

            LabelAttribute a = attributes[0];

            if (LabelAttribute.MEASURE_UNIT.equals(a)) {

                MeasureUnit mu = getBaseUnit();

                if (mu != null) {

                    s += " (" + mu.getLabel() + ")";
                }
            }
            else {

                throw new RuntimeException("we don't know how to handle label attribute " + a);
            }
        }
        else if (attributes.length > 1) {

            throw new RuntimeException("we don't know yet how to handle multiple attributes");
        }

        return s;
    }

    @Override
    public Address getMetricSourceAddress() {

        return source;
    }

    @Override
    public Property buildProperty(Object value) {

        String id = getId();
        Class type = getType();
        MeasureUnit mu = getBaseUnit();

        //noinspection UnnecessaryLocalVariable
        Property p = PropertyFactory.createInstance(id, type, value, mu);
        return p;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return getId();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * Return the simple label, without any attribute.
     *
     * @see MetricDefinition#getLabel(LabelAttribute...)
     */
    protected abstract String getSimpleLabel();

    protected void setId(String id) {

        if (id == null) {

            throw new IllegalArgumentException("null id");
        }

        this.id = id;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
