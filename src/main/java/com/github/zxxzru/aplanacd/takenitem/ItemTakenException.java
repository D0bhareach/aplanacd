package com.github.zxxzru.aplanacd.takenitem;

class ItemTakenException extends RuntimeException {

    ItemTakenException(Long id) {
        super(String.format("Disk with id %d is already taken. ", id));
    }
}
