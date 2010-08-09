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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.openengsb.core.config.DomainProvider;
import org.openengsb.ui.web.service.DomainService;

public class TestClientForm extends Form {

    @SpringBean
    DomainService domainServices;

    private String messageContent = "";
    private TextArea<String> messageContentInput;
    private String context = "";
    private TextField<String> contextInput;
    private DropDownChoice dropDownChoice;

    public TestClientForm(String id) {
        super(id);
        initComponents();
    }

    // for tests
    public TestClientForm(String id, DomainService domainServices) {
        super(id);
        this.domainServices = domainServices;
        initComponents();
    }

    private void initComponents() {
        messageContentInput = new TextArea<String>("testclient.form.messageContent", new Model<String>());
        messageContentInput.setRequired(true);
        contextInput = new TextField<String>("testclient.form.messageContext");
        contextInput.setRequired(true);

        IModel domainProviderModels = new LoadableDetachableModel() {
            @Override
            public Object load() {
                return domainServices.getDomains();
            }
        };
        IChoiceRenderer renderer = new ChoiceRenderer() {
            public Object getDisplayValue(Object obj) {
                return ((DomainProvider) obj).getName();
            }

            public String getIdValue(Object obj, int index) {
                String domainProviderId = ((DomainProvider) obj).getId();
                return domainProviderId;
            }
        };

        dropDownChoice = new DropDownChoice("testclient.form.domainProvider", domainProviderModels);
        dropDownChoice.setChoiceRenderer(renderer);

        Button sendButton = new Button("testclient.form.send");
        Button resetButton = new Button("testclient.form.reset") {
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
        // todo do something special when an error occurs, instead of displaying messages.

    }

}
