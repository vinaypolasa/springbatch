package com.pvk.batchdemo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table( name = "zips")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Uszip {
    @Id
    @GeneratedValue
    @Column(name = "zid", updatable = false, nullable = false)
    private int zid;
    @Column(name = "zip")
    private String zip;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column( name = "country")
    private String country;
    @Column (name = "timezone")
    private String timezone;
}
