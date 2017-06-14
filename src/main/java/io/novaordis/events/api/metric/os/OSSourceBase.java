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
import io.novaordis.events.api.metric.MetricSourceBase;
import io.novaordis.events.api.metric.MetricSourceException;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.OSAddress;
import io.novaordis.utilities.os.NativeExecutionResult;
import io.novaordis.utilities.os.NativeExecutor;
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

    //
    // the local OS instance or an OS running on a remote host
    //
    private NativeExecutor nativeExecutor;

    // Constructors ----------------------------------------------------------------------------------------------------

    protected OSSourceBase(Address address) {

        super(address);
    }

    /**
     * Used by subclasses that build the Address instance internally
     */
    protected OSSourceBase() {

        super(null);
    }

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricSourceException {

        insureAllMetricDefinitionsAreAssociatedWithThisSource(metricDefinitions);

        List<OSMetricDefinition> osMetricDefinitions = new ArrayList<>();
        Map<String, String> commandOutputs = new HashMap<>();

        for(MetricDefinition d: metricDefinitions) {

            //
            // all metric definitions must be OSMetricDefinitions
            //

            if (!(d instanceof OSMetricDefinition)) {

                throw new MetricSourceException(d + " is not an " + OSMetricDefinition.class.getSimpleName());
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

            String stdout = execute(command);
            commandOutputs.put(command, stdout);
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

    // MetricSourceBase overrides --------------------------------------------------------------------------------------

    @Override
    public OSAddress getAddress() {

        Address a = super.getAddress();
        return (OSAddress)a;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    void setNativeExecutor(NativeExecutor ne) {

        this.nativeExecutor = ne;
    }

    /**
     * Execute the command using the native executor and return the execution stdout. The stdout is in most cases a
     * multi-line string. If the execution fails for any reason (native executor unchecked command, native executor
     * checked command, non-zero exit code), or no stdout is returned, return null and log human interpretable
     * warnings. The method mustn't knowingly throw any unchecked exception.
     */
    String execute(String command) {

        String stdout = null;

        try {

            NativeExecutionResult r = nativeExecutor.execute(command);

            stdout = r.getStdout();
            String stderr = r.getStderr();

            if (r.isSuccess()) {

                if (stdout == null) {

                    log.warn("\"" + command + "\" succeeded but returned no stdout");
                }
            }
            else {

                int exitCode = r.getExitCode();

                String logStdout = stdout;
                stdout = null;

                String s = "\"" + command + "\" execution failed with exit code " + exitCode + ":";

                if (logStdout == null) {

                    s += "\n\nno stdout";
                }
                else {

                    s += "\n\nstdout:\n\n" + logStdout;
                }

                if (stderr == null) {

                    s += "\n\nno stderr";
                }
                else {

                    s += "\n\nstderr:\n\n" + stderr;
                }

                log.warn(s);
            }
        }
        catch (Exception e) {

            //
            // command fails in an unusual way
            //

            log.warn("\"" + command + "\" execution failed with exception (see below)", e);
        }

        return stdout;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    protected NativeExecutor getNativeExecutor() {

        return nativeExecutor;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
