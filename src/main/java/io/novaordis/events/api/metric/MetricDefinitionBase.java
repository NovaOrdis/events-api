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

import io.novaordis.events.api.measure.MeasureUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // <String (os name), List<MetricSource>>
    private Map<String, List<MetricSource>> sources;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MetricDefinitionBase() {
        this.sources = new HashMap<>();
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

    @Override
    public final String getLabel(LabelAttribute... attributes) {

        String s = getSimpleLabel();

        if (attributes.length == 1) {

            LabelAttribute a = attributes[0];

            if (LabelAttribute.MEASURE_UNIT.equals(a)) {

                MeasureUnit mu = getMeasureUnit();

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

    /**
     * The implementation returns a copy of the internal list.
     */
    @Override
    public List<MetricSource> getSources(String  osName) {

        if (osName == null) {
            throw new IllegalArgumentException("null OS name");
        }

        List<MetricSource> sl = sources.get(osName);

        if (sl == null) {
            return Collections.emptyList();
        }

        return new ArrayList<>(sl);
    }

    /**
     * Not thread safe.
     */
    @Override
    public boolean addSource(String osName, MetricSource source) {

        List<MetricSource> sl = sources.get(osName);

        if (sl == null) {

            sl = new ArrayList<>();
            sources.put(osName, sl);
        }

        if (sl.contains(source)) {
            return false;
        }

        sl.add(source);

        return true;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return getDefinition();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * Return the simple label, without any attribute.
     *
     * @see MetricDefinition#getLabel(LabelAttribute...)
     */
    protected abstract String getSimpleLabel();

    /**
     * Used by the subclasses that need a finer grained control to the source storage.
     */
    protected void setSources(Map<String, List<MetricSource>> sources) {

        this.sources = sources;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
