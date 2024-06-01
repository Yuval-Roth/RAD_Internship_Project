package com.arealcompany.client_vaadin.views;

import com.arealcompany.client_vaadin.Business.AppController;
import com.arealcompany.client_vaadin.Business.dtos.Player;
import com.arealcompany.client_vaadin.Business.dtos.PopulationStat;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route("population")
public class PopulationView extends BaseLayout {
    private final AppController appController;

    public PopulationView(AppController appController) {
        super();

        H2 h1 = new H2("Population");
        h1.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        content.add(h1);


        Grid<PopulationStat> grid = new Grid<>(PopulationStat.class, false);
        List<PopulationStat> popStats = appController.getPopulationStats();



        grid.setItems(popStats);

        content.add(grid);
        this.appController = appController;
    }
}
