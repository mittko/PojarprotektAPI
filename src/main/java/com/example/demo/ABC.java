package com.example.demo;

import jakarta.persistence.*;
//import lombok.Getter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Data
@Getter
@Setter
@Table(name = "TEST7")
public class ABC implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "name")
    public String name;

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TEST2{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
