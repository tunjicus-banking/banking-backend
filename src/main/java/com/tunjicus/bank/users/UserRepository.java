package com.tunjicus.bank.users;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    public Iterable<User> findUsersByFirstNameContainsAndLastNameContains(String firstName, String lastName);
    public Iterable<User> findUsersByFirstNameContains(String firstName);
}
