package com.jaytech.security.subconcepts.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Entity
@Table(schema = "concepts", name = "poc_user")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Slf4j
public class PocUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) /* Automatically select which generation type is best for us */
    private Long id;
    private String name;
}
