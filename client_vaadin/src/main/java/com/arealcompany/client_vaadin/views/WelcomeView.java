package com.arealcompany.client_vaadin.views;

import com.arealcompany.client_vaadin.Business.AppController;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

@Route("")
public class WelcomeView extends BaseLayout {

    private final AppController appController;

    public WelcomeView(AppController appController) {
        super();
        String message = appController.getWelcomeMessage();
        H2 h1 = new H2(message);
        h1.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        content.add(h1);

        appController.setUsername("admin");
        appController.setPassword("adminpass");
        this.appController = appController;
    }
}
