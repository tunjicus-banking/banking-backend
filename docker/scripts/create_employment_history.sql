use banking
go

drop table if exists employment_history
go

create table employment_history
(
    employment_history_id bigint identity primary key,
    user_id               int       not null,
    position_id           int       not null,
    salary                money     not null check (salary > 0),
    hire_date             datetime2 not null default getdate(),
    end_date              datetime2          default null,

    constraint FK_users_employment_history foreign key (user_id)
        references users (user_id) on delete cascade,
    constraint FK_positions_employment_history foreign key (position_id)
        references positions (position_id) on delete no action
)
go