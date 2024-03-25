package com.kvbadev.wms.presentation.modelAssemblers;

import com.kvbadev.wms.models.warehouse.Delivery;
import com.kvbadev.wms.presentation.controllers.DeliveriesController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DeliveryModelAssembler implements RepresentationModelAssembler<Delivery, EntityModel<Delivery>> {
    @Override
    public EntityModel<Delivery> toModel(Delivery delivery) {
        Links links = Links.of(
                linkTo(methodOn(DeliveriesController.class).getDelivery(delivery.getId())).withSelfRel(),
                linkTo(methodOn(DeliveriesController.class).getDeliveries()).withRel("deliveries")
        );
        return EntityModel.of(delivery,links);
    }
}
