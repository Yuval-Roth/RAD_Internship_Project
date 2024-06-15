package com.arealcompany.client_vaadin.views;

import com.arealcompany.client_vaadin.Business.AppController;
import com.arealcompany.client_vaadin.Business.Endpoints;
import com.arealcompany.client_vaadin.Business.dtos.Player;
import com.arealcompany.client_vaadin.Business.dtos.User;
import com.arealcompany.client_vaadin.generics.ListGenericView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.Map;


@Route("nba/players")
public class NbaPlayersView extends BaseLayout {

    public NbaPlayersView(AppController appController) {
        super(appController);

        H2 h1 = new H2("NBA Players");
        h1.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        content.add(h1);

        if(!User.isUserLoggedIn){
            openLoginDialog();
            return;
        }

        ListGenericView<Player> listGenericView = new ListGenericView<>(
                appController,
                Player.class,
                Map.of("fetch", Endpoints.GET_PLAYERS,
                        "update" , Endpoints.UPDATE_PLAYER,
                        "delete", Endpoints.DELETE_PLAYER),
                List.of("id", "firstname", "lastname", "height", "birth", "affiliation"))
                .setErrorHandler(this::openErrorDialog)
                .setFilter((fieldMap,otherEntity) -> {
                    String id = fieldMap.apply("id");
                    String firstname = fieldMap.apply("firstname");
                    String lastname = fieldMap.apply("lastname");
                    return (id == null || otherEntity.id().toString().contains(id)) &&
                    (firstname == null || otherEntity.firstname().toLowerCase().contains(firstname.toLowerCase())) &&
                    (lastname == null || otherEntity.lastname().toLowerCase().contains(lastname.toLowerCase()));
                },
                List.of("id", "firstname", "lastname"))
                .setFieldConverter("birth", obj -> {
                    Map<String,String> birth = (Map<String, String>) obj;
                    return birth.get("date");
                })
                .setFieldConverter("height", obj -> {
                    Map<String,String> height = (Map<String, String>) obj;
                    return height.get("meters");
                });
        listGenericView.getGrid().setHeight("500px");
        content.add(listGenericView);
    }
}
