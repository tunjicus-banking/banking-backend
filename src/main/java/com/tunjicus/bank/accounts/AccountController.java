package com.tunjicus.bank.accounts;

import com.tunjicus.bank.accounts.dtos.GetAccountDto;
import com.tunjicus.bank.accounts.dtos.PostAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public ArrayList<GetAccountDto> getAll() {
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    public GetAccountDto get(@PathVariable int id) {
        var account = accountService.findById(id);
        if (account.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Not Found"
            );
        }
        return account.get();
    }

    @PostMapping
    public GetAccountDto create(@Valid @RequestBody PostAccountDto accountDto) {
        if (accountDto.getType() == AccountType.UNKNOWN) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot create unknown account"
            );
        }

        try {
            return accountService.save(accountDto);
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid user"
            );
        }
    }
}
