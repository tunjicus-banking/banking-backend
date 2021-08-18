use banking
go

drop table if exists banks
go

create table banks
(
    bank_id  int identity primary key,
    funds    money         not null,
    location nvarchar(255) not null,
    name     nvarchar(255) not null
)
go

