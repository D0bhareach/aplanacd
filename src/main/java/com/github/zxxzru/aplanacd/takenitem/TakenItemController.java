package com.github.zxxzru.aplanacd.takenitem;

import java.util.Iterator;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    class TakenItemController {

        private final TakenItemRepository repository;

        TakenItemController(TakenItemRepository repository) {
            this.repository = repository;
        }

        @GetMapping("/rented")
        List<TakenItem> getAll(){
            return repository.findAll();
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
