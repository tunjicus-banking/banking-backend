package com.tunjicus.bank.companies;

import com.tunjicus.bank.companies.dto.GetCompanyDto;
import com.tunjicus.bank.companies.exceptions.CompanyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

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
}
