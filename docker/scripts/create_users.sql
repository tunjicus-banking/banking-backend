use banking
go

drop table if exists users
go

create table users
(
    id int identity primary key,
    email varchar(255) not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,

    constraint U_Email unique (email)
)
    go

