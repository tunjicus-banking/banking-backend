use banking
go

drop table if exists positions
go

create table positions
(
    position_id int identity primary key,
    company_id  int           not null,
    title       nvarchar(500) not null,
    description nvarchar(1000),
    active      bit           not null default 1,

    constraint FK_companies_positions foreign key (company_id)
        references companies (company_id) on delete cascade
)