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

import io.novaordis.events.api.metric.MetricSourceException;
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

    // Constructors ----------------------------------------------------------------------------------------------------

    public LocalOS() throws Exception {

        setNativeExecutor(OS.getInstance());

        log.debug(this + " constructed");
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

    /**
     * A LocalOS instance is always started, start() is a noop.
     */
    @Override
    public void start() throws MetricSourceException {

        log.debug("noop start");
    }

    @Override
    public boolean isStarted() {

        return true;
    }

    /**
     * A LocalOS instance is always started, stop() is a noop.
     */
    @Override
    public void stop() {

        log.debug("noop stop, " + this + " will remain started");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {

        return o instanceof LocalOS;
    }

    @Override
    public int hashCode() {

        return 1;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
