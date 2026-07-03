package com.daniela.creditflow.infrastructure.persistence.customer.entity;

import com.daniela.creditflow.domain.customer.model.CustomerStatus;
import com.daniela.creditflow.infrastructure.persistence.credity.entity.CreditEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id
    @Column(updatable = false,
            nullable = false)
    private UUID id;

    @Column(length = 150,
            nullable = false)
    private String name;

    @Column(length = 11,
            nullable = false,
            unique = true)
    private String cpf;

    @Column(length = 150,
            nullable = false,
            unique = true)
    private String email;

    @Column(name = "date_of_birth",
            nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "phone_number",
            length = 11,
            nullable = false)
    private String phoneNumber;

    @Column(name = "monthly_income",
            precision = 15,
            scale = 2,
            nullable = false)
    private BigDecimal monthlyIncome;

    @Column(name = "credit_score",
            nullable = false)
    private Integer creditScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;

    @OneToMany(mappedBy = "customer",
            fetch = FetchType.LAZY)
    private List<CreditEntity> credits =
            new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at",
            nullable = false,
            updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",
            nullable = false)
    private Instant updatedAt;

    public CustomerEntity() {
    }

    public CustomerEntity(
            UUID id,
            String name,
            String cpf,
            String email,
            LocalDate dateOfBirth,
            String phoneNumber,
            BigDecimal monthlyIncome,
            Integer creditScore,
            CustomerStatus status,
            Instant createdAt,
            Instant updatedAt) {

        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.monthlyIncome = monthlyIncome;
        this.creditScore = creditScore;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CustomerEntity(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CustomerEntity customer)) return false;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
