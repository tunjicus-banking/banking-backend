package com.tunjicus.bank.accounts.repositories;

import com.tunjicus.bank.accounts.models.Account;
import com.tunjicus.bank.accounts.models.IdAndCompanyId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Integer> {
    Optional<Account> findByUserIdEqualsAndFundsGreaterThanEqualAndTypeEqualsAndClosedIsFalse(
            int id, BigDecimal amount, String type);

    Optional<Account> findByUserIdEqualsAndTypeEqualsAndClosedIsFalse(int id, String type);

    Optional<Account> findByUserIdEqualsAndIdEqualsAndFundsGreaterThanAndClosedIsFalse(
            int userId, int accountId, BigDecimal amount);

    Optional<Account> findByUserIdEqualsAndIdEqualsAndClosedIsFalse(int userId, int accountId);

    Optional<Account> findByIdAndClosedIsFalse(int id);

    Optional<Account> findByUserIdAndTypeAndClosedIsFalse(int userId, String type);

    @Query("select sum(funds) from accounts where userId = ?1 and closed = false")
    Optional<BigDecimal> getNetWorth(int userId);

    @Query(
            "select a.id as id, p.companyId as companyId from employment_history eh "
                    + "inner join positions p on p.id = eh.positionId "
                    + "inner join accounts a on p.companyId = a.userId "
                    + "where eh.positionId = ?1")
    Optional<IdAndCompanyId> findCompanyAccount(int positionId);

    @Modifying
    @Transactional
    @Query("update accounts a set a.funds = a.funds + ?1 where a.id = ?2")
    void addMoney(BigDecimal funds, int id);
}
