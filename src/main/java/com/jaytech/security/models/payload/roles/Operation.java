package com.jaytech.security.models.payload.roles;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operation {
    private String operation;
    private String description;
}
