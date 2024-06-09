package com.arealcompany.client_vaadin.views;

import com.arealcompany.client_vaadin.Business.AppController;
import com.arealcompany.client_vaadin.Business.dtos.Player;
import com.arealcompany.client_vaadin.Business.dtos.User;
import com.arealcompany.client_vaadin.exceptions.ApplicationException;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.function.Consumer;


@Route("nba/players")
public class NbaPlayersView extends BaseLayout {

    private final AppController appController;

    public NbaPlayersView(AppController appController) {
        super(appController);
        this.appController = appController;

        H2 h1 = new H2("NBA Players");
        h1.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        content.add(h1);

        if(!User.isUserLoggedIn){
            openLoginDialog();
            return;
        }

        try{

            List<Player> nbaPlayers = appController.getNbaPlayers();
            Grid<Player> grid = new Grid<>(Player.class,false);
            Grid.Column<Player> playerC = grid.addColumn(Player::id).setHeader("ID").setAutoWidth(true).setFlexGrow(0).setSortable(true);
            Grid.Column<Player> firstNameC = grid.addColumn(Player::firstname).setHeader("First Name").setAutoWidth(true).setFlexGrow(0).setSortable(true);
            Grid.Column<Player> lastNameC = grid.addColumn(Player::lastname).setHeader("Last Name").setAutoWidth(true).setFlexGrow(0).setSortable(true);
            grid.addColumn(Player::height).setHeader("Height").setAutoWidth(true).setFlexGrow(0).setSortable(true);
            grid.addColumn(Player::birth).setHeader("Birth").setAutoWidth(true).setFlexGrow(0).setSortable(true);
            grid.addColumn(Player::affiliation).setHeader("Affiliation").setSortable(true);

            GridListDataView<Player> dataView = grid.setItems(nbaPlayers);
            grid.setWidth("100%");
            grid.setHeight(500, Unit.PIXELS);

            PlayerFilter playerFilter = new PlayerFilter(dataView);

            HeaderRow hr = grid.appendHeaderRow();
            hr.getCell(playerC).setComponent(createFilterHeader(playerFilter::setId));
            hr.getCell(firstNameC).setComponent(createFilterHeader(playerFilter::setFirstName));
            hr.getCell(lastNameC).setComponent(createFilterHeader(playerFilter::setLastName));


            content.add(grid);
        } catch (ApplicationException e) {
            openErrorDialog(e.getMessage());
        }
    }

    private static class PlayerFilter {
        private final GridListDataView<Player> dataView;

        private String id;
        private String firstName;
        private String lastName;

        public void setId(String id) {
            this.id = id;
            dataView.refreshAll();
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
            dataView.refreshAll();
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
            dataView.refreshAll();
        }

        public PlayerFilter(GridListDataView<Player> dataView){
            this.dataView = dataView;
            dataView.setFilter(this::filter);
        }

        public boolean filter(Player player){
            return (id == null || player.id().toString().contains(id)) &&
                    (firstName == null || player.firstname().toLowerCase().contains(firstName.toLowerCase())) &&
                    (lastName == null || player.lastname().toLowerCase().contains(lastName.toLowerCase()));
        }
    }
}
