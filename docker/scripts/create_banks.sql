use banking
go

drop table if exists banks;
go

create table banks
(
    id int identity primary key,
    funds numeric(19,2) not null,
    location varchar(255) not null,
    name varchar(255) not null
)
go

