package com.example.service.adoptions;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
class AdoptionsView extends VerticalLayout {

    AdoptionsView(DogAdoptionService service) {
        setSizeFull();
        var dogGrid = new Grid<>(Dog.class, false);
        dogGrid.setSizeFull();
        dogGrid.setItems(service.findAll());

        dogGrid.addComponentColumn(dog -> {
            var image = new Image(dog.image(), dog.name());
            image.setWidth("100px");
            return image;
        }).setHeader("Image");

        dogGrid.addColumn(Dog::name).setHeader("Name");
        dogGrid.addColumn(Dog::description).setHeader("Description");
        dogGrid.addColumn(Dog::owner).setHeader("Owner");
        dogGrid.addComponentColumn(dog -> {
            var adopter = new TextField();
            var adoptButton = new Button("Adopt", event -> {
                service.adopt(dog.id(), adopter.getValue());
                dogGrid.setItems(service.findAll());
            });
            return new HorizontalLayout(adopter, adoptButton);
        });

        dogGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        add(dogGrid);
    }
}
