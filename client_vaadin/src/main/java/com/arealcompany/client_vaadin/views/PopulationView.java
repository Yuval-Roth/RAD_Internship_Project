package com.arealcompany.client_vaadin.views;

import com.arealcompany.client_vaadin.Business.AppController;
import com.arealcompany.client_vaadin.Business.dtos.Player;
import com.arealcompany.client_vaadin.Business.dtos.PopulationStat;
import com.arealcompany.client_vaadin.exceptions.ApplicationException;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route("population")
public class PopulationView extends BaseLayout {
    private final AppController appController;

    public PopulationView(AppController appController) {
        super();
        this.appController = appController;

        H2 h1 = new H2("Population");
        h1.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        content.add(h1);

        List<PopulationStat> popStats = null;
        try {
            popStats = appController.getPopulationStats();
            Grid<PopulationStat> grid = new Grid<>(PopulationStat.class, false);
            Grid.Column<PopulationStat> countryC = grid.addColumn(PopulationStat::country).setHeader("Country").setSortable(true);
            Grid.Column<PopulationStat> numberC = grid.addColumn(PopulationStat::readable_format).setHeader("Count").setSortable(true);

            HeaderRow hr = grid.appendHeaderRow();
            PopulationFilter populationFilter = new PopulationFilter(grid.setItems(popStats));
            hr.getCell(countryC).setComponent(createFilterHeader(populationFilter::setCountry));
            hr.getCell(numberC).setComponent(createFilterHeader(populationFilter::setCount));

            grid.setWidth("50%");
            grid.setHeight("500px");
            grid.getStyle().setAlignSelf(Style.AlignSelf.CENTER);

            content.add(grid);
        } catch (ApplicationException e) {
            openErrorDialog(e.getMessage());
        }
    }

    private static class PopulationFilter {
        private final GridListDataView<PopulationStat> dataView;
        private String country;
        private String count;

        public PopulationFilter(GridListDataView<PopulationStat> dataView) {
            this.dataView = dataView;
            dataView.addFilter(this::filter);
        }

        public void setCountry(String country) {
            this.country = country;
            dataView.refreshAll();
        }

        public void setCount(String count) {
            this.count = count;
            dataView.refreshAll();
        }

        public boolean filter(PopulationStat populationStat) {
            return (country == null || (populationStat.country() != null && populationStat.country().toLowerCase().contains(country.toLowerCase())))
                    && (count == null || populationStat.readable_format().contains(count));
        }
    }
}
