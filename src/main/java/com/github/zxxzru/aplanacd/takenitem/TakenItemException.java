package com.github.zxxzru.aplanacd.takenitem;

public class TakenItemException extends RuntimeException {

    public TakenItemException(Long id) {
        super(String.format("Disk with id %d is already taken. ", id));
    }
}
