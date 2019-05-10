/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.mqtt.homie.internal.homie300;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.eclipse.smarthome.core.thing.ChannelGroupUID;
import org.eclipse.smarthome.core.thing.type.ChannelKind;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openhab.binding.mqtt.homie.internal.homie300.PropertyAttributes.DataTypeEnum;

public class PropertyTests {
    @Mock
    private Node node;

    @Mock
    private DeviceCallback callback;

    @Before
    public void setUp() {
        initMocks(this);
        when(node.uid()).thenReturn(new ChannelGroupUID("mqtt:homie300:device:node"));
    }

    @Test
    public void stateChannelTypeTest() {
        Property property = createGenericProperty();

        property.attributes.retained = true;

        property.attributesReceived();
        assertThat(property.getType().getKind(), is(ChannelKind.STATE));
    }

    @Test
    public void triggerChannelTypeTest() {
        Property property = createGenericProperty();

        property.attributes.retained = false;

        property.attributesReceived();
        assertThat(property.getType().getKind(), is(ChannelKind.TRIGGER));
    }

    @Test
    public void rawButtonChannelTypeTest() {
        Property property = createGenericProperty();

        property.attributes.retained = false;
        property.attributes.datatype = DataTypeEnum.enum_;
        property.attributes.format = "PRESSED,RELEASED";

        property.attributesReceived();
        assertThat(property.getType().getKind(), is(ChannelKind.TRIGGER));
        assertThat(property.getType().getUID().getId(), is("rawbutton"));
    }

    @Test
    public void buttonChannelTypeTest() {
        Property property = createGenericProperty();

        property.attributes.retained = false;
        property.attributes.datatype = DataTypeEnum.enum_;
        property.attributes.format = "SHORT_PRESSED,DOUBLE_PRESSED,LONG_PRESSED";

        property.attributesReceived();
        assertThat(property.getType().getKind(), is(ChannelKind.TRIGGER));
        assertThat(property.getType().getUID().getId(), is("button"));
    }

    @Test
    public void rawRockerChannelTypeTest() {
        Property property = createGenericProperty();

        property.attributes.retained = false;
        property.attributes.datatype = DataTypeEnum.enum_;
        property.attributes.format = "DIR1_PRESSED,DIR1_RELEASED,DIR2_PRESSED,DIR2_RELEASED";

        property.attributesReceived();
        assertThat(property.getType().getKind(), is(ChannelKind.TRIGGER));
        assertThat(property.getType().getUID().getId(), is("rawrocker"));
    }

    public Property createGenericProperty() {
        PropertyAttributes attrs = spy(new PropertyAttributes());
        return new Property("/homie/device/node", this.node, "property", this.callback, attrs);
    }
}