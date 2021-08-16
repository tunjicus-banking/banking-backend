package com.tunjicus.bank.users;

import com.tunjicus.bank.users.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(int id) {
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return user.get();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findByName(String name) {
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
            return (List<User>) userRepository.findUsersByFirstNameContainsAndLastNameContains(firstName, lastname);
        }

        return (List<User>) userRepository.findUsersByFirstNameContains(firstName);
    }
}
