package com.system.crypto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity(name = "USERS")
public class UserEntity {
    @Id
    @UuidGenerator
    private String id;
    @Column(unique = true)
    private String username;
    @Column
    private String password;
}
