package com.jaytech.security.models.user;

import com.jaytech.security.models.parent.allmodels.CommonModelOperationsLogs;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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


    
}
