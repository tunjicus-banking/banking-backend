package com.tunjicus.bank.accounts;

import com.tunjicus.bank.accounts.dtos.GetAccountDto;
import com.tunjicus.bank.accounts.dtos.PostAccountDto;
import com.tunjicus.bank.accounts.enums.PostAccountType;
import com.tunjicus.bank.accounts.exceptions.AccountNotFoundException;
import com.tunjicus.bank.accounts.models.Account;
import com.tunjicus.bank.accounts.repositories.AccountRepository;
import com.tunjicus.bank.accounts.repositories.CheckingRepository;
import com.tunjicus.bank.accounts.repositories.SavingsRepository;
import com.tunjicus.bank.auth.AuthService;
import com.tunjicus.bank.users.User;
import com.tunjicus.bank.users.exceptions.UnauthorizedUserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    private AccountService accountService;
    @Mock private AccountRepository accountRepository;
    @Mock private CheckingRepository checkingRepository;
    @Mock private SavingsRepository savingsRepository;
    @Mock private AuthService authService;

    @BeforeEach
    void setup() {
        accountService =
                new AccountService(
                        accountRepository, checkingRepository, savingsRepository, authService);
    }

    @Test
    @DisplayName("Test findById success")
    void testFindById() {
        var account = new Account(1, 1, 1, "C", BigDecimal.TEN, false, new Date());
        doReturn(Optional.of(account)).when(accountRepository).findByIdAndClosedIsFalse(1);
        var returnedAccount = accountService.findById(1);

        Assertions.assertNotNull(returnedAccount, "Returned account is null");
        Assertions.assertEquals(
                new GetAccountDto(account),
                returnedAccount,
                "Returned account does not equal original");
    }

    @Test
    @DisplayName("Test findById failure")
    void testFindByIdFailure() {
        doReturn(Optional.empty()).when(accountRepository).findByIdAndClosedIsFalse(2);
        Assertions.assertThrows(
                AccountNotFoundException.class,
                () -> accountService.findById(2),
                "Failed to throw AccountNotFoundException for account 10");
    }

    @Test
    @DisplayName("Test saving a checking account")
    void testSaveChecking() {
        var user = new User();
        user.setId(1);
        doReturn(user).when(authService).getCurrentUser();

        var accountDto = new PostAccountDto();
        accountDto.setBankId(1);
        accountDto.setType(PostAccountType.CHECKING);
        var account = new Account(accountDto, 1);
        doReturn(account).when(accountRepository).save(Mockito.any());

        var result = accountService.save(accountDto);
        Mockito.verify(checkingRepository, times(1)).save(Mockito.any());
        Assertions.assertEquals(new GetAccountDto(account), result);
    }

    @Test
    @DisplayName("Test saving a savings account")
    void testSaveSavings() {
        var user = new User();
        user.setId(1);
        doReturn(user).when(authService).getCurrentUser();

        var accountDto = new PostAccountDto();
        accountDto.setBankId(1);
        accountDto.setType(PostAccountType.SAVINGS);
        var account = new Account(accountDto, 1);
        doReturn(account).when(accountRepository).save(Mockito.any());

        var result = accountService.save(accountDto);
        Mockito.verify(savingsRepository, times(1)).save(Mockito.any());
        Assertions.assertEquals(new GetAccountDto(account), result);
    }

    @Test
    @DisplayName("Test deleting a valid account")
    void testDelete() {
        var user = new User();
        user.setId(1);
        doReturn(user).when(authService).getCurrentUser();

        var account = new Account(1, 1, 1, "C", BigDecimal.TEN, false, new Date());
        doReturn(Optional.of(account)).when(accountRepository).findByIdAndClosedIsFalse(1);
        accountService.delete(1);
        Mockito.verify(accountRepository, times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Testing an invalid delete")
    void testDeleteInvalid() {
        doReturn(Optional.empty()).when(accountRepository).findByIdAndClosedIsFalse(1);
        Assertions.assertThrows(
                AccountNotFoundException.class,
                () -> accountService.delete(1),
                "Delete not throwing AccountNotFound for a missing account");

        var user = new User();
        user.setId(1);
        doReturn(user).when(authService).getCurrentUser();
        var account = new Account(1, 3, 1, "C", BigDecimal.TEN, false, new Date());
        doReturn(Optional.of(account)).when(accountRepository).findByIdAndClosedIsFalse(1);
        Assertions.assertThrows(
                UnauthorizedUserException.class,
                () -> accountService.delete(1),
                "User able to delete account they don't own");
    }
}
