use banking
go

drop table if exists accounts
go

create table accounts
(
    account_id int identity,
    type       nvarchar(1) not null,
    bank_id    int         not null,
    user_id    int         not null,
    funds      money       not null,
    closed     bit       default 0,
    created_at datetime2 default GETDATE(),

    constraint PK_accounts primary key (account_id, type),
    constraint CHK_Accounts_type check (type in ('C', 'S')),
    constraint FK_users_accounts foreign key (user_id)
        references users (user_id) on delete cascade,
    constraint FK_banks_accounts foreign key (bank_id)
        references banks (bank_id) on delete cascade
)
go