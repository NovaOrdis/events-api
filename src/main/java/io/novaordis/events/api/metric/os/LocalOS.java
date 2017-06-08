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

import io.novaordis.utilities.os.NativeExecutionResult;
import io.novaordis.utilities.os.OS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/1/17
 */
public class LocalOS extends OSSourceBase {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(LocalOS.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private OS os;

    // Constructors ----------------------------------------------------------------------------------------------------

    public LocalOS() throws Exception {

        this.os = OS.getInstance();
    }

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public String getAddress() {

        return null;
    }

    @Override
    public boolean hasAddress(String address) {

        return false;
    }

    @Override
    protected String execute(String command) {

        String stdout = null;

        try {

            NativeExecutionResult r = os.execute(command);

            if (r.isSuccess()) {

                stdout = r.getStdout();

                if (stdout == null) {

                    log.warn("");

                }
                else if (stdout.isEmpty()) {

                    stdout = null;

                    log.warn("");

                }
            }
            else {

                log.warn("\"" + command + "\" execution failed");
            }
        }
        catch (Exception e) {

            //
            // command fails in an unusual way
            //

            log.warn("\"" + command + "\" execution failed", e);
        }

        return stdout;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
