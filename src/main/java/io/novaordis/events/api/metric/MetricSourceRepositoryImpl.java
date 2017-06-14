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

import io.novaordis.utilities.address.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The default implementation of a MetricSourceRepository.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/4/17
 */
public class MetricSourceRepositoryImpl implements MetricSourceRepository {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(MetricSourceRepositoryImpl.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    /**
     * Maps the source type to a set of sources of that type.
     */
    private ConcurrentMap<Class<? extends MetricSource>, Set<MetricSource>> sources;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MetricSourceRepositoryImpl() {

        sources = new ConcurrentHashMap<>();

        log.debug(this + " constructed");
    }

    // MetricSourceRepository implementation ---------------------------------------------------------------------------

    @Override
    public Set<MetricSource> getSources() {

        Set<MetricSource> result = new HashSet<>();

        //noinspection Convert2streamapi
        for(Collection<MetricSource> sourcesOfASpecificType: sources.values()) {

            result.addAll(sourcesOfASpecificType);
        }

        return result;
    }

    /**
     * @return a copy of the set, if sources of type t exist.
     */
    @Override
    public <T extends MetricSource> Set<T> getSources(Class<T> t) {

        Set<? extends MetricSource> s = sources.get(t);

        if (s == null || s.isEmpty()) {

            return Collections.emptySet();
        }

        //noinspection unchecked
        return new HashSet<>((Set<T>)s);
    }

    @Override
    public <T extends MetricSource> T getSource(Class<T> t, Address... addresses) {

        if (addresses.length > 1) {

            throw new IllegalArgumentException("illegal to provide more than one address");
        }

        Set<T> sources = getSources(t);

        if (sources.isEmpty()) {

            return null;
        }

        //
        // we have sources, match the address
        //

        if (addresses.length == 0) {

            //
            // no address specified, we assume an "singleton" metric source
            //

            if (sources.size() > 1) {

                throw new IllegalStateException(
                        "no address was specified, but more than one " + t.getSimpleName() + " sources are known");
            }

            return sources.iterator().next();
        }

        //
        // an address has been specified
        //

        Address address = addresses[0];

        for(T s: sources) {

            if (s.hasAddress(address)) {

                return s;
            }
        }

        return null;
    }

    @Override
    public <T extends MetricSource> void add(T source) {

        if (source == null) {

            throw new IllegalArgumentException("null source");
        }

        Set<MetricSource> s = sources.get(source.getClass());

        if (s == null) {

            s = new HashSet<>();
            sources.put(source.getClass(), s);
        }

        s.add(source);
    }

    @Override
    public boolean isEmpty() {

        return sources.isEmpty();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return "MetricSourceRepositoryImpl[" + Integer.toHexString(System.identityHashCode(this)) + "]";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
