package com.arealcompany.client_vaadin.views;

import com.arealcompany.client_vaadin.Business.AppController;
import com.arealcompany.client_vaadin.Business.dtos.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@PageTitle("RAD internship project")
@Component
public abstract class BaseLayout extends AppLayout {

    protected final VerticalLayout content;
    private final AppController appController;

    public BaseLayout(AppController appController) {
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

        if(! User.isUserLoggedIn){
            Button loginButton = new Button("Login", event -> openLoginDialog());
            loginButton.getStyle().setMarginLeft("75%");
            addToNavbar(loginButton);
        } else {
            H4 username = new H4("Hello, " + User.currentUser.username());
            username.getStyle().set("margin-left", "65%");
            Button logoutButton = new Button("Logout", event -> {
                User.isUserLoggedIn = false;
                User.currentUser = null;
                UI.getCurrent().getPage().setLocation("/");
            });
            logoutButton.getStyle().set("margin-left", "50px");
            addToNavbar(username, logoutButton);
        }
        this.appController = appController;
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

    protected void openLoginDialog() {
        Dialog dialog = new Dialog();
        VerticalLayout layout = new VerticalLayout();
        FormLayout formLayout = new FormLayout();
        TextField usernameField = new TextField("Username");
        usernameField.setPlaceholder("Username");
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setPlaceholder("Password");

        Button submitButton = new Button("Submit", event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            // Perform login logic here
            if (appController.login(username, password)) {
                Notification.show("Login successful");
                dialog.close();
                UI.getCurrent().getPage().reload();
            } else {
                Notification.show("Login failed");
            }
        });
        Button cancelButton = new Button("Cancel", event -> dialog.close());

        formLayout.add(usernameField, passwordField, submitButton, cancelButton);
        formLayout.setWidth("50%");
        formLayout.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        layout.add(formLayout);
        dialog.add(layout);
        dialog.addOpenedChangeListener(event -> {
            if (!event.isOpened()) {
                UI.getCurrent().getPage().setLocation("/");
            }
        });

        content.add(dialog);
        dialog.open();

    }

    protected void openErrorDialog(String message) {
        Dialog dialog = new Dialog();
        VerticalLayout layout = new VerticalLayout();
        H1 h1 = new H1("Error");
        H4 h4 = new H4(message);
        Button closeButton = new Button("Close", event -> dialog.close());
        layout.add(h1, h4, closeButton);
        dialog.add(layout);
        content.add(dialog);
        dialog.open();
    }
}
