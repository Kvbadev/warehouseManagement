package com.kvbadev.wms.presentation.modelAssemblers;

import com.kvbadev.wms.presentation.controllers.DeliveriesController;
import com.kvbadev.wms.presentation.controllers.ItemsController;
import com.kvbadev.wms.models.warehouse.Item;
import com.kvbadev.wms.presentation.controllers.ParcelsController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ItemModelAssembler implements RepresentationModelAssembler<Item, EntityModel<Item>> {
    @Override
    public EntityModel<Item> toModel(Item item) {
        Links links = Links.of(
                linkTo(methodOn(ItemsController.class).getItem(item.getId())).withSelfRel(),
                linkTo(methodOn(ItemsController.class).getItems()).withRel("items")
        );
        if(item.getParcel() != null){
            links = links.and(linkTo(methodOn(ParcelsController.class).getParcel(item.getParcel().getId())).withRel("parcel"));
        }
        return EntityModel.of(item,links);
    }
}
