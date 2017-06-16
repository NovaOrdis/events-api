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

package io.novaordis.events.api.metric;

import io.novaordis.events.api.metric.jboss.JBossController;
import io.novaordis.events.api.metric.jmx.JmxBus;
import io.novaordis.events.api.metric.os.LocalOS;
import io.novaordis.events.api.metric.os.RemoteOS;
import io.novaordis.jboss.cli.model.JBossControllerAddress;
import io.novaordis.jmx.JmxAddress;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.LocalOSAddress;
import io.novaordis.utilities.address.OSAddress;

/**
 * A static factory that builds metric source instances from their addresses.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/13/17
 */
public class MetricSourceFactoryImpl implements MetricSourceFactory {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    //
    // a mechanism to defer the creation to a delegate factory - used for testing.
    //
    private MetricSourceFactory delegateFactory;

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // MetricSourceFactory implementation ------------------------------------------------------------------------------

    @Override
    public MetricSource buildMetricSource(Address a) throws MetricSourceException {

        if (a == null) {

            throw new IllegalArgumentException("null address");
        }

        if (a instanceof LocalOSAddress) {

            return new LocalOS();
        }
        else if (a instanceof OSAddress) {

            return new RemoteOS((OSAddress)a);
        }
        else if (a instanceof JmxAddress) {

            return new JmxBus((JmxAddress)a);
        }
        else if (a instanceof JBossControllerAddress) {

            try {

                return new JBossController((JBossControllerAddress) a);
            }
            catch(Exception e) {

                throw new MetricSourceException(e);
            }
        }
        else if (JBossControllerAddress.PROTOCOL.equals(a.getProtocol())) {

            //
            // TODO this is clunky, think of a better solution
            //
            throw new IllegalArgumentException(
                    "use a JBossControllerAddress instance to create a JBossController, not a simple AddressImpl");
        }
        else if (delegateFactory != null) {

            return delegateFactory.buildMetricSource(a);
        }
        else {

            throw new MetricSourceException(a + " NOT YET SUPPORTED");
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setDelegateFactory(MetricSourceFactory f) {

        this.delegateFactory = f;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
