package com.github.zxxzru.aplanacd.disk;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.zxxzru.aplanacd.user.User;
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

import com.github.zxxzru.aplanacd.user.UserRepository;

@RestController
class DiskController {

    private final DiskRepository repository;
    private final DiskResourceAssembler assembler;
    private final UserRepository userRepository;

    DiskController(DiskRepository repository,
                   UserRepository userRepository,
                   DiskResourceAssembler assembler
                   ) {
        this.repository = repository;
        this.assembler = assembler;
        this.userRepository = userRepository;
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

    private final User getUserById(Long id){
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.isPresent() ? userOptional.get() :
                userRepository.findById(new Long(1)).get();
                return user;
    }

    @PostMapping("/disk")
   ResponseEntity<?> newDisk(@RequestBody Disk newDisk) throws URISyntaxException {
        Long userId = newDisk.getUser().getId();
        User user = getUserById(userId);
        // TODO: What to do when Optional has nothing?
        // right now  fake User with ID: 1 is created at start up.
        newDisk.setUser(user);
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
        Long userId = newDisk.getUser().getId();
        User user = getUserById(userId);
        // reset user in case it was not set before
        newDisk.setUser(user);
        Disk dsk = repository.findById(id)
                // TODO: values below are not necessarily not null
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
