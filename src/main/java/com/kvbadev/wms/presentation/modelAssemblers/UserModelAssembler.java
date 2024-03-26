package com.kvbadev.wms.presentation.modelAssemblers;

import com.kvbadev.wms.models.security.User;
import com.kvbadev.wms.presentation.controllers.ItemsController;
import com.kvbadev.wms.presentation.controllers.ParcelsController;
import com.kvbadev.wms.presentation.controllers.UsersController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public EntityModel<User> toModel(User user) {
        Links links = Links.of(
                linkTo(methodOn(UsersController.class).getUser(user.getId())).withSelfRel(),
                linkTo(methodOn(UsersController.class).getUsers()).withRel("users")
        );
        return EntityModel.of(user,links);
    }
}
