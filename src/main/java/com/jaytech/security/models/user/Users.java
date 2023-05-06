package com.jaytech.security.models.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.mapping.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jaytech.security.models.parent.allmodels.CommonModelOperationsLogs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Users/* extends CommonModelOperationsLogs*/ implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) /* Automatically select which generation type is best for us */
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String country;
    private String state;
    private LocalDate birthDate;
    private Long phoneNumber;
    private String password;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER, targetEntity = Roles.class)
    private Collection<Roles> roles;

    @Column(name = "is_account_non_expired", columnDefinition = "boolean default true")
    private Boolean isAccountNonExpired = true;
    @Column(name = "is_account_non_locked", columnDefinition = "boolean default true")
    private Boolean isAccountNonLocked = true;
    @Column(name = "is_credentials_non_expired", columnDefinition = "boolean default true")
    private Boolean isCredentialsNonExpired = true;
    @Column(name = "is_enabled", columnDefinition = "boolean default true")
    private Boolean isEnabled = true;



    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        try {
            return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).toList(); /*As in our situation a user might have multiple roles*/
        } catch (Exception e) {
            throw new RuntimeException("Exception in  'getAuthorities' method");
        }
    }


    @Override
    public String getUsername() {
        try {/*Username for us is the username and not the email*/
            return username;
        } catch (Exception e) {
            throw new RuntimeException("Exception in  'getUsername' method");
        }

    }

    @Override
    public String getPassword() {
        try {/*Password*/
            return password;
        } catch (Exception e) {
            throw new RuntimeException("Exception in  'getPassword' method");
        }

    }


    @Override
    public boolean isAccountNonExpired() {

        try {/*Initially the account won't be expired*/
            return isAccountNonExpired;
        } catch (Exception e) {
            throw new RuntimeException("Exception in  'isAccountNonExpired' method");
        }

    }

    @Override
    public boolean isAccountNonLocked() {
        try {/*Initially the account won't be expired*/
            return isAccountNonLocked;
        } catch (Exception e) {
            throw new RuntimeException("Exception in  'isAccountNonLocked' method");
        }
    }

    @Override
    public boolean isCredentialsNonExpired() {
        try {/*Initially the account won't have expired credentials*/
            return isCredentialsNonExpired;
        } catch (Exception e) {
            throw new RuntimeException("Exception in  'isCredentialsNonExpired' method");
        }
    }

    @Override
    public boolean isEnabled() {
        try {/*Initially the account will be enabled*/
            return isEnabled;
        } catch (Exception e) {
            throw new RuntimeException("Exception in  'isEnabled' method");
        }

    }
}


/*
 * When spring security starts and set up the application, it will use an object called UserDetais
 * And this UserDetails is an interface that contains bunch of methods.
 *
 * So whenever you have the class User think always to implement UserDetails interface so that your application user is already a spring user.
 *
 *
 *
 */