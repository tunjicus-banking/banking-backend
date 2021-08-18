use banking
go

drop table if exists users
go

create table users
(
    user_id    int identity primary key,
    username   nvarchar(255) not null,
    first_name nvarchar(255) not null,
    last_name  nvarchar(255) not null,

    constraint U_username unique (username)
)
go
