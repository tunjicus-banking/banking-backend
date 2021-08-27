package com.tunjicus.bank.roles;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RolesRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
}
