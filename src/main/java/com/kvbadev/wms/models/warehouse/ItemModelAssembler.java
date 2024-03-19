package com.kvbadev.wms.models.warehouse;

import com.kvbadev.wms.controllers.ItemsController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ItemModelAssembler implements RepresentationModelAssembler<Item, EntityModel<Item>> {
    @Override
    public EntityModel<Item> toModel(Item item) {
        return EntityModel.of(item,
                linkTo(methodOn(ItemsController.class).getItem(item.getId())).withSelfRel(),
                linkTo(methodOn(ItemsController.class).getItems()).withRel("items"));
    }
}
