use banking
go

drop table if exists users
go

create table users
(
    user_id    int identity primary key,
    username   varchar(255) not null,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,

    constraint U_username unique (username)
)
go
