package com.jaytech.security.models.payload.roles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private String role;
    // only existing features to be considered
    private String feature;
    // only existing operations to be considered
    private List<String> operations;
    private String description;

    public Role(String role, String description) {
        this.role=role;
        this.description=description;
    }
}
