package com.github.zxxzru.aplanacd.disk;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class DiskController {

    private final DiskRepository repository;

    DiskController(DiskRepository repository) {
        this.repository = repository;
    }

    // Aggregate root

    @GetMapping("/disk")
    List<Disk> all() {
        return repository.findAll();
    }

    @PostMapping("/disk")
    Disk newDisk(@RequestBody Disk newDisk) {
        return repository.save(newDisk);
    }

    // Single item

    @GetMapping("/disk/{id}")
    Disk one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new DiskNotFoundException(id));
    }

    @PutMapping("/disk/{id}")
    Disk replaceDisk(@RequestBody Disk newDisk, @PathVariable Long id) {

        return repository.findById(id)
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
    }

    @DeleteMapping("/disk/{id}")
    void deleteDisk(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
