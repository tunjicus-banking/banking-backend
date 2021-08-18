use banking
go

drop table if exists savings_accounts
go

create table savings_accounts
(
    account_id        int           not null,
    type              nvarchar(1)   not null,
    transaction_limit tinyint default 6,
    interest_rate     numeric(5, 5) not null,

    constraint CHK_savings_accounts_type check (type = 'S'),
    constraint CHK_savings_accounts_transaction_limit check (transaction_limit between 1 and 100),
    constraint CHK_savings_accounts_interest_rate check (interest_rate < 1),
    constraint FK_accounts_savings foreign key (account_id, type)
        references accounts (account_id, type),
    constraint PK_savings_accounts primary key (account_id, type)
)
go