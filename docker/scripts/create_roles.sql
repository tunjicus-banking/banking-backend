use banking
go

drop table if exists roles
go

create table roles
(
    role_id int identity primary key,
    name varchar(250) not null
)
go