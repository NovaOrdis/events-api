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
import io.novaordis.events.api.metric.jboss.JBossCliMetricDefinition;
import io.novaordis.events.api.metric.source.MetricSource;
import io.novaordis.utilities.UserErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public interface MetricDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    Logger log = LoggerFactory.getLogger(MetricDefinition.class);

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * So far, we support metrics that are implemented in two different ways:
     *
     * 1. Class based - if the name of the class is found in the classpath, it is loaded
     * 2. Type based (like jboss:)
     */
    static MetricDefinition getInstance(String s) throws UserErrorException {

        log.debug("parsing metric definition \"" + s + "\"");

        if (s.startsWith(JBossCliMetricDefinition.PREFIX)) {

            try {

                return new JBossCliMetricDefinition(s);
            }
            catch (Exception e) {

                String msg = "invalid jboss metric definition";
                msg = e.getMessage() == null ? msg : msg + ": " + e.getMessage();
                throw new UserErrorException(msg, e);
            }
        }

        //
        // TODO naive implementation, come up with something better
        //

        String[] packages = {

                "io.novaordis.events.api.metric.os",
        };

        String fqcn = null;
        Class c = null;

        for(String p : packages) {

            fqcn = p + "." + s;

            try {

                c = Class.forName(fqcn);
                if (c != null) {
                    break;
                }
            }
            catch (Exception e) {

                log.debug("no such metric implementation: " + fqcn);
            }
        }

        if (c == null) {
            throw new UserErrorException("unknown metric " + s);
        }

        try {
            return (MetricDefinition)c.newInstance();
        }
        catch(Exception e) {

            throw new UserErrorException(fqcn + " exists, but it cannot be instantiated", e);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * May return null if the metric is non-dimensional (for example load average).
     *
     * @see MeasureUnit
     */
    MeasureUnit getMeasureUnit();

    /**
     * The human readable text that explains what this metric represents.
     */
    String getDescription();

    /**
     * The types for values corresponding to this metric definition. Typical: Integer, Long, Double.
     */
    Class getType();

    // new -------------------------------------------------------------------------------------------------------------

    /**
     * The metric definition. Information that should be sufficient to a metric source to provide a value. For
     * example "PhysicalMemoryTotal", "java.lang:type=Threading.ThreadCount",
     * "/subsystem=messaging/hornetq-server=default/jms-queue=DLQ:message-count". The first makes sense within the
     * context of local or remote OS, the second in the context of a JMX bus, and the third in the context of a
     * JBoss management controller.
     */
    String getDefinition();

    // to refactor -----------------------------------------------------------------------------------------------------

    /**
     * The metric name. Must not contain any spaces. It is used by the factory method getInstance() to create the
     * corresponding class instance, and usually, it is the simple name of the implementing class. Example:
     * "PhysicalMemoryTotal" or "jboss:localhost:9999/subsystem=test/test-attribute.
     */
    @Deprecated
    String getName();

    /**
     * A human readable string, possibly space separated, that is used to represent the metric in user-facing
     * representations, such as a CSV file headers. The label includes, by default, the parantheses-enclosed measure
     * unit, if the metric has a measure unit. For example, PhysicalMemoryTotal's label is
     * "Total Physical Memory (bytes)"
     *
     * @see MetricDefinition#getSimpleLabel()
     * @see MeasureUnit#getLabel()
     */
    String getLabel();

    /**
     * A human readable string, possibly space separated, that is used to represent the metric in user-facing
     * representations, such as a CSV file headers, but without measure unit. To get a complete representation,
     * including the measure unit, if available, use getLabel(). For example, PhysicalMemoryTotal's simple label is
     * "Total Physical Memory".
     *
     * @see MetricDefinition#getLabel()
     */
    String getSimpleLabel();

    /**
     * @return a list of sources for this metric, in the descending order of their priority. The data collection layer
     * will use this information to minimize the number of native calls or the number of file reads: if all required
     * metrics can be obtained from a common source, only run that specific native command (or read that file).
     *
     * If no sources for the specified OS instance exist, the method will return an empty list, never null.
     *
     * @param osName one of OS.Linux, OS.MacOS, OS.Windows
     */
    List<MetricSource> getSources(String osName);

    /**
     * Add a source for this metric. Subsequent additions establish priority: the first added source (for a specific
     * os) takes precedence over the second added source, for the same os, etc. If a source is already present, it won't
     * be added and the method will return false.
     *
     * @param osName one of OS.Linux, OS.MacOS, OS.Windows
     *
     * @return true if the source was indeed added (no duplicate found)
     */
    boolean addSource(String osName, MetricSource source);

}
