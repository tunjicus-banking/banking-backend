package com.tunjicus.bank.transactions;

import com.tunjicus.bank.transactions.dtos.GetTransactionDto;
import com.tunjicus.bank.transactions.dtos.PostTransactionDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Defines a transaction between two users")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GetTransactionDto create(@Valid @RequestBody PostTransactionDto transactionDto) {
        return transactionService.usersTransfer(transactionDto);
    }
}
