package com.arealcompany.client_vaadin.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.util.Map;

@Route("")
public class WelcomeView extends BaseLayout {
    public WelcomeView() {
        super();
        H2 h1 = new H2("Welcome!");
        h1.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        content.add(h1);
    }
}
