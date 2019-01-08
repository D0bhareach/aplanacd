package com.github.zxxzru.aplanacd.takenitem;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@Entity
@Table(name="taken_item")
public class TakenItem {

@Id
@Column(name="id")
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;

@Column(name="disk_id")
private Long diskId;

@Column(name="user_id")
private Long userId;

    TakenItem(){};

   TakenItem(Long diskId, Long userId) {
        this.diskId = diskId;
        this.userId = userId;
    }

    // setters
// public void setId(Long id) {
// this.id = id;
// }
// public void setUserId(Long id) {
// this.userId = id;
// }
// public void setDiskId(Long id) {
// this.diskId = id;
// }
//
// // getters
// public Long getId(){
// return this.id;
// }
// public Long getUserId(){
// return this.userId;
// }
// public Long getDiskId(){
// return this.diskId;
// }
}
