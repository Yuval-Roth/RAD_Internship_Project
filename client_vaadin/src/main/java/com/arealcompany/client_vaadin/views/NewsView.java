package com.arealcompany.client_vaadin.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.util.Map;

@Route("news")
public class NewsView extends BaseLayout {
    public NewsView() {
        super();


        H2 h1 = new H2("News");
        h1.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        content.add(h1);
    }
}
