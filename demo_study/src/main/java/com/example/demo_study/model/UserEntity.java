package com.example.demo_study.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.cglib.core.KeyFactory;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints= {@UniqueConstraint(columnNames = "email")})
public class UserEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    private String Id;

    @Column(nullable = false)
    private String username;

     @Column(nullable = false)
    private String email;

     @Column(nullable = false)
    private String password;
}
