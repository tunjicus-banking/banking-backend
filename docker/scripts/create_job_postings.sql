use banking
go

drop table if exists job_postings
go

create table job_postings
(
    job_posting_id int identity primary key,
    position_id    int           not null,
    description    nvarchar(max) not null,
    salary_low     money         not null,
    salary_high    money         not null,
    up_since       datetime2 default getdate(),
    active         bit       default 1,

    constraint FK_positions_job_postings foreign key (position_id)
        references positions (position_id),
    constraint CHK_job_postings_salary_range check
        (salary_high >= job_postings.salary_low and salary_high > 0)
)
go