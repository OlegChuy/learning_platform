package com.oleh.chui.learning_platform.entity;

import com.oleh.chui.learning_platform.dto.PersonDTO;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "persons")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Person implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(unique = true)
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String email;
    @NonNull
    private LocalDate birthday;

    @NonNull
    @Enumerated(EnumType.STRING)
    @ManyToOne()
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_details_id", referencedColumnName = "id")
    private PersonDetails personDetails;
    @OneToMany(mappedBy = "creator")
    private Set<Course> courseSet;
    @ManyToMany
    @JoinTable(
            name = "person_learn_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> selectedCourses;

    public Person(PersonDTO personDTO) {
        this.username = personDTO.getUsername();
        this.password = personDTO.getPassword();
        this.email = personDTO.getEmail();
        this.birthday = personDTO.getBirthday();
        this.role = Role.builder().role(Role.RoleEnum.USER).build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.getRole().toString()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
