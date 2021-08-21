package com.tunjicus.bank.jobPostings;

import com.tunjicus.bank.companies.CompanyRepository;
import com.tunjicus.bank.companies.exceptions.CompanyNotFoundException;
import com.tunjicus.bank.jobPostings.dtos.GetJobPostingDto;
import com.tunjicus.bank.jobPostings.dtos.PostJobPostingDto;
import com.tunjicus.bank.jobPostings.exceptions.JobPostingNotFoundException;
import com.tunjicus.bank.positions.PositionRepository;
import com.tunjicus.bank.positions.exceptions.PositionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;
    private final PositionRepository positionRepository;

    private Page<GetJobPostingDto> findByCompanyId(int companyId, boolean includeAll, Pageable pageable) {
        if (!companyRepository.existsById(companyId)) {
            throw new CompanyNotFoundException(companyId);
        }

        Page<JobPosting> result;
        if (includeAll) {
            result = jobPostingRepository.findAllByCompanyIdIncludeAll(companyId, pageable);
        } else {
            result = jobPostingRepository.findAllByCompanyId(companyId, pageable);
        }

        return result.map(GetJobPostingDto::new);
    }

    private Page<GetJobPostingDto> findByPositionId(int positionId, boolean includeAll, Pageable pageable) {
        if (!positionRepository.existsById(positionId)) {
            throw new PositionNotFoundException(positionId);
        }

        Page<JobPosting> result;
        if (includeAll) {
            result = jobPostingRepository.findAllByPositionId(positionId, pageable);
        } else {
            result = jobPostingRepository.findAllByPositionIdAndActiveIsTrue(positionId, pageable);
        }

        return result.map(GetJobPostingDto::new);
    }

    Page<GetJobPostingDto> findAll(int companyId, int positionId, boolean includeAll, int page, int size) {
        page = Math.max(page, 0);
        size = size < 0 ? 20 : size;
        var pageable = PageRequest.of(page, size);

        if (companyId >= 0) return findByCompanyId(companyId, includeAll, pageable);
        else if (positionId >= 0) return findByPositionId(positionId, includeAll, pageable);

        Page<JobPosting> result;
        if (includeAll) {
            result = jobPostingRepository.findAll(pageable);
        } else {
            result = jobPostingRepository.findAllByActiveIsTrue(pageable);
        }

        return result.map(GetJobPostingDto::new);
    }

    GetJobPostingDto create(PostJobPostingDto dto) {
        if (!positionRepository.existsById(dto.getPositionId())) {
            throw new PositionNotFoundException(dto.getPositionId());
        }

        if (dto.getSalaryHigh().compareTo(dto.getSalaryLow()) < 0) {
            dto.setSalaryHigh(dto.getSalaryLow());
        }

        var jobPosting = jobPostingRepository.save(new JobPosting(dto));
        jobPosting.setUpSince(new Date());
        return new GetJobPostingDto(jobPosting);
    }

    GetJobPostingDto update(PostJobPostingDto dto, int id) {
        if (!positionRepository.existsById(dto.getPositionId())) {
            throw new PositionNotFoundException(dto.getPositionId());
        }

        var oldPosting = jobPostingRepository.findById(id);
        if (oldPosting.isEmpty()) {
            throw new JobPostingNotFoundException(id);
        }

        if (dto.getSalaryHigh().compareTo(dto.getSalaryLow()) < 0) {
            dto.setSalaryHigh(dto.getSalaryLow());
        }

        var jobPosting = new JobPosting(dto);
        jobPosting.setId(id);

        // Make sure upSince time gets updated when activating an existing posting
        if (!oldPosting.get().isActive() && jobPosting.isActive()) {
            jobPosting.setUpSince(new Date());
        }

        jobPosting = jobPostingRepository.save(jobPosting);
        return new GetJobPostingDto(jobPosting);
    }

    GetJobPostingDto findById(int id) {
        var jobPosting = jobPostingRepository.findById(id);
        if (jobPosting.isEmpty()) {
            throw new JobPostingNotFoundException(id);
        }

        return new GetJobPostingDto(jobPosting.get());
    }

    void delete(int id) {
        if (!jobPostingRepository.existsById(id)) {
            throw new JobPostingNotFoundException(id);
        }

        jobPostingRepository.deleteById(id);
    }
}
