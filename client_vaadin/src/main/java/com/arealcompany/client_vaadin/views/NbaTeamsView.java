package com.arealcompany.client_vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.util.HashMap;
import java.util.Map;

@Route("nba/teams")
public class NbaTeamsView extends BaseLayout {

    public NbaTeamsView() {
        super();

        H2 h1 = new H2("NBA Teams");
        h1.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        content.add(h1);
    }
}
