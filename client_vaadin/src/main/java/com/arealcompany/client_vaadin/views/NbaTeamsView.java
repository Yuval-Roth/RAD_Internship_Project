package com.arealcompany.client_vaadin.views;

import com.arealcompany.client_vaadin.Business.AppController;
import com.arealcompany.client_vaadin.Business.Endpoints;
import com.arealcompany.client_vaadin.Business.dtos.Team;
import com.arealcompany.client_vaadin.Business.dtos.User;
import com.arealcompany.client_vaadin.exceptions.ApplicationException;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.Map;

@Route("nba/teams")
public class NbaTeamsView extends BaseLayout {

    private final AppController appController;

    public NbaTeamsView(AppController appController) {
        super(appController);
        this.appController = appController;

        H2 h1 = new H2("NBA Teams");
        h1.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        content.add(h1);

        if(!User.isUserLoggedIn){
            openLoginDialog();
            return;
        }

        try{
            List<Team> nbaTeams = appController.fetchByEndpoint(Endpoints.GET_TEAMS);
            Grid<Team> grid = new Grid<>(Team.class,false);
            Grid.Column<Team> idC = grid.addColumn(Team::id).setHeader("ID").setAutoWidth(true).setFlexGrow(0).setSortable(true);
            Grid.Column<Team> nameC = grid.addColumn(Team::name).setHeader("Name").setAutoWidth(true).setFlexGrow(0).setSortable(true);
            Grid.Column<Team> nicknameC = grid.addColumn(Team::nickname).setHeader("Nickname").setAutoWidth(true).setFlexGrow(0).setSortable(true);
            grid.addColumn(Team::code).setHeader("Code").setAutoWidth(true).setFlexGrow(0).setSortable(true);
            grid.addColumn(Team::city).setHeader("City").setAutoWidth(true).setFlexGrow(0).setSortable(true);
            grid.addColumn(Team::allStar).setHeader("All Star").setAutoWidth(true).setFlexGrow(0).setSortable(true);
            grid.addColumn(Team::nbaFranchise).setHeader("NBA Franchise").setSortable(true);


            GridListDataView<Team> dataView = grid.setItems(nbaTeams);
            grid.setWidth("100%");
            grid.setHeight(500, Unit.PIXELS);

            TeamFilter teamFilter = new TeamFilter(dataView);
            HeaderRow hr = grid.appendHeaderRow();
            hr.getCell(idC).setComponent(createFilterHeader(teamFilter::setId));
            hr.getCell(nameC).setComponent(createFilterHeader(teamFilter::setName));
            hr.getCell(nicknameC).setComponent(createFilterHeader(teamFilter::setNickname));

            content.add(grid);
        } catch (ApplicationException e) {
            openErrorDialog(e.getMessage());
        }
    }

    private static class TeamFilter {
        private final GridListDataView<Team> dataView;
        private String id;
        private String name;
        private String nickname;

        public TeamFilter(GridListDataView<Team> dataView) {
            this.dataView = dataView;
            dataView.setFilter(this::filter);
        }

        public void setId(String id) {
            this.id = id;
            dataView.refreshAll();
        }

        public void setName(String name) {
            this.name = name;
            dataView.refreshAll();
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
            dataView.refreshAll();
        }

        public boolean filter(Team team) {
            return (id == null || team.id().toString().toLowerCase().contains(id.toLowerCase())) &&
                    (name == null || team.name().toLowerCase().contains(name.toLowerCase())) &&
                    (nickname == null || team.nickname().toLowerCase().contains(nickname.toLowerCase()));
}
    }
}
