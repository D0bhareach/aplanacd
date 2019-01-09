package com.github.zxxzru.aplanacd;

import com.github.zxxzru.aplanacd.disk.Disk;
import com.github.zxxzru.aplanacd.disk.DiskRepository;
import com.github.zxxzru.aplanacd.user.User;
import com.github.zxxzru.aplanacd.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

        import org.springframework.boot.CommandLineRunner;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;


import java.util.*;

@Configuration
@Slf4j
class LoadData {

    User defaultUser = new User("default", "address", "login", "no-password");
    User u2 = new User("user-2-name", "user-2-address", "user-2-login", "user-2-password");
    User u3 = new User("user-3-name", "user-3-address", "user-3-login", "user-3-password");
    User u4 = new User("user-4-name", "user-4-address", "user-4-login", "user-4-password");
    User u5 = new User("user-5-name", "user-5-address", "user-5-login", "user-5-password");
    User u6 = new User("user-6-name", "user-6-address", "user-6-login", "user-6-password");
    User[] users = {defaultUser, u2, u3, u4, u5, u6};

    Disk[] disks = {
            new Disk("disk-1-name", "disk-1-company", 1, u2),
            new Disk("disk-2-name", "disk-2-company", 2, u3),
            new Disk("disk-3-name", "disk-3-company", 3, u4),
            new Disk("disk-4-name", "disk-4-company", 4, u5),
            new Disk("disk-5-name", "disk-5-company", 5, u6),
            new Disk("disk-6-name", "disk-6-company", 6, u2),
            new Disk("disk-7-name", "disk-7-company", 7, u2),
            new Disk("disk-8-name", "disk-8-company", 8, u4),
            new Disk("disk-9-name", "disk-9-company", 9, u2),
            new Disk("disk-10-name", "disk-10-company", 10, u5),
            new Disk("disk-11-name", "disk-11-company", 11, u6),
            new Disk("disk-12-name", "disk-12-company", 12, u3),
            new Disk("disk-13-name", "disk-13-company", 13, u4),
            new Disk("disk-14-name", "disk-14-company", 14, u5),
            new Disk("disk-15-name", "disk-15-company", 15, u5),
            new Disk("disk-16-name", "disk-16-company", 16, u2),
            new Disk("disk-17-name", "disk-17-company", 17, u4),
            new Disk("disk-18-name", "disk-18-company", 18, u2),
            new Disk("disk-19-name", "disk-19-company", 19, u4),
            new Disk("disk-20-name", "disk-20-company", 20, u3),
    };

    private String makeName(String n, String pattern){
        int index = n != null && n.length() > 0 ? n.lastIndexOf(pattern) : -1;
        return index > 0 ? n.substring(0, index) : "undefined";
    }

    @Bean
    CommandLineRunner initUser(UserRepository userRepo) {
        return args -> {
            Arrays.stream(users)
                    .forEach(user -> {
                        log.info(String.format("Preloading %s : %s",
                                makeName(user.getName(), "-"),
                                userRepo.save(user)));
                    });

        };
    }
    @Bean
    CommandLineRunner initDisk(DiskRepository diskRepo) {
        return args -> {
            System.out.println(args.toString());
            Arrays.stream(disks)
                    .forEach(disk -> {
                        log.info(String.format("Preloading %s : %s",
                                makeName(disk.getName(), "-"),
                                diskRepo.save(disk)));
                    });

        };
    }
}
