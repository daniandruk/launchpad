package org.daniandruk.launchpad.client;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Property;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ColorPicker;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.daniandruk.launchpad.model.Item;
import org.daniandruk.launchpad.rest.Endpoint;

@SpringUI(path = "/client")
@Theme("valo")
public class MainUI extends UI {

    @Inject
    Endpoint endpoint;

    public MainUI() {
    }

    @Override
    protected void init(VaadinRequest request) {
        Label label = new Label("Technical Tests: Challenge 1 – Algorithm-focussed");
        label.addStyleName("h1");
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        HorizontalLayout searchLayout = new HorizontalLayout();
        searchLayout.setWidth("100%");
        TextField queryField = new TextField("query");

        VerticalLayout colorLayout = new VerticalLayout();

        CheckBox colorCheckBox = new CheckBox("Include color");
        colorLayout.addComponent(colorCheckBox);

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setSwatchesVisibility(false);
        colorPicker.setHistoryVisibility(false);
        colorPicker.setTextfieldVisibility(false);
        colorPicker.setHSVVisibility(false);
        colorPicker.setVisible(false);

        colorLayout.addComponent(colorPicker);

        Button button = new Button("Submit");

        searchLayout.addComponent(queryField);
        searchLayout.addComponent(colorLayout);
        searchLayout.addComponent(button);

        HorizontalLayout itemsLayout = new HorizontalLayout();

        Image[] thumbsnails = new Image[3];
        Link[] links = new Link[3];
        VerticalLayout[] itemLayouts = new VerticalLayout[3];

        for (int i = 0; i < 3; i++) {
            thumbsnails[i] = new Image();
            links[i] = new Link();
            itemLayouts[i] = new VerticalLayout();
            itemLayouts[i].setSpacing(true);
            itemLayouts[i].addComponent(thumbsnails[i]);
            itemLayouts[i].addComponent(links[i]);
        }

        itemsLayout.setWidth("100%");
        itemsLayout.setSpacing(true);
        itemsLayout.addComponents(itemLayouts);

        mainLayout.addComponent(label);
        mainLayout.addComponent(searchLayout);
        mainLayout.addComponent(itemsLayout);

        colorCheckBox.addValueChangeListener((Property.ValueChangeEvent event) -> {
            Object value = event.getProperty().getValue();
            boolean isCheck = (null == value) ? false : (Boolean) value;
            colorPicker.setVisible(isCheck);
        });

        button.addClickListener((Button.ClickEvent event) -> {
            String value = queryField.getValue();
            if (value == null || value.isEmpty()) {
                Notification.show("Try Again",
                        "Please enter the query string",
                        Notification.Type.HUMANIZED_MESSAGE);
            } else {
                List<Item> items = new ArrayList<>();
                Color color = null;
                if (colorPicker.isVisible()) {
                    color = colorPicker.getColor();
                }
                if (color != null) {
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();
                    items.addAll(endpoint.rgb(queryField.getValue(), red, green, blue));
                } else {
                    items.addAll(endpoint.search(queryField.getValue()));
                }
                setupThumbnails(items, thumbsnails, links);
            }
        });

        setContent(mainLayout
        );
    }

    private void setupThumbnails(List<Item> items, Image[] thumbsnails, Link[] links) {
        for (int i = 0; i < 3; i++) {
            try {
                URL thumbnailUrl = new URL(items.get(i).getThumbnail());
                Resource thumbnailResource = new ExternalResource(thumbnailUrl);
                thumbsnails[i].setSource(thumbnailResource);
                URL linklUrl = new URL(items.get(i).getUrl());
                Resource linkResource = new ExternalResource(linklUrl);
                links[i].setResource(linkResource);
                links[i].setCaption("Link the media");
            } catch (MalformedURLException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
