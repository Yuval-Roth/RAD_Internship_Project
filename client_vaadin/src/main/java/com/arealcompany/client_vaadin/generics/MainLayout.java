package com.arealcompany.client_vaadin.generics;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.theme.lumo.LumoUtility;
// import org.springframework.stereotype.Component;
// import com.example.frontend.ListPopView;

@SpringComponent
@Route("base")
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Vaadin CRM");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        // Create header layout
        var header = new HorizontalLayout(new DrawerToggle(), logo);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);
    }

    private Button createNavButton(String path, String label) {
        Button button = new Button(label);
        button.addClickListener(e -> UI.getCurrent().navigate(path));
        return button;
    }

    private void createDrawer() {
        VerticalLayout layout = new VerticalLayout();

        // Example paths and labels for buttons
        layout.add(
                createNavButton("/population", "population"),
                createNavButton("/news", "news"),
                createNavButton("/nba/gamesV", "nba:games"),
                createNavButton("/nba/playersV", "nba:players"),
                createNavButton("/nba/teamsV", "nba:teams"));

        // Assuming there's a method 'addToDrawer' that adds components to the drawer
        addToDrawer(layout);
    }

}