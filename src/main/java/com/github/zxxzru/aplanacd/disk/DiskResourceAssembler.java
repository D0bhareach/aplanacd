package com.github.zxxzru.aplanacd.disk;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
class DiskResourceAssembler implements ResourceAssembler<Disk, Resource<Disk>> {

    @Override
    public Resource<Disk> toResource(Disk employee) {

        return new Resource<>(employee,
                linkTo(methodOn(DiskController.class).one(employee.getId())).withSelfRel(),
                linkTo(methodOn(DiskController.class).all()).withRel("disk"));
    }

    Link allToResource() {
        return linkTo(methodOn(DiskController.class).all()).withRel("disk");
    }
}
