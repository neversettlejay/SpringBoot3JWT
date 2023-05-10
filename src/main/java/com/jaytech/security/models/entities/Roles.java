package com.jaytech.security.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jaytech.security.models.entities.Users;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Roles/* extends CommonModelOperationsLogs*/ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) /* Automatically select which generation type is best for us */
    private Long id;
    private String role;
    private String description;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    @ToString.Exclude
    private Collection<Users> users;


    public void setUsersForRoles(Users users) {
        this.users.add(users);
    }
}
