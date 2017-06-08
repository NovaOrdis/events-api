/*
 * Copyright (c) 2017 Nova Ordis LLC
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

package io.novaordis.events.api.metric.os;

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricException;
import io.novaordis.events.api.metric.MetricSourceBase;
import io.novaordis.utilities.os.NativeExecutionResult;
import io.novaordis.utilities.os.OS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A metric source whose all metrics can be obtained executing OS commands.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/5/17
 */
public abstract class OSSourceBase extends MetricSourceBase {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(OSSourceBase.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // MetricSource implementation -------------------------------------------------------------------------------------

    public List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricException {

        insureAllMetricDefinitionsAreAssociatedWithThisSource(metricDefinitions);

        List<OSMetricDefinition> osMetricDefinitions = new ArrayList<>();
        Map<String, String> commandOutputs = new HashMap<>();

        for(MetricDefinition d: metricDefinitions) {

            //
            // all metric definitions must be OSMetricDefinitions
            //

            if (!(d instanceof OSMetricDefinition)) {

                throw new MetricException(d + " is not an " + OSMetricDefinition.class.getSimpleName());
            }

            OSMetricDefinition osmd = (OSMetricDefinition)d;
            String command = osmd.getCommand();

            //
            // command may be null, when we know the metric definition is not available for a specific O/S
            //

            if (command == null) {

                log.debug(d + " not available on " + OSType.getCurrent());
            }
            else {

                commandOutputs.put(command, null);
            }

            osMetricDefinitions.add(osmd);
        }

        //
        // execute all commands
        //

        //
        // TODO if more than one command, execute in parallel
        //

        for(String command: commandOutputs.keySet()) {

            //
            // execute the command and associate the output with the command
            //

            try {

                NativeExecutionResult r = OS.getInstance().execute(command);

                if (r.isSuccess()) {

                    String stdout = r.getStdout();
                    commandOutputs.put(command, stdout);
                }
                else {

                    log.warn("\"" + command + "\" execution failed");
                }
            }
            catch (Exception e) {

                log.warn("\"" + command + "\" execution failed", e);
            }
        }

        //
        // process output
        //

        List<Property> results = new ArrayList<>();

        for(OSMetricDefinition osmd: osMetricDefinitions) {

            String commandOutput = commandOutputs.get(osmd.getCommand());

            //
            // the command output will be null in case the metric is not available on the current O/S so
            // parseCommandOutput() implementations must be able to handle that
            //
            Property p = osmd.parseCommandOutput(commandOutput);
            results.add(p);
        }

        return results;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
