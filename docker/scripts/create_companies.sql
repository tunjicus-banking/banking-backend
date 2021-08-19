use banking
go

drop table if exists companies
go

create table companies
(
    company_id  int                                            not null,
    name        nvarchar(500)                                  not null,
    description nvarchar(1000)                                 not null,
    brand_color int check (brand_color between 0 and 16777215) not null,

    constraint PK_companies primary key (company_id),
    constraint FK_users_companies foreign key (company_id)
        references users (user_id)
)
go