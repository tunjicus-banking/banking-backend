use banking
go

drop table if exists requirements
go

create table requirements
(
    requirement_id      int primary key,
    experience          double precision not null default 0 check (experience is null or experience >= 0),
    company_experience  double precision not null default 0 check (company_experience is null or company_experience >= 0),
    net_worth           money            not null default 0 check (net_worth is null or net_worth >= 0),
    current_salary      money            not null default 0 check (current_salary is null or current_salary >= 0),
    modifier            tinyint          not null default 0 check (modifier is null or modifier <= 100),
    required_completion tinyint          not null check (required_completion <= 100),

    constraint FK_job_postings_requirements foreign key (requirement_id)
        references job_postings (job_posting_id) on delete cascade
)
go

drop table if exists requirements_info
go

create table requirements_info
(
    requirement_id int,
    requirement    varchar(3) not null,
    specification  varchar(1) not null check (specification in ('R', 'N', 'P')),

    constraint PK_requirements_info primary key (requirement_id, requirement),
    constraint FK_requirements_requirements_info foreign key (requirement_id)
        references requirements (requirement_id),
    constraint CHK_requirements_info_requirement check (requirement in ('EX', 'CX', 'NW', 'CS'))
)