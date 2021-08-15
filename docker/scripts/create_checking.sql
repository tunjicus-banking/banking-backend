use banking
go

drop table if exists checking_accounts
go

create table checking_accounts
(
    account_id       int        not null,
    type             varchar(1) not null,
    maintenance_fees money default 3.00,

    constraint PK_checking_accounts primary key (account_id, type),
    constraint FK_accounts_checking foreign key (account_id, type)
        references accounts (account_id, type),
    constraint CHK_checking_accounts_type check (type = 'C'),
    constraint CHK_checking_accounts_maintenance_fees check (maintenance_fees >= 0)
)
go