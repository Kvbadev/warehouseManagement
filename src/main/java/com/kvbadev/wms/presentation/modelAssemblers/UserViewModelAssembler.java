package com.kvbadev.wms.presentation.modelAssemblers;

import com.kvbadev.wms.presentation.controllers.UsersController;
import com.kvbadev.wms.presentation.dataTransferObjects.UserView;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserViewModelAssembler implements RepresentationModelAssembler<UserView, EntityModel<UserView>> {
    @Override
    public EntityModel<UserView> toModel(UserView user) {
        Links links = Links.of(
                linkTo(methodOn(UsersController.class).getUser(user.getId())).withSelfRel(),
                linkTo(methodOn(UsersController.class).getUsers()).withRel("users")
        );
        return EntityModel.of(user,links);
    }
}
