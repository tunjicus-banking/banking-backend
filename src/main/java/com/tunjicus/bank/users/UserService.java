package com.tunjicus.bank.users;

import com.tunjicus.bank.auth.dtos.RegisterDto;
import com.tunjicus.bank.employmentHistory.EmploymentHistoryRepository;
import com.tunjicus.bank.employmentHistory.GetEmploymentHistoryDto;
import com.tunjicus.bank.roles.RoleName;
import com.tunjicus.bank.roles.RolesRepository;
import com.tunjicus.bank.users.dtos.GetUserDto;
import com.tunjicus.bank.users.dtos.UpdateUserDto;
import com.tunjicus.bank.users.exceptions.UserNotFoundException;
import com.tunjicus.bank.users.exceptions.UserValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmploymentHistoryRepository employmentHistoryRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    GetUserDto findById(int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info(authentication.getName());
        return new GetUserDto(
                userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Transactional
    public GetUserDto save(RegisterDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UserValidationException("A user with this username exists already");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new UserValidationException("Passwords do not match");
        }

        if (dto.getPassword().length() < 8) {
            throw new UserValidationException("Password needs to be at least 8 characters long");
        }

        var password = passwordEncoder.encode(dto.getPassword());
        var userRole = rolesRepository.findByName(RoleName.USER);
        if (userRole.isEmpty()) {
            logger.error("Failed to find USER role");
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"
            );
        }
        var user = new User(dto, password);
        user.getRoles().add(userRole.get());

        return new GetUserDto(userRepository.save(user));
    }

    Page<GetUserDto> findByName(String name, int page, int size) {
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
        Page<User> output;
        if (nameSplit.length > 1) {
            var lastname =
                    Strings.join(
                            Arrays.asList(Arrays.copyOfRange(nameSplit, 1, nameSplit.length)), ' ');
            output = userRepository.findUsersByFirstNameContainsAndLastNameContains(
                    firstName, lastname, paging);
        } else {
            output = userRepository.findUsersByFirstNameContains(firstName, paging);
        }

        return output.map(GetUserDto::new);
    }

    GetUserDto update(UpdateUserDto user, int id) {
        var foundUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        foundUser.setUsername(user.getUsername());
        foundUser.setFirstName(user.getFirstName());
        foundUser.setLastName(user.getLastName());

        return new GetUserDto(userRepository.save(foundUser));
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
