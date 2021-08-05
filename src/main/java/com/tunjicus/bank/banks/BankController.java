package com.tunjicus.bank.banks;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankController {
    private final BankRepository bankRepository;

    // Using List not Iterable for swagger docs
    @GetMapping
    public List<Bank> getAll() {
        return (List<Bank>) bankRepository.findAll();
    }

    @GetMapping("/{id}")
    public Bank get(@PathVariable int id) {
        var bank = bankRepository.findById(id);
        if (bank.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Not Found"
            );
        }
        return bank.get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bank created(@Valid @RequestBody Bank bank) {
        return bankRepository.save(bank);
    }
}
