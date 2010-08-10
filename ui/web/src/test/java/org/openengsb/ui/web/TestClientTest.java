package org.openengsb.ui.web;
/**
 * Copyright 2010 OpenEngSB Division, Vienna University of Technology
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.openengsb.core.config.DomainProvider;
import org.openengsb.core.config.ServiceManager;
import org.openengsb.core.config.descriptor.ServiceDescriptor;
import org.openengsb.ui.web.service.DomainService;
import org.openengsb.ui.web.service.ManagedServices;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestClientTest {

    public WicketTester wicketTester;
    private TestClientPage tcpage;

    @Before
    public void setUp() {
        wicketTester = new WicketTester();

        TestClientForm tcform = new TestClientForm("testclient.form", mockManagedServices());
        TestClientPanel tcpanel = new TestClientPanel("testclient.panel", tcform);
        tcpage = new TestClientPage(tcpanel);

        wicketTester.startComponent(tcform);
        wicketTester.startComponent(tcpanel);
        wicketTester.startComponent(tcpage);
    }

    @Test
    public void testStartPage() {
        wicketTester.startPage(tcpage);
        wicketTester.assertRenderedPage(tcpage.getClass());
    }

    @Test
    public void testFormSubmit() {
        wicketTester.startPage(tcpage);
        FormTester form = wicketTester.newFormTester("testclient.panel:testclient.form");
        form.setValue("testclient.form.messageContent", "Test Messsage");
        //form.submit();
        //wicketTester.assertRenderedPage(tcpage.getClass());
        
        // TODO write test for form submit
    }


    private DomainService mockDomainServices() {
        DomainService ds = mock(DomainService.class);
        List<DomainProvider> dsList = new ArrayList<DomainProvider>();
        DomainProvider ms = mock(DomainProvider.class);
        when(ms.getName()).thenReturn("DomainName");
        when(ms.getId()).thenReturn("DomainID");
        dsList.add(ms);
        when(ds.getDomains()).thenReturn(dsList);
        return ds;

    }

    private ManagedServices mockManagedServices() {
        ManagedServices ds = mock(ManagedServices.class);
        List<ServiceManager> dsList = new ArrayList<ServiceManager>();
        ServiceManager ms = mock(ServiceManager.class);
        ServiceDescriptor sd = new ServiceDescriptor();
        ServiceDescriptor.Builder builder = sd.builder();
        builder.name("ServiceName");
        builder.id("ServiceID");
        builder.implementsInterface("ImplementsInterfaceID");
        when(ms.getDescriptor()).thenReturn(sd);
        dsList.add(ms);
        when(ds.getManagedServices()).thenReturn(dsList);
        return ds;

    }
}
