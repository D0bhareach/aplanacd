package com.github.zxxzru.aplanacd.disk;

import lombok.Data;
import com.github.zxxzru.aplanacd.user.User;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Data
@Entity
@Table(name="disk")
public class Disk {

@Id
@Column(name="id")
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;

@Column(name="name", length=100)
private String name;

@Column(name="company", length=25)
private String company;


@Column(name="year")
private Integer year;

@ManyToOne
@JoinColumn(name="user_id", nullable=false)
private User user;

    public Disk(String name, String company, Integer year, User user) {
        this.name = name;
        this.company = company;
        this.year = year;
        this.user = user;
    }

    Disk(){};



// setters 
// public void setName(String name) {
// this.name = name;
// }
//
// public void setCompany(String c) {
// this.company = c;
// }
public void setYear(Integer y) {
 this.year = y != null ? y : 0;
}
//  public void setId(Long id) {
//  this.id = id != null ? id : 0;
//  }
// //
// // getters
// public Long getId(){
// return this.id;
// }
// public String getName(){
// return this.name;
// }
// public String getCompany(){
// return this.company;
// }
// public Integer getYear(){
// return this.year;
// }
}
