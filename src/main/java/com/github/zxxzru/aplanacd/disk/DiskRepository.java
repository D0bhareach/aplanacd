package com.github.zxxzru.aplanacd.disk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
// list of taken disks

public interface DiskRepository extends JpaRepository<Disk, Long> {

    @Query("select d from Disk d  where d.id not in (select t.diskId from TakenItem t)")
    List<Disk> getAvailableDisks();

    @Query("select d from Disk d right join TakenItem t on d.id = t.diskId where d.user.id = :id")
    List<Disk> getRentedFromUser(@Param("id") Long id);

    @Query("select d from Disk d right join TakenItem t on d.id = t.diskId")
    List<Disk> getAllRented();

    @Query("select d from Disk d right join TakenItem t on d.id = t.diskId where t.userId = :id")
    List<Disk> getRentedByUser(@Param("id") Long id);
}
