package com.github.zxxzru.aplanacd.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
class UserResourceAssembler implements ResourceAssembler<User, Resource<User>> {

    @Override
    public Resource<User> toResource(User employee) {

        return new Resource<>(employee,
                linkTo(methodOn(UserController.class).one(employee.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("user"));
    }

    Link allToResource() {
        return linkTo(methodOn(UserController.class).all()).withRel("user");
    }
}
