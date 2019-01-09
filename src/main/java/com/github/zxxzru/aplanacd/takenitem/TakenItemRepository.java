package com.github.zxxzru.aplanacd.takenitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TakenItemRepository extends JpaRepository<TakenItem, Long> {
    @Query("SELECT t FROM TakenItem t WHERE t.diskId = :id")
    List<TakenItem> getTakenItemList(@Param("id") Long id);

}
