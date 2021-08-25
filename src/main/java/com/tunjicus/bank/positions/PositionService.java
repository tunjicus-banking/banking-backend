package com.tunjicus.bank.positions;

import com.tunjicus.bank.companies.CompanyRepository;
import com.tunjicus.bank.companies.exceptions.CompanyNotFoundException;
import com.tunjicus.bank.jobPostings.JobPostingRepository;
import com.tunjicus.bank.positions.exceptions.PositionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PositionService {
    private final PositionRepository positionRepository;
    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;

    Position findById(int id) {
        return positionRepository.findById(id).orElseThrow(() -> new PositionNotFoundException(id));
    }

    private Page<Position> findByCompanyId(int companyId, Pageable pageable) {
        if (!companyRepository.existsById(companyId)) {
            throw new CompanyNotFoundException(companyId);
        }

        return positionRepository.findAllByCompanyIdAndActiveIsTrue(companyId, pageable);
    }

    Page<Position> findAll(int companyId, int page, int size) {
        page = Math.max(page, 0);
        size = size < 0 ? 20 : size;
        var pageable = PageRequest.of(page, size);

        if (companyId >= 0) return findByCompanyId(companyId, pageable);
        return positionRepository.findAllByActiveIsTrue(pageable);
    }

    Position create(Position position) {
        return positionRepository.save(position);
    }

    Position update(Position position, int id) {
        if (!positionRepository.existsById(id)) {
            throw new PositionNotFoundException(id);
        }

        position.setId(id);
        return positionRepository.save(position);
    }

    void delete(int id) {
        var position =
                positionRepository
                        .findById(id)
                        .orElseThrow(() -> new PositionNotFoundException(id));

        position.setActive(false);
        positionRepository.save(position);
        jobPostingRepository.deleteAllByPositionId(id);
    }
}
