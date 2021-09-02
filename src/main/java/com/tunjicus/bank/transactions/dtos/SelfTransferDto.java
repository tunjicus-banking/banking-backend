package com.tunjicus.bank.transactions.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tunjicus.bank.scheduled.TimeService;
import com.tunjicus.bank.transactions.Transaction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class SelfTransferDto {
    @NotNull(message = "from cannot be null")
    @Positive(message = "from must be positive")
    private int from;

    @NotNull(message = "to cannot be null")
    @Positive(message = "to must be positive")
    private int to;

    @NotNull(message = "amount cannot be null")
    @DecimalMin(value = "0", inclusive = false, message = "amount must be greater than 0")
    private BigDecimal amount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date transactionTime;

    public SelfTransferDto(Transaction transaction) {
        to = transaction.getToAccount();
        from = transaction.getFromAccount();
        amount = transaction.getAmount();
        transactionTime = TimeService.calculateSimulatedDate(transaction.getTransactionTime());
    }
}
