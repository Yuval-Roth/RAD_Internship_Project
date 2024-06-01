package com.arealcompany.client_vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.util.HashMap;
import java.util.Map;



@Route("nba/players")
public class NbaPlayersView extends BaseLayout {

    public NbaPlayersView() {
        super();
        H2 h1 = new H2("NBA Players");
        h1.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        content.add(h1);

    }
}
