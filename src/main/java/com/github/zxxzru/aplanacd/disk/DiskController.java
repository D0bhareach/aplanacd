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
        id = id != null && id > 0 ? id : 1;
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.isPresent() ? userOptional.get() :
                userRepository.findById(new Long(1)).get();
                return user;
    }

    private final Disk getNotNullDisk (Disk newDisk){
        System.out.println(newDisk.toString());
        String name = newDisk.getName();
        name = name != null ? name : "";
        String company = newDisk.getCompany();
        company = company != null ? company : "";
        Integer year = newDisk.getYear();
        year = year != null ? year : 0;
        User user =newDisk.getUser();
        Long userId = user != null && user.getId() != null ? user.getId() : 1;
        user = getUserById(userId);

        return new Disk(name, company, year, user);

    }

    @PostMapping("/disk")
   ResponseEntity<?> newDisk(@RequestBody Disk newDisk) throws URISyntaxException {
        // Long userId = newDisk.getUser().getId();
        // User user = getUserById(userId);
        // // TODO: What to do when Optional has nothing?
        // // right now  fake User with ID: 1 is created at start up.
        // newDisk.setUser(user);
        newDisk = getNotNullDisk(newDisk);
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
    ResponseEntity<?>replaceDisk(@RequestBody Disk newDisk, @PathVariable Long id)
            throws URISyntaxException, DiskNotFoundException {
        // Long userId = newDisk.getUser().getId();
        // User user = getUserById(userId);
        // // reset user in case it was not set before
        // newDisk.setUser(user);
        Disk oldDisk = repository.findById(id)
                .orElseThrow(() -> new DiskNotFoundException(id));
        // get values for Disk from post body
        String name = newDisk.getName();
        name = name != null ? name : oldDisk.getName();
        String company = newDisk.getCompany();
        company = company != null ? company : oldDisk.getCompany();
        int year = newDisk.getYear();
        User user = newDisk.getUser();
        user = user != null ? user : oldDisk.getUser();

        Disk resultDisk = new Disk(name, company, year, user);
                // TODO: values below are not necessarily not null
                // .map(disk -> {
                //     disk.setName(newDisk.getName());
                //     disk.setCompany(newDisk.getCompany());
                //     disk.setYear(newDisk.getYear());
                //     disk.setUser(newDisk.getUser());
                    resultDisk = repository.save(resultDisk);
                // })
                // .orElseGet(() -> {
                //     newDisk.setId(id);
                    // oldDisk = repository.save(newDisk);
                // });
        Resource<Disk> resource = assembler.toResource(resultDisk);
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
