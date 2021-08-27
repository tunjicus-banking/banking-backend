package com.tunjicus.bank.companies;

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

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

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

        userRepository.save(new User(dto));
        return new GetCompanyDto(companyRepository.save(new Company(dto)));
    }
}
