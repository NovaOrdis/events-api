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

package io.novaordis.events.api.metric.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServerConnection;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation identical with novaordis-jmx' test MockMBeanServerConnection.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/20/17
 */
public class MockMBeanServerConnection implements MBeanServerConnection {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(MockMBeanServerConnection.class);

    // Static ----------------------------------------------------------------------------------------------------------

    private static final Map<ObjectName, List<Attribute>> content = new HashMap<>();

    public static void clear() {

        content.clear();
    }

    public static void addAttribute(ObjectName on, String attributeName, Object value) {

        List<Attribute> al = content.get(on);

        if (al == null) {

            al = new ArrayList<>();
            content.put(on, al);
        }

        Integer index = null;

        for(int i = 0; i < al.size(); i ++) {

            if (al.get(i).getName().equals(attributeName)) {

                index = i;
                break;
            }
        }

        Attribute a = new Attribute(attributeName, value);

        if (index != null) {

            al.set(index, a);

        }
        else {

            al.add(a);
        }
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockMBeanServerConnection() {

        log.info(this + " constructed");
    }


    // MBeanServerConnection implementation ----------------------------------------------------------------------------

    @Override
    public ObjectInstance createMBean(String className, ObjectName name) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
        throw new RuntimeException("createMBean() NOT YET IMPLEMENTED");
    }

    @Override
    public ObjectInstance createMBean(String className, ObjectName name, ObjectName loaderName) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
        throw new RuntimeException("createMBean() NOT YET IMPLEMENTED");
    }

    @Override
    public ObjectInstance createMBean(String className, ObjectName name, Object[] params, String[] signature) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
        throw new RuntimeException("createMBean() NOT YET IMPLEMENTED");
    }

    @Override
    public ObjectInstance createMBean(String className, ObjectName name, ObjectName loaderName, Object[] params, String[] signature) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
        throw new RuntimeException("createMBean() NOT YET IMPLEMENTED");
    }

    @Override
    public void unregisterMBean(ObjectName name) throws InstanceNotFoundException, MBeanRegistrationException, IOException {
        throw new RuntimeException("unregisterMBean() NOT YET IMPLEMENTED");
    }

    @Override
    public ObjectInstance getObjectInstance(ObjectName name) throws InstanceNotFoundException, IOException {
        throw new RuntimeException("getObjectInstance() NOT YET IMPLEMENTED");
    }

    @Override
    public Set<ObjectInstance> queryMBeans(ObjectName name, QueryExp query) throws IOException {
        throw new RuntimeException("queryMBeans() NOT YET IMPLEMENTED");
    }

    @Override
    public Set<ObjectName> queryNames(ObjectName name, QueryExp query) throws IOException {
        throw new RuntimeException("queryNames() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isRegistered(ObjectName name) throws IOException {
        throw new RuntimeException("isRegistered() NOT YET IMPLEMENTED");
    }

    @Override
    public Integer getMBeanCount() throws IOException {
        throw new RuntimeException("getMBeanCount() NOT YET IMPLEMENTED");
    }

    @Override
    public Object getAttribute(ObjectName name, String attributeName)
            throws MBeanException, AttributeNotFoundException,
            InstanceNotFoundException, ReflectionException, IOException {

        List<Attribute> attributes = content.get(name);

        if (attributes == null) {

            //
            // this simulates a real response
            //
            throw new InstanceNotFoundException(name.toString());
        }

        for(Attribute a: attributes) {

            if (a.getName().equals(attributeName)) {

                return a.getValue();
            }
        }

        throw new AttributeNotFoundException("Could not find any attribute matching: " + attributeName);
    }

    @Override
    public AttributeList getAttributes(ObjectName name, String[] attributeNames)
            throws InstanceNotFoundException, ReflectionException, IOException {

        List<Attribute> attributes = content.get(name);

        if (attributes == null) {

            throw new InstanceNotFoundException();
        }

        List<Object> values = new ArrayList<>();

        outer: for(String attributeName: attributeNames) {

            for (Attribute a : attributes) {

                if (a.getName().equals(attributeName)) {

                    values.add(a.getValue());
                    continue outer;
                }
            }

            //
            // attribute not found
            //

            //
            // simulates real behavior
            //
            throw new ReflectionException(new AttributeNotFoundException(
                    "Could not find any attribute matching: " + attributeName));
        }

        AttributeList result = new AttributeList(values.size());

        for(int i = 0; i < values.size(); i ++) {

            result.add(i, new Attribute(attributeNames[i], values.get(i)));
        }

        return result;
    }

    @Override
    public void setAttribute(ObjectName name, Attribute attribute) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
        throw new RuntimeException("setAttribute() NOT YET IMPLEMENTED");
    }

    @Override
    public AttributeList setAttributes(ObjectName name, AttributeList attributes) throws InstanceNotFoundException, ReflectionException, IOException {
        throw new RuntimeException("setAttributes() NOT YET IMPLEMENTED");
    }

    @Override
    public Object invoke(ObjectName name, String operationName, Object[] params, String[] signature) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
        throw new RuntimeException("invoke() NOT YET IMPLEMENTED");
    }

    @Override
    public String getDefaultDomain() throws IOException {
        throw new RuntimeException("getDefaultDomain() NOT YET IMPLEMENTED");
    }

    @Override
    public String[] getDomains() throws IOException {
        throw new RuntimeException("getDomains() NOT YET IMPLEMENTED");
    }

    @Override
    public void addNotificationListener(ObjectName name, NotificationListener listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, IOException {
        throw new RuntimeException("addNotificationListener() NOT YET IMPLEMENTED");
    }

    @Override
    public void addNotificationListener(ObjectName name, ObjectName listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, IOException {
        throw new RuntimeException("addNotificationListener() NOT YET IMPLEMENTED");
    }

    @Override
    public void removeNotificationListener(ObjectName name, ObjectName listener) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
        throw new RuntimeException("removeNotificationListener() NOT YET IMPLEMENTED");
    }

    @Override
    public void removeNotificationListener(ObjectName name, ObjectName listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
        throw new RuntimeException("removeNotificationListener() NOT YET IMPLEMENTED");
    }

    @Override
    public void removeNotificationListener(ObjectName name, NotificationListener listener) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
        throw new RuntimeException("removeNotificationListener() NOT YET IMPLEMENTED");
    }

    @Override
    public void removeNotificationListener(ObjectName name, NotificationListener listener, NotificationFilter filter, Object handback) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
        throw new RuntimeException("removeNotificationListener() NOT YET IMPLEMENTED");
    }

    @Override
    public MBeanInfo getMBeanInfo(ObjectName name) throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException {
        throw new RuntimeException("getMBeanInfo() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isInstanceOf(ObjectName name, String className) throws InstanceNotFoundException, IOException {
        throw new RuntimeException("isInstanceOf() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return "MockMBeanServerConnection[" + Integer.toHexString(System.identityHashCode(this)) + "]";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
