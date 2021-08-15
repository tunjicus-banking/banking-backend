package com.tunjicus.bank.transactions;

import com.tunjicus.bank.transactions.dtos.PostTransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public Transaction create(@Valid @RequestBody PostTransactionDto transactionDto) {
        return transactionService.usersTransfer(transactionDto);
    }
}
