package Milestone.spring_project.backend.domain.Entity.Auth;

import jakarta.persistence.*;

@Entity
@Table(name = "ADMIN_PHONE_NUMBERS")
public class AdminPhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADMIN_ID")
    private Long id;

    @Column(name = "ADMIN_PHONE_NUM",unique = true, length = 11)
    private String phoneNumber;
}

