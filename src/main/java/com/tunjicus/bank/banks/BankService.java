package com.tunjicus.bank.banks;

import com.tunjicus.bank.banks.exceptions.BankNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankService {
    private final BankRepository bankRepository;

    List<Bank> findAll() {
        return (List<Bank>) bankRepository.findAll();
    }

    Bank findById(int id) {
        var bank = bankRepository.findById(id);
        if (bank.isEmpty()) {
            throw new BankNotFoundException(id);
        }
        return bank.get();
    }

    Bank save(Bank bank) {
        return bankRepository.save(bank);
    }
}
