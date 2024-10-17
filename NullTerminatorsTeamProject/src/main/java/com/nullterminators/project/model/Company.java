package com.nullterminators.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import java.io.Serializable;
import lombok.Data;

/**
 * Company model to map company table in DB.
 */
@Data
@Entity
public class Company implements Serializable {

  @Id
  @SequenceGenerator(name = "companyIdSeq", sequenceName = "company_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "companyIdSeq")
  private Integer id;

  private String name;

  @Column(unique = true)
  private String username;

  private String address;

  private String state;

  private String password;
}
