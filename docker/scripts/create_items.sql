use banking
go

drop table if exists items
go

create table items
(
    item_id     int identity primary key,
    user_id     int          not null,
    name        varchar(255) not null,
    description varchar(max),
    price       money        not null check (price >= 0),

    constraint FK_users_items foreign key (user_id)
        references users (user_id) on delete cascade,
    constraint U_name unique (name)
)
go