package com.github.zxxzru.aplanacd.takenitem;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
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
    class TakenItemController {

        private final TakenItemRepository repository;
        // private final TakenItemResourceAssembler assembler;

        TakenItemController(TakenItemRepository repository) {
            this.repository = repository;
          //  this.assembler = assembler;
        }

        @GetMapping("/disk/taken")
        List<TakenItem> getAll(){
            return repository.findAll();
        }

        @PostMapping("/disk/take")
        TakenItem newTakenItem(@RequestBody TakenItem newTakenItem) {
// TODO: it must be impossible to take disk if it's already taken
            // Resource<TakenItem> resource = assembler.toResource
            Long diskId = newTakenItem.getDiskId();
            List taken = repository.getTakenItemList(diskId);
            if (taken.size() > 0) {
                throw new ItemTakenException(diskId);
            }
            return repository.save(newTakenItem);

            // return ResponseEntity
            //         .created(new URI(resource.getId().expand().getHref()))
            //         .body(resource);
        }


        @DeleteMapping("/disk/return/{id}")
        void deleteTakenItem(@PathVariable Long id) {
            List<TakenItem> taken = repository.getTakenItemList(id);
            Iterator<TakenItem> itr = taken.iterator();
            while (itr.hasNext()){
                TakenItem item = itr.next();
                repository.delete(item);
            }
        }
}
