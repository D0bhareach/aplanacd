package com.github.zxxzru.aplanacd.user;

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
class UserController {

    private final UserRepository repository;
    private final UserResourceAssembler assembler;

    UserController(UserRepository repository, UserResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // Aggregate root

    @GetMapping("/user")
    Resources<Resource<User>> all() {
        List<Resource<User>> users = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(users, assembler.allToResource());
    }

    @PostMapping("/user")
    ResponseEntity<?> newUser(@RequestBody User newUser) throws URISyntaxException {

        Resource<User> resource = assembler.toResource(repository.save(newUser));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    // Single item

    @GetMapping("/user/{id}")
    Resource<User> one(@PathVariable Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return assembler.toResource(user);
    }

    @PutMapping("/user/{id}")
    ResponseEntity<?> replaceUser(@RequestBody User newUser, @PathVariable Long id)throws URISyntaxException {

        Resource<User> resource = assembler.toResource(repository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setAddress(newUser.getAddress());
                    user.setLogin(newUser.getLogin());
                    user.setPassword(newUser.getPassword());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                }));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/user/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
