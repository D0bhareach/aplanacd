package com.github.zxxzru.aplanacd.disk;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import org.springframework.http.ResponseEntity;

@RestController
class DiskController {

    private final DiskRepository repository;
    private final DiskResourceAssembler assembler;

    DiskController(DiskRepository repository, DiskResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // Aggregate root

    @GetMapping("/disk")
    Resources<Resource<Disk>> all() {
        List<Resource<Disk>>disks = repository.findAll()
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(disks, assembler.allToResource());
    }

    @PostMapping("/disk")
   ResponseEntity<?> newDisk(@RequestBody Disk newDisk) throws URISyntaxException {
        Resource<Disk> resource = assembler.toResource(repository.save(newDisk));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    // Single item

    @GetMapping("/disk/{id}")
    Resource<Disk> one(@PathVariable Long id) {

        Disk disk = repository.findById(id)
                .orElseThrow(() -> new DiskNotFoundException(id));
        return assembler.toResource(disk);
    }

    @PutMapping("/disk/{id}")
    ResponseEntity<?>replaceDisk(@RequestBody Disk newDisk, @PathVariable Long id) throws URISyntaxException {

        Disk dsk = repository.findById(id)
                .map(disk -> {
                    disk.setName(newDisk.getName());
                    disk.setCompany(newDisk.getCompany());
                    disk.setYear(newDisk.getYear());
                    disk.setUser(newDisk.getUser());
                    return repository.save(disk);
                })
                .orElseGet(() -> {
                    newDisk.setId(id);
                    return repository.save(newDisk);
                });
        Resource<Disk> resource = assembler.toResource(dsk);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/disk/{id}")
    ResponseEntity<?> deleteDisk(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
