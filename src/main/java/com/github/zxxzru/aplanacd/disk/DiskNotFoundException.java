package com.github.zxxzru.aplanacd.disk;

class DiskNotFoundException extends RuntimeException {

    DiskNotFoundException(Long id) {
        super(String.format("Could not find disk with %d", id));
    }
}
