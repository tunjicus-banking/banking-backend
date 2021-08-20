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
    private final CompanyRepository companyRepository;
    private final JobPostingRepository jobPostingRepository;

    Position findById(int id) {
        var position = positionRepository.findById(id);
        if (position.isEmpty()) {
            throw new PositionNotFoundException(id);
        }
        return position.get();
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
        var oPosition = positionRepository.findById(id);
        if (oPosition.isEmpty()) {
            throw new PositionNotFoundException(id);
        }

        var position = oPosition.get();
        position.setActive(false);
        positionRepository.save(position);
        jobPostingRepository.deleteAllByPositionId(id);
    }
}
