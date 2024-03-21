package com.kvbadev.wms.presentation.modelAssemblers;

import com.kvbadev.wms.presentation.controllers.ItemsController;
import com.kvbadev.wms.presentation.controllers.ParcelsController;
import com.kvbadev.wms.models.warehouse.Parcel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ParcelModelAssembler implements RepresentationModelAssembler<Parcel, EntityModel<Parcel>> {
    @Override
    public EntityModel<Parcel> toModel(Parcel parcel) {
        Links links = Links.of(
                linkTo(methodOn(ParcelsController.class).getParcel(parcel.getId())).withSelfRel(),
                linkTo(methodOn(ParcelsController.class).getParcels()).withRel("parcels")
        );
        if(parcel.getDelivery() != null){
            links = links.and(linkTo(methodOn(ParcelsController.class).getParcel(parcel.getDelivery().getId())).withRel("delivery"));
        }
        return EntityModel.of(parcel,links);
    }
}
