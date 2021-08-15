use banking
go

drop table if exists transactions
go

create table transactions
(
    transaction_id   bigint identity,
    from_user        int   not null,
    to_user          int   not null,
    amount           money not null check (amount > 0),
    transaction_time datetime2 default getdate(),

    constraint FK_users_transactions_from_user foreign key (from_user)
        references users (user_id) on delete no action,
    constraint FK_users_transactions_to_user foreign key (to_user)
        references users (user_id) on delete cascade
)
go