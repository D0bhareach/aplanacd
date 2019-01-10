package com.github.zxxzru.aplanacd.disk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
// @Query("select d from Disk d right join TakenItem t on d.id = t.diskId")
// list of taken disks

public interface DiskRepository extends JpaRepository<Disk, Long> {
    @Query("select d from Disk d  where d.id not in (select t.diskId from TakenItem t)")
    List<Disk> getAvailableDisks();
}
