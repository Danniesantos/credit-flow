package com.daniela.creditflow.infrastructure.persistence.installment.entity;

import com.daniela.creditflow.domain.model.InstallmentStatus;
import com.daniela.creditflow.domain.model.PaymentMethod;
import com.daniela.creditflow.infrastructure.persistence.credit.entity.CreditEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@Table(name = "installments")
public class InstallmentEntity {

    @Id
    @Column(nullable = false,
            updatable = false)
    private UUID id;
    @Column(name = "number", nullable = false)
    private Integer number;
    @Column(name = "amount",
            precision = 15,
            scale = 2,
            nullable = false)
    private BigDecimal amount;
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InstallmentStatus status;

    @Column(name = "paid_at")
    private Instant paidAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id",
            nullable = false)
    private CreditEntity credit;

    private InstallmentEntity() {
    }

    public InstallmentEntity(UUID id,
                             Integer number,
                             BigDecimal amount,
                             LocalDate dueDate,
                             PaymentMethod paymentMethod,
                             InstallmentStatus status,
                             Instant paidAt,
                             CreditEntity credit) {

        this.id = id;
        this.number = number;
        this.amount = amount;
        this.dueDate = dueDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.paidAt = paidAt;
        this.credit = credit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstallmentEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
