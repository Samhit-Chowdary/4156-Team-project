package com.nullterminators.project.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public class Company implements Serializable {

    @Id
    @SequenceGenerator(name = "companyIdSeq",
                      sequenceName = "company_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "companyIdSeq")
    private Integer id;

    private String name;

    @Column(unique = true)
    private String username;

    private String address;

    private String state;

    private String password;
}
