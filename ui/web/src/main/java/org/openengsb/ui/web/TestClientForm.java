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
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.openengsb.core.config.ServiceManager;
import org.openengsb.ui.web.service.ManagedServices;

import java.util.Arrays;
import java.util.List;

public class TestClientForm extends Form {


    @SpringBean
    ManagedServices managedServices;

    private TextArea<String> messageContentInput;
    private TextField<String> contextInput;


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
        //input fields
        messageContentInput = new TextArea<String>("testclient.form.messageContent", new Model<String>());
        contextInput = new TextField<String>("testclient.form.messageContext");

        //drop down choices
        DropDownChoice managedServicesDropDownChoice = initManagedServices();
        DropDownChoice<String> operationDropDownChoice = initMethods();


        Button sendButton = new Button("testclient.form.submit");
        Button resetButton = new Button("testclient.form.reset") {
            @Override
            public void onSubmit() {
                // just reload this page
                setResponsePage(TestClientPage.class);
            }
        }.setDefaultFormProcessing(false);

        add(managedServicesDropDownChoice);
        add(operationDropDownChoice);
        add(contextInput);
        add(messageContentInput);
        add(sendButton);
        add(resetButton);
    }

    private DropDownChoice initManagedServices() {

        IChoiceRenderer renderer = new ChoiceRenderer() {
            public Object getDisplayValue(Object obj) {
                return ((ServiceManager) obj).getDescriptor().getName();
            }

            public String getIdValue(Object obj, int index) {
                return (String)((ServiceManager) obj).getDescriptor().getId();
            }
        };
        DropDownChoice<ServiceManager> managedServicesDropDownChoice = new DropDownChoice<ServiceManager>("testclient.form.managedServices", managedServices.getManagedServices());
        managedServicesDropDownChoice.setChoiceRenderer(renderer);
        return managedServicesDropDownChoice;
    }

    private DropDownChoice<String> initMethods() {
        //TODO load methods to be tested
        List<String> methods = Arrays.asList("Load methods");
        return new DropDownChoice<String>("testclient.form.operations", methods);
    }


    @Override
    public void onSubmit() {

        String messageContent = messageContentInput.getInput();
        String context = contextInput.getInput();
        // todo get instance from the selected service
    }

    @Override
    protected void onError() {
        setResponsePage(InternalErrorPage.class);
    }

}
