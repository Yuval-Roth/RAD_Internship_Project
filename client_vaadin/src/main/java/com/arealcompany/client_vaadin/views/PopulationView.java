package com.arealcompany.client_vaadin.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.util.Map;

@Route("population")
public class PopulationView extends BaseLayout {
    public PopulationView() {
        super();

        H2 h1 = new H2("Population");
        h1.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        content.add(h1);
    }
}
