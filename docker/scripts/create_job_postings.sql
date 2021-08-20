use banking
go

drop table if exists job_postings
go

create table job_postings
(
    job_posting_id int identity primary key,
    position_id    int           not null,
    description    nvarchar(max) not null,
    up_since       datetime2 default getdate(),
    active         bit       default 1,

    constraint FK_positions_job_postings foreign key (position_id)
        references positions (position_id)
)
go