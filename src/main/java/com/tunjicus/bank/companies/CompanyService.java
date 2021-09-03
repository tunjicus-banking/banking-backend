package com.tunjicus.bank.companies;

import com.tunjicus.bank.accounts.AccountService;
import com.tunjicus.bank.accounts.enums.PostAccountType;
import com.tunjicus.bank.accounts.models.Account;
import com.tunjicus.bank.accounts.models.CheckingAccount;
import com.tunjicus.bank.accounts.repositories.AccountRepository;
import com.tunjicus.bank.accounts.repositories.CheckingRepository;
import com.tunjicus.bank.companies.dtos.GetCompanyDto;
import com.tunjicus.bank.companies.dtos.PostCompanyDto;
import com.tunjicus.bank.companies.exceptions.CompanyExistsException;
import com.tunjicus.bank.companies.exceptions.CompanyNotFoundException;
import com.tunjicus.bank.users.User;
import com.tunjicus.bank.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    Page<GetCompanyDto> findAll(int page, int size) {
        page = Math.max(page, 0);
        size = size < 0 ? 20 : size;

        return companyRepository.findAll(PageRequest.of(page, size)).map(GetCompanyDto::new);
    }

    GetCompanyDto findById(int id) {
        var company =
                companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));

        return new GetCompanyDto(company);
    }

    @Transactional
    public GetCompanyDto create(PostCompanyDto dto) {
        if (userRepository.existsByUsername(dto.getName())) {
            throw new CompanyExistsException(dto.getName());
        }

        var user = userRepository.save(new User(dto));
        var account = new Account(user.getId(), 1, PostAccountType.CHECKING, BigDecimal.ZERO);
        accountRepository.save(account);

        return new GetCompanyDto(companyRepository.save(new Company(dto, user.getId())));
    }
}
