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

import org.apache.velocity.test.provider.Person;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.openengsb.core.config.ServiceManager;
import org.openengsb.core.config.descriptor.ServiceDescriptor;
import org.openengsb.ui.web.service.ManagedServices;

public class TestClientForm extends Form {


    @SpringBean
    ManagedServices managedServices;

    private String messageContent = "";
    private TextArea<String> messageContentInput;
    private String context = "";
    private TextField<String> contextInput;
    private DropDownChoice dropDownChoice;

    public TestClientForm(String id) {
        super(id);
        messageContentInput = new TextArea<String>("testclientForm.messageContent", new Model<String>());
        messageContentInput.setRequired(true);
        contextInput = new TextField<String>("testclientForm.messageContext");
        contextInput.setRequired(true);

        IModel managedServicesModels = new LoadableDetachableModel() {
            @Override
            public Object load() {
                return managedServices.getManagedServices();
            }
        };
        IChoiceRenderer renderer = new ChoiceRenderer() {
            public Object getDisplayValue(Object obj) {
                ServiceDescriptor desc = ((ServiceManager) obj).getDescriptor();
                return desc.getName();
            }

            public String getIdValue(Object obj, int index) {
                ServiceDescriptor desc = ((ServiceManager) obj).getDescriptor();
                return desc.getServiceInterfaceId();
            }
        };

        dropDownChoice = new DropDownChoice("testclientForm.managedServices", managedServicesModels);
        dropDownChoice.setChoiceRenderer(renderer);

        Button sendButton = new Button("testclientForm.send");
        Button resetButton = new Button("testclientForm.reset") {
            @Override
            public void onSubmit() {
                // just set a new instance of the page
                setResponsePage(TestClientPage.class);
            }
        }.setDefaultFormProcessing(false);

        add(dropDownChoice);
        add(contextInput);
        add(messageContentInput);
        add(sendButton);
        add(resetButton);
    }


    @Override
    protected void onSubmit() {
        messageContent = messageContentInput.getInput();
        context = contextInput.getInput();
        dropDownChoice.getValue();
        // todo get instance from the selected service

    }

    @Override
    protected void onError() {
        // do something special when an error occurs,
        // instead of displaying messages.
    }
}
