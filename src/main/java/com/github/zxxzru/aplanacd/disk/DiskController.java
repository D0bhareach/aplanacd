package com.github.zxxzru.aplanacd.disk;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.zxxzru.aplanacd.takenitem.TakenItemException;
import com.github.zxxzru.aplanacd.takenitem.TakenItem;
import com.github.zxxzru.aplanacd.user.User;
import org.springframework.data.repository.query.Param;
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
import com.github.zxxzru.aplanacd.takenitem.TakenItemRepository;

@RestController
class DiskController {

    private final DiskRepository repository;
    private final DiskResourceAssembler assembler;
    private final UserRepository userRepository;
    private final TakenItemRepository takenItemRepository;

    DiskController(DiskRepository repository,
                   UserRepository userRepository,
                   DiskResourceAssembler assembler,
                   TakenItemRepository takenItemRepository
                   ) {
        this.repository = repository;
        this.assembler = assembler;
        this.userRepository = userRepository;
        this.takenItemRepository = takenItemRepository;
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

    // Aggregate root

    @GetMapping("/disk")
    Resources<Resource<Disk>> all() {
        List<Resource<Disk>>disks = repository.findAll()
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(disks, assembler.allToResource());
    }

    @GetMapping("/disk/available")
    Resources<Resource<Disk>> allAvailable() {
        List <Resource<Disk>>resDisks = new ArrayList<>();
         List<Disk>disks = repository.getAvailableDisks();
        Iterator<Disk> itr = disks.iterator();
        while (itr.hasNext()){
            Disk d = itr.next();
            resDisks.add(assembler.toResource(d));
        }
        return new Resources<>(resDisks, assembler.allToResource());
    }

    @GetMapping("/disk/rented/from/user/{id}")
    Resources<Resource<Disk>> rentedFromUser(@PathVariable Long id) {
        List <Resource<Disk>>resDisks = new ArrayList<>();
        List<Disk>disks = repository.getRentedFromUser(id);
        Iterator<Disk> itr = disks.iterator();
        while (itr.hasNext()){
            Disk d = itr.next();
            resDisks.add(assembler.toResource(d));
        }
        return new Resources<>(resDisks, assembler.allToResource());
    }

    @GetMapping("/disk/rented")
    Resources<Resource<Disk>> allRented() {
        List <Resource<Disk>>resDisks = new ArrayList<>();
        List<Disk>disks = repository.getAllRented();
        Iterator<Disk> itr = disks.iterator();
        while (itr.hasNext()){
            Disk d = itr.next();
            resDisks.add(assembler.toResource(d));
        }
        return new Resources<>(resDisks, assembler.allToResource());
    }

    @GetMapping("/disk/rented/by/user/{id}")
    Resources<Resource<Disk>> rentedByUser(@PathVariable Long id) {
        List <Resource<Disk>>resDisks = new ArrayList<>();
        List<Disk>disks = repository.getRentedByUser(id);
        Iterator<Disk> itr = disks.iterator();
        while (itr.hasNext()){
            Disk d = itr.next();
            resDisks.add(assembler.toResource(d));
        }
        return new Resources<>(resDisks, assembler.allToResource());
    }

    @PostMapping("/disk")
   ResponseEntity<?> newDisk(@RequestBody Disk newDisk) throws URISyntaxException {
        newDisk = getNotNullDisk(newDisk);
        Resource<Disk> resource = assembler.toResource(repository.save(newDisk));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PostMapping("/disk/rent")
    Resource<Disk> rentDisk(@RequestBody TakenItem newTakenItem) {

        Long userId = newTakenItem.getUserId();
        Long diskId = newTakenItem.getDiskId();
        if (userId == null || diskId == null){
            throw new DiskRentBadRequest(userId, diskId);
        }

        List taken = takenItemRepository.getTakenItemList(diskId);
        if (taken.size() > 0) {
            throw new TakenItemException(diskId);
        }
        takenItemRepository.save(newTakenItem);
        return one(diskId);
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
        Disk oldDisk = repository.findById(id)
                .orElseThrow(() -> new DiskNotFoundException(id));
        if ( newDisk == null ){
            Resource<Disk> resource = assembler.toResource(oldDisk);
            return ResponseEntity
                    .created(new URI(resource.getId().expand().getHref()))
                    .body(resource);
            }
        // get values for Disk from post body
        String name = newDisk.getName();
        name = name != null ? name : oldDisk.getName();
        String company = newDisk.getCompany();
        company = company != null ? company : oldDisk.getCompany();
        int year = 0;
        if (newDisk.getYear() != null) {
            year = newDisk.getYear();
        } else if (oldDisk.getYear() != null){
            year = oldDisk.getYear();
        }
        User user = newDisk.getUser();
        user = user != null ? user : oldDisk.getUser();

        Disk resultDisk = new Disk(name, company, year, user);
        resultDisk.setId(oldDisk.getId());
        // repository.delete(oldDisk);
                    resultDisk = repository.save(resultDisk);
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
