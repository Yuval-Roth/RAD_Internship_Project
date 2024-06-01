package com.arealcompany.client_vaadin.views;

import com.arealcompany.client_vaadin.Business.AppController;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@PageTitle("RAD internship project")
@Component
public abstract class BaseLayout extends AppLayout {

    protected final VerticalLayout content;

    public BaseLayout() {
        content = new VerticalLayout();
        setContent(content);

        SideNav nav1 = new SideNav();
        nav1.addItem(new SideNavItem("Welcome", WelcomeView.class, VaadinIcon.HOME.create()));
        nav1.addItem(new SideNavItem("News", NewsView.class, VaadinIcon.NEWSPAPER.create()));
        nav1.addItem(new SideNavItem("Population", PopulationView.class, VaadinIcon.FAMILY.create()));

        SideNav nav2 = new SideNav();
        nav2.setLabel("NBA");
        nav2.addItem(new SideNavItem("Teams", NbaTeamsView.class, VaadinIcon.TROPHY.create()));
        nav2.addItem(new SideNavItem("Players", NbaPlayersView.class, VaadinIcon.NURSE.create()));

        VerticalLayout sideNav = new VerticalLayout();
        sideNav.add(nav1, nav2);

        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("RAD internship project");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");


        Scroller scroller = new Scroller(sideNav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle, title);
    }

    protected static com.vaadin.flow.component.Component createFilterHeader(Consumer<String> filterChangeConsumer) {
        TextField textField = new TextField();
        textField.setPlaceholder("Filter");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        VerticalLayout layout = new VerticalLayout(textField);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");

        return layout;
    }
}
