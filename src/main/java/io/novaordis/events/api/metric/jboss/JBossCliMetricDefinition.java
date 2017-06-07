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

package io.novaordis.events.api.metric.jboss;

import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.events.api.metric.MetricDefinitionBase;
import io.novaordis.events.api.metric.MetricDefinitionException;
import io.novaordis.events.api.metric.MetricSource;

/**
 * A metric that can be read from a JBoss controller.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
public class JBossCliMetricDefinition extends MetricDefinitionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Package protected static ----------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private CliPath path;
    private CliAttribute attribute;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @throws MetricDefinitionException in case an invalid metric definition is encountered. The error message
     *  must be human-readable, as it will most likely end up in error messages.
     *
     * @throws IllegalArgumentException
     */
    public JBossCliMetricDefinition(MetricSource source, CliPath path, CliAttribute attribute)
            throws MetricDefinitionException {

        super(source);

        this.path = path;
        this.attribute = attribute;
    }

    // MetricDefinitionBase overrides ----------------------------------------------------------------------------------

    @Override
    public JBossController getSource() {

        return (JBossController)super.getSource();
    }

    @Override
    public String getId() {

        return path.getPath() + "/" + attribute.getName();
    }

    @Override
    public String getSimpleLabel() {

        //
        // it would be nice if we could come up with a human readable label - we'll see how we do that; in the mean
        // time, we'll just report path and attribute
        //

        return path.getPath() + "/" + attribute.getName();
    }

    @Override
    public MeasureUnit getBaseUnit() {

        //
        // it would be nice if we could come up with a valid value - we'll see how we do that; in the mean
        // time, we'll just return null
        //

        return null;
    }

    @Override
    public String getDescription() {

        //
        // it would be nice if we could come up with a description - we'll see how we do that; in the mea time, we'll
        // just return the simple label
        //

        return getSimpleLabel();
    }

    @Override
    public Class getType() {

        return null;
    }

    //
    // we need to override all source-related methods, as we override the storage
    //

    // Public ----------------------------------------------------------------------------------------------------------

    public String getPath() {

        if (path == null) {

            return null;
        }

        return path.getPath();
    }

    public String getAttributeName() {

        if (attribute == null) {

            return null;
        }

        return attribute.getName();
    }

    @Override
    public String toString() {

        //return PREFIX + source + ":" + path + "/" + attribute;
        return "?";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    CliAttribute getAttribute() {
        return attribute;
    }

    CliPath getPathInstance() {
        return path;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
