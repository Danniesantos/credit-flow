package com.daniela.creditflow.domain.model;

import com.daniela.creditflow.domain.exceptions.InstallmentNotFoundException;
import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import com.daniela.creditflow.domain.valueobject.InstallmentId;
import com.daniela.creditflow.domain.valueobject.Money;
import com.daniela.creditflow.support.CreditTestFactory;
import com.daniela.creditflow.support.InstallmentTestFactory;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CreditTest {

    private static final ZoneId ZONE_ID =
            ZoneId.of("America/Sao_Paulo");

    private static final Clock FIXED_CLOCK =
            Clock.fixed(
                    Instant.parse("2026-08-24T15:00:00Z"),
                    ZONE_ID
            );

    private static final Instant NOW =
            FIXED_CLOCK.instant();

    private static final LocalDate TODAY =
            LocalDate.now(FIXED_CLOCK);

    @Test
    @DisplayName("Should approve credit under analysis")
    void shouldApproveCredit() {

        Credit credit = CreditTestFactory.underAnalysisCredit();

        credit.approve(NOW);

        assertThat(credit.getStatus())
                .isEqualTo(CreditStatus.APPROVED);
    }

    @Test
    @DisplayName("Should reject credit")
    void shouldRejectCredit() {

        Credit credit = CreditTestFactory.underAnalysisCredit();

        credit.reject(NOW);

        assertThat(credit.getStatus())
                .isEqualTo(CreditStatus.REJECTED);
    }

    @Test
    @DisplayName("Should cancel credit under analysis")
    void shouldCancelCredit() {

        Credit credit = CreditTestFactory.underAnalysisCredit();

        credit.cancel(NOW);

        assertThat(credit.getStatus())
                .isEqualTo(CreditStatus.CANCELED);
    }

    @Test
    @DisplayName("Should not cancel contracted credit")
    void shouldNotCancelContractedCredit() {

        Credit credit = CreditTestFactory.contractedCredit();

        assertThatThrownBy(() ->
                credit.cancel(NOW)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining(
                        "Contracted credits cannot be canceled");
    }

    @Test
    @DisplayName("Should contract approved credit")
    void shouldContractCredit() {

        Credit credit = CreditTestFactory.approvedCredit();

        List<Installment> installments =
                InstallmentTestFactory.installments(
                        credit.getId(),
                        credit.getInstallmentsQuantity()
                );

        credit.contract(installments, NOW);

        assertThat(credit.getStatus())
                .isEqualTo(CreditStatus.CONTRACTED);

        assertThat(credit.getInstallments())
                .hasSize(credit.getInstallmentsQuantity());
    }

    @Test
    @DisplayName("Should not contract credit before approval")
    void shouldNotContractCreditBeforeApproval() {

        Credit credit = CreditTestFactory.underAnalysisCredit();

        assertThatThrownBy(() ->
                credit.contract(
                        List.of(),
                        NOW)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining(
                        "Only approved credits can be contracted");
    }

    @Test
    @DisplayName("Should not contract an already contracted credit")
    void shouldNotContractAlreadyContractedCredit() {

        Credit credit = CreditTestFactory.contractedCredit();

        List<Installment> installments =
                InstallmentTestFactory.installments(
                        credit.getId(),
                        12
                );

        assertThatThrownBy(() ->
                credit.contract(installments, NOW)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Credit is already contracted");
    }

    @Test
    @DisplayName("Should pay installment")
    void shouldPayInstallment() {

        Credit credit = CreditTestFactory.contractedCredit();

        Installment installment =
                credit.getInstallments().getFirst();

        credit.markInstallmentAsPaid(
                installment.getId(),
                PaymentMethod.PIX,
                NOW,
                NOW
        );

        assertThat(installment.isPaid())
                .isTrue();
    }

    @Test
    @DisplayName("Should mark credit as paid off")
    void shouldMarkCreditAsPaidOff() {

        Credit credit = CreditTestFactory.contractedCredit();

        credit.getInstallments().forEach(installment ->
                credit.markInstallmentAsPaid(
                        installment.getId(),
                        PaymentMethod.PIX,
                        NOW,
                        NOW
                )
        );

        assertThat(credit.getStatus())
                .isEqualTo(CreditStatus.PAID_OFF);
    }

    @Test
    @DisplayName("Should renegotiate overdue credit")
    void shouldRenegotiateCredit() {

        Credit credit =
                CreditTestFactory.creditWithOverdueInstallments();

        List<Installment> installments =
                InstallmentTestFactory.installments(
                        credit.getId(),
                        credit.nextInstallmentNumber(),
                        6
                );

        credit.renegotiate(
                installments,
                TODAY,
                NOW
        );

        assertThat(credit.getInstallments())
                .hasSize(
                        credit.paidInstallments().size() + 6
                );
    }

    @Test
    @DisplayName("Should not renegotiate without overdue installments")
    void shouldNotRenegotiateWithoutOverdueInstallments() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        assertThatThrownBy(() ->
                credit.renegotiate(
                        List.of(),
                        TODAY,
                        NOW
                )
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Credit cannot be renegotiated");
    }

    @Test
    @DisplayName("Should restructure contracted credit")
    void shouldRestructureCredit() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        List<Installment> installments =
                InstallmentTestFactory.installments(
                        credit.getId(),
                        credit.nextInstallmentNumber(),
                        24
                );

        credit.restructure(
                installments,
                NOW
        );

        assertThat(credit.getInstallments())
                .hasSize(
                        credit.paidInstallments().size() + 24
                );
    }

    @Test
    @DisplayName("Should not restructure paid off credit")
    void shouldNotRestructurePaidOffCredit() {

        Credit credit =
                CreditTestFactory.paidOffCredit();

        assertThatThrownBy(() ->
                credit.restructure(
                        List.of(),
                        NOW
                )
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Credit cannot be restructured");
    }

    @Test
    @DisplayName("Should not pay installment before contract")
    void shouldNotPayInstallmentBeforeContract() {

        Credit credit = CreditTestFactory.approvedCredit();

        Installment installment =
                InstallmentTestFactory.installments(
                        credit.getId(),
                        1
                ).getFirst();

        assertThatThrownBy(() ->
                credit.markInstallmentAsPaid(
                        installment.getId(),
                        PaymentMethod.PIX,
                        NOW,
                        NOW
                )
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Credit must be contracted before payments");
    }

    @Test
    @DisplayName("Should not pay installment when credit is paid off")
    void shouldNotPayPaidOffCredit() {

        Credit credit = CreditTestFactory.paidOffCredit();

        Installment installment =
                credit.getInstallments().getFirst();

        assertThatThrownBy(() ->
                credit.markInstallmentAsPaid(
                        installment.getId(),
                        PaymentMethod.PIX,
                        NOW,
                        NOW
                )
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Credit is already paid off");
    }

    @Test
    @DisplayName("Should calculate total paid amount")
    void shouldCalculateTotalPaidAmount() {

        Credit credit =
                CreditTestFactory.creditWithOnePaidInstallment();

        assertThat(credit.totalPaidAmount())
                .isEqualTo(TestConstants.INSTALLMENT_AMOUNT);
    }

    @Test
    @DisplayName("Should calculate remaining amount")
    void shouldCalculateRemainingAmount() {

        Credit credit =
                CreditTestFactory.creditWithOnePaidInstallment();

        assertThat(credit.remainingAmount())
                .isEqualTo(new Money(BigDecimal.valueOf(11000)));
    }

    @Test
    @DisplayName("Should return paid installments quantity")
    void shouldReturnPaidInstallmentsQuantity() {

        Credit credit =
                CreditTestFactory.creditWithOnePaidInstallment();

        assertThat(credit.paidInstallmentsQuantity())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("Should return remaining installments quantity")
    void shouldReturnRemainingInstallmentsQuantity() {

        Credit credit =
                CreditTestFactory.creditWithOnePaidInstallment();

        assertThat(credit.remainingInstallments())
                .isEqualTo(11);
    }

    @Test
    @DisplayName("Should return overdue installments quantity")
    void shouldReturnOverdueInstallmentsQuantity() {

        Credit credit =
                CreditTestFactory.creditWithOverdueInstallments();

        assertThat(credit.overdueInstallmentsQuantity(TODAY))
                .isEqualTo(12L);
    }

    @Test
    @DisplayName("Should calculate overdue amount")
    void shouldCalculateOverdueAmount() {

        Credit credit =
                CreditTestFactory.creditWithOverdueInstallments();

        assertThat(credit.overdueAmount(TODAY))
                .isEqualTo(new Money(BigDecimal.valueOf(12_000)));
    }

    @Test
    @DisplayName("Should return next installment number")
    void shouldReturnNextInstallmentNumber() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        assertThat(credit.nextInstallmentNumber())
                .isEqualTo(13);
    }

    @Test
    @DisplayName("Should find installment by id")
    void shouldFindInstallment() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        Installment installment =
                credit.getInstallments().getFirst();

        Installment found =
                credit.findInstallment(installment.getId());

        assertThat(found)
                .isEqualTo(installment);
    }

    @Test
    @DisplayName("Should throw when installment is not found")
    void shouldThrowWhenInstallmentIsNotFound() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        assertThatThrownBy(() ->
                credit.findInstallment(
                        new InstallmentId(UUID.randomUUID())
                )
        )
                .isInstanceOf(InstallmentNotFoundException.class);
    }

    @Test
    @DisplayName("Should return true when credit has overdue installments")
    void shouldHaveOverdueInstallments() {

        Credit credit =
                CreditTestFactory.creditWithOverdueInstallments();

        assertThat(credit.hasOverdueInstallments(TODAY))
                .isTrue();
    }

    @Test
    @DisplayName("Should return true when credit has pending installments")
    void shouldHavePendingInstallments() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        assertThat(credit.hasPendingInstallments())
                .isTrue();
    }

    @Test
    @DisplayName("Should return true when credit is under analysis")
    void shouldReturnTrueWhenCreditIsUnderAnalysis() {

        Credit credit = CreditTestFactory.underAnalysisCredit();

        assertThat(credit.isUnderAnalysis()).isTrue();
    }

    @Test
    @DisplayName("Should return true when credit is approved")
    void shouldReturnTrueWhenCreditIsApproved() {

        Credit credit = CreditTestFactory.approvedCredit();

        assertThat(credit.isApproved()).isTrue();
    }

    @Test
    @DisplayName("Should return true when credit is rejected")
    void shouldReturnTrueWhenCreditIsRejected() {

        Credit credit = CreditTestFactory.rejectedCredit();

        assertThat(credit.isRejected()).isTrue();
    }

    @Test
    @DisplayName("Should return true when credit is canceled")
    void shouldReturnTrueWhenCreditIsCanceled() {

        Credit credit = CreditTestFactory.canceledCredit();

        assertThat(credit.isCanceled()).isTrue();
    }

    @Test
    @DisplayName("Should return true when credit is contracted")
    void shouldReturnTrueWhenCreditIsContracted() {

        Credit credit = CreditTestFactory.contractedCredit();

        assertThat(credit.isContracted()).isTrue();
    }

    @Test
    @DisplayName("Should return true when credit is paid off")
    void shouldReturnTrueWhenCreditIsPaidOff() {

        Credit credit = CreditTestFactory.paidOffCredit();

        assertThat(credit.isPaidOff()).isTrue();
    }

    @Test
    @DisplayName("Should allow renegotiation")
    void shouldAllowRenegotiation() {

        Credit credit =
                CreditTestFactory.creditWithOverdueInstallments();

        assertThat(credit.canRenegotiate(TODAY))
                .isTrue();
    }

    @Test
    @DisplayName("Should allow restructure")
    void shouldAllowRestructure() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        assertThat(credit.canRestructure())
                .isTrue();
    }

    @Test
    @DisplayName("Should return paid installments")
    void shouldReturnPaidInstallments() {

        Credit credit =
                CreditTestFactory.creditWithOnePaidInstallment();

        assertThat(credit.paidInstallments())
                .hasSize(1);
    }

    @Test
    @DisplayName("Should return pending installments")
    void shouldReturnPendingInstallments() {

        Credit credit =
                CreditTestFactory.creditWithOnePaidInstallment();

        assertThat(credit.pendingInstallments())
                .hasSize(11);
    }

    @Test
    @DisplayName("Should restore credit with installments")
    void shouldRestoreCredit() {

        Credit original = CreditTestFactory.contractedCredit();

        Credit restored = CreditTestFactory.restoredCredit(original);

        assertThat(restored.getInstallments())
                .hasSize(original.getInstallmentsQuantity());

        assertThat(restored.getStatus())
                .isEqualTo(original.getStatus());
    }
}