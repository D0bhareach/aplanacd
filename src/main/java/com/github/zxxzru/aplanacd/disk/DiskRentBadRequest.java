package com.github.zxxzru.aplanacd.disk;

public class DiskRentBadRequest extends RuntimeException {
    DiskRentBadRequest (Long userId, Long diskId) {
        super(String.format("Could not rent disk with userId: %d and diskId: %d", userId, diskId));
    }
}
