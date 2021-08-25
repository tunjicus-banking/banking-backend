package com.tunjicus.bank.requirements;

import com.tunjicus.bank.requirements.models.Requirement;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RequirementRepository extends CrudRepository<Requirement, Integer> {}
