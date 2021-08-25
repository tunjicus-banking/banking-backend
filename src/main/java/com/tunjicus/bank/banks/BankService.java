package com.tunjicus.bank.banks;

import com.tunjicus.bank.banks.exceptions.BankNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankService {
    private final BankRepository bankRepository;

    List<Bank> findAll() {
        return (List<Bank>) bankRepository.findAll();
    }

    Bank findById(int id) {
        return bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id));
    }

    Bank save(Bank bank) {
        return bankRepository.save(bank);
    }

    Bank update(Bank bank, int id) {
        if (!bankRepository.existsById(id)) {
            throw new BankNotFoundException(id);
        }

        bank.setId(id);
        return bankRepository.save(bank);
    }
}
