package com.arealcompany.client_vaadin.views;

import com.arealcompany.client_vaadin.Business.AppController;
import com.arealcompany.client_vaadin.Business.dtos.Article;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route("news")
public class NewsView extends BaseLayout {
    private final AppController appController;

    public NewsView(AppController appController) {
        super();
        this.appController = appController;

        H2 h1 = new H2("News");
        h1.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        content.add(h1);

        Dialog dialog = new Dialog();
        TextArea textArea = new TextArea();
        dialog.add(textArea);
        dialog.setWidthFull();
        dialog.setHeight("500px");
        textArea.setWidthFull();
        textArea.setReadOnly(true);

        List<Article> articles = appController.getArticles();

        VirtualList<Article> virtualList = new VirtualList<>();
        virtualList.setItems(articles);
        virtualList.setHeight("500px");

        ComponentRenderer<Component,Article> renderer = new ComponentRenderer<>(
                article -> {
                    VerticalLayout layout = new VerticalLayout();
                    layout.getStyle().set("border","1px solid black");
                    layout.getStyle().setBackgroundColor("lightgray");
                    layout.getStyle().set("padding","10px");
                    layout.getStyle().setBorderRadius("10px");
                    layout.getStyle().setMarginBottom("10px");
                    HorizontalLayout titleRow = new HorizontalLayout();
                    Icon icon = new Icon(VaadinIcon.NEWSPAPER);
                    H4 title = new H4(article.title());
                    title.getStyle().set("cursor","pointer");
                    Span description = new Span(article.description());
                    description.setSizeFull();
                    description.getStyle().setMaxWidth("500px");
                    titleRow.add(icon,title);
                    title.addAttachListener(_ ->
                        title.addClickListener(_ ->
                            getUI().ifPresent(ui -> ui.getPage()
                                    .executeJs("window.open('"+article.url()+"','_blank')"))));

                    String[] timeStr = article.publishedAt().replace("Z","").split("T");
                    Span publishedAt = new Span("Published at "+ timeStr[0] + " " + timeStr[1]);
                    Span source = new Span("Source: "+article.source().get("name"));
                    layout.add(titleRow,description,publishedAt,source);
                    return layout;
                }
        );
        virtualList.setRenderer(renderer);

        content.add(virtualList,dialog);
    }
}
