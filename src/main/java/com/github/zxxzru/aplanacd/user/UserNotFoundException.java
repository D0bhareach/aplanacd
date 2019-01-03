package com.github.zxxzru.aplanacd.user;

class UserNotFoundException extends RuntimeException {

    UserNotFoundException(Long id) {
        super(String.format("Could not find disk %d", id));
    }
}
