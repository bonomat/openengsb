/**

   Copyright 2010 OpenEngSB Division, Vienna University of Technology

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.openengsb.ui.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.openengsb.core.config.DomainProvider;
import org.openengsb.core.config.ServiceManager;
import org.openengsb.core.config.descriptor.ServiceDescriptor;
import org.openengsb.ui.web.service.DomainService;
import org.openengsb.ui.web.service.ManagedServices;
import org.osgi.framework.ServiceReference;

public class Index extends BasePage {

    @SpringBean
    DomainService domainService;

    @SpringBean
    ManagedServices managedServices;

    @SuppressWarnings("serial")
	public Index() {
        add(new Link("lang.en") {
            @Override
            public void onClick() {
                this.getSession().setLocale(Locale.ENGLISH);
            }
        });
        add(new Link("lang.de") {
            @Override
            public void onClick() {
                this.getSession().setLocale(Locale.GERMAN);
            }
        });
        add(new ListView<DomainProvider>("domains", domainService.getDomains()) {
            @Override
            protected void populateItem(ListItem<DomainProvider> item) {
                item.add(new Label("domain.name", item.getModelObject().getName(item.getLocale())));
                item.add(new Label("domain.description", item.getModelObject().getDescription(item.getLocale())));
                item.add(new Label("domain.class", item.getModelObject().getDomainInterface().getName()));
            }
        });
        add(new ListView<ServiceManager>("services", managedServices.getManagedServices()) {
            @Override
            protected void populateItem(ListItem<ServiceManager> item) {
                ServiceDescriptor desc = item.getModelObject().getDescriptor(item.getLocale());
                item.add(new Link<ServiceManager>("create.new", item.getModel()) {
                    @Override
                    public void onClick() {
                        setResponsePage(new EditorPage(getModelObject()));
                    }
                });
                item.add(new Label("service.name", desc.getName()));
                item.add(new Label("service.description", desc.getDescription()));
            }
        });
        managedServices.getManagedServices().get(0).update("test", new HashMap<String, String>());
        add(new ListView<ServiceReference>("instances", managedServices.getManagedServiceInstances()) {
            @Override
            protected void populateItem(final ListItem<ServiceReference> item) {
                item.add(new ListView<String>("properties", Arrays.asList(item.getModelObject().getPropertyKeys())) {
                    @Override
                    protected void populateItem(ListItem<String> keyItem) {
                        keyItem.add(new Label("key", keyItem.getModelObject()));
                        keyItem.add(new Label("value", item.getModelObject().getProperty(keyItem.getModelObject())
                                .toString()));
                    }
                });
            }
        });

        add(new BookmarkablePageLink("testclient", TestClientPage.class));

    }

}
