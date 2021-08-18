package com.tunjicus.bank.users;

import com.tunjicus.bank.users.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<User> findByName(String name, int page, int size) {
        page = Math.max(page, 0);
        size = size < 0 ? 20 : size;
        Pageable paging = PageRequest.of(page, size);

        if (name.isEmpty()) {
            return Page.empty();
        }

        var nameSplit = name.split(" ");
        var firstName = nameSplit[0];
        if (nameSplit.length > 1) {
            var lastname = Strings.join(
                    Arrays.asList(
                            Arrays.copyOfRange(nameSplit, 1, nameSplit.length)
                    ),
                    ' ');
            return userRepository.findUsersByFirstNameContainsAndLastNameContains(firstName, lastname, paging);
        }

        return userRepository.findUsersByFirstNameContains(firstName, paging);
    }
}
