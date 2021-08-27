package com.tunjicus.bank.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    Page<User> findUsersByFirstNameContainsAndLastNameContains(String firstName, String lastName, Pageable p);
    Page<User> findUsersByFirstNameContains(String firstName, Pageable p);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
