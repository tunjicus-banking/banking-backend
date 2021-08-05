package com.tunjicus.bank.users;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public ArrayList<User> findByName(String name) {
        if (name.isEmpty()) {
            return new ArrayList<>();
        }
        var nameSplit = name.split(" ");
        var firstName = nameSplit[0];
        if (nameSplit.length > 1) {
            var lastname = Strings.join(
                    Arrays.asList(
                            Arrays.copyOfRange(nameSplit, 1, nameSplit.length)
                    ),
                    ' ');
            return (ArrayList<User>) userRepository.findUsersByFirstNameContainsAndLastNameContains(firstName, lastname);
        }

        return (ArrayList<User>) userRepository.findUsersByFirstNameContains(firstName);
    }
}
