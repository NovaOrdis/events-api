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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A JBoss DMR (Dynamic Model Representation) path.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
class DmrPath {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private List<String> pathElements;

    // Constructors ----------------------------------------------------------------------------------------------------

    public DmrPath() {

        this(new String[0]);
    }

    public DmrPath(String... pes) {

        this.pathElements = new ArrayList<>();

        for(String pe: pes) {

            addPathElement(pe);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * A path element in the "a=b" format.
     */
    public void addPathElement(String pathElement) {

        //
        // skip leading slash
        //

        if (pathElement.startsWith("/")) {

            pathElement = pathElement.substring(1);
        }

        pathElements.add(pathElement);
    }

    public String getPath() {

        String s = "/";

        for(Iterator<String> i = pathElements.iterator(); i.hasNext(); ) {

            s += i.next();

            if (i.hasNext()) {

                s += "/";
            }
        }

        return s;
    }

    @Override
    public String toString() {
        return getPath();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
