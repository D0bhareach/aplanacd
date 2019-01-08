package com.github.zxxzru.aplanacd.user;

import java.util.Set;
// import com.github.zxxzru.aplanacd.disk.Disk;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
// import javax.persistence.OneToMany;

@Data
@Entity
@Table(name="user")
public class User {

@Id
@Column(name="id")
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;

@Column(name="name", length=100)
private String name;

@Column(name="address", length=250)
private String address;

@Column(name="login", length=25)
private String login;


@Column(name="password", length=25)
private String password;

// @OneToMany(mappedBy="user")
// private Set<Disk> disks;

    public User(String name, String address, String login, String password) {
        this.name = name;
        this.address = address;
        this.login = login;
        this.password = password;
    }

    User(){}

// setters 
// public void setName(String name) {
// this.name = name;
// }
//
// public void setAddress(String a) {
// this.address = a;
// }
// public void setLogin(String l) {
// this.login = l;
// }
// public void setPassword(String p) {
// this.password = p;
// }
// public void setId(Long id) {
// this.id = id;
// }
//
// // getters
// public Long getId(){
// return this.id;
// }
// public String getName(){
// return this.name;
// }
// public String getLogin(){
// return this.login;
// }
// public String getPassword(){
// return this.password;
// }
// public String getAddress(){
// return this.address;
// }
}
