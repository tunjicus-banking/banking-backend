package com.tunjicus.bank.users;

import com.tunjicus.bank.employmentHistory.EmploymentHistoryRepository;
import com.tunjicus.bank.employmentHistory.GetEmploymentHistoryDto;
import com.tunjicus.bank.users.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmploymentHistoryRepository employmentHistoryRepository;

    User findById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    User save(User user) {
        return userRepository.save(user);
    }

    Page<User> findByName(String name, int page, int size) {
        page = Math.max(page, 0);
        size = size < 0 ? 20 : size;
        Pageable paging =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("firstName").ascending().and(Sort.by("lastName").ascending()));

        if (name.isEmpty()) {
            return Page.empty();
        }

        var nameSplit = name.split(" ");
        var firstName = nameSplit[0];
        if (nameSplit.length > 1) {
            var lastname =
                    Strings.join(
                            Arrays.asList(Arrays.copyOfRange(nameSplit, 1, nameSplit.length)), ' ');
            return userRepository.findUsersByFirstNameContainsAndLastNameContains(
                    firstName, lastname, paging);
        }

        return userRepository.findUsersByFirstNameContains(firstName, paging);
    }

    User update(User user, int id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        user.setId(id);
        return userRepository.save(user);
    }

    Page<GetEmploymentHistoryDto> getEmploymentHistory(int userId, int page, int size) {
        page = Math.max(page, 0);
        size = size < 0 ? 20 : size;
        var pageable = PageRequest.of(page, size, Sort.by("hireDate").descending());

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        //  TODO: Make sure userId matches up with current user

        return employmentHistoryRepository
                .findAllByUserId(userId, pageable)
                .map(GetEmploymentHistoryDto::new);
    }
}
