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

import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.pages.InternalErrorPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.openengsb.core.config.ServiceManager;
import org.openengsb.ui.web.service.ManagedServices;

import java.util.List;

public class TestClientForm extends Form {


    @SpringBean
    ManagedServices managedServices;

    private TextArea<String> messageContentInput;
    private TextField<String> contextInput;
    private DropDownChoice managedServicesDownChoice;
    private List<ServiceManager> serviceManager;
    
    public TestClientForm(String id) {
        super(id);
        initComponents();
    }

    // for tests
    public TestClientForm(String id, ManagedServices managedServices) {
        super(id);
        this.managedServices = managedServices;
        initComponents();
    }

    private void initComponents() {
        messageContentInput = new TextArea<String>("testclient.form.messageContent", new Model<String>());
        messageContentInput.setRequired(true);
        contextInput = new TextField<String>("testclient.form.messageContext");
        contextInput.setRequired(true);
        serviceManager = managedServices.getManagedServices();

        IModel manageServices = new LoadableDetachableModel() {
            @Override
            public Object load() {
                return serviceManager;
            }
        };
        IChoiceRenderer renderer = new ChoiceRenderer() {
            public Object getDisplayValue(Object obj) {
                return ((ServiceManager) obj).getDescriptor().getName();
            }

            public String getIdValue(Object obj, int index) {
               return ((ServiceManager) obj).getDescriptor().getId();
            }
        };

        managedServicesDownChoice = new DropDownChoice("testclient.form.managedServices", manageServices);
        managedServicesDownChoice.setChoiceRenderer(renderer);

        Button sendButton = new Button("testclient.form.submit");
        Button resetButton = new Button("testclient.form.reset") {
            @Override
            public void onSubmit() {
                // just reload this page
                setResponsePage(TestClientPage.class);
            }
        }.setDefaultFormProcessing(false);

        add(managedServicesDownChoice);
        add(contextInput);
        add(messageContentInput);
        add(sendButton);
        add(resetButton);
    }


    @Override
    public void onSubmit() {

        String messageContent = messageContentInput.getInput();
        String context = contextInput.getInput();
        String domainProvider = managedServicesDownChoice.getValue();

       
        // todo get instance from the selected service

    }

    @Override
    protected void onError() {
         setResponsePage(InternalErrorPage.class);
    }

}
