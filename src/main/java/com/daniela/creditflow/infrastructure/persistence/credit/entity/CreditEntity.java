package com.daniela.creditflow.infrastructure.persistence.credit.entity;

import com.daniela.creditflow.domain.credit.model.CreditStatus;
import com.daniela.creditflow.domain.credit.model.CreditType;
import com.daniela.creditflow.infrastructure.persistence.customer.entity.CustomerEntity;
import com.daniela.creditflow.infrastructure.persistence.installment.entity.InstallmentEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "credits")
public class CreditEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customer_id",
            referencedColumnName = "id",
            nullable = false)
    private CustomerEntity customer;

    @Column(name = "requested_amount",
            precision = 15,
            scale = 2,
            nullable = false)
    private BigDecimal requestedAmount;

    @OneToMany(
            mappedBy = "credit",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<InstallmentEntity> installments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "credit_type", nullable = false)
    private CreditType creditType;

    @Column(name = "interest_rate",
            precision = 15,
            scale = 6,
            nullable = false)
    private BigDecimal interestRate;

    @Column(name = "installments_quantity",
            nullable = false)
    private Integer installmentsQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",
            nullable = false)
    private CreditStatus status;

    @Column(nullable = false,
            updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public CreditEntity() {
    }

    public CreditEntity(UUID id,
                        CustomerEntity customer,
                        BigDecimal requestedAmount,
                        CreditType creditType,
                        BigDecimal interestRate,
                        Integer installmentsQuantity,
                        CreditStatus status,
                        Instant createdAt,
                        Instant updatedAt) {

        this.id = id;
        this.customer = customer;
        this.requestedAmount = requestedAmount;
        this.creditType = creditType;
        this.interestRate = interestRate;
        this.installmentsQuantity = installmentsQuantity;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void addInstallment(InstallmentEntity installment) {
        installments.add(installment);
    }
}
