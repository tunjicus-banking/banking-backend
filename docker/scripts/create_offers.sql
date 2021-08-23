use banking
go

drop table if exists offers
go

create table offers
(
    offer_id       bigint identity primary key,
    job_posting_id int       not null,
    user_id        int       not null,
    salary         money     not null,
    accepted       smallint  not null default 0 check (accepted between -1 and 1),
    offer_time     datetime2 not null default getdate(),

    constraint FK_job_postings_offers foreign key (job_posting_id)
        references job_postings (job_posting_id),
    constraint FK_users_offers foreign key (user_id)
        references users (user_id)
)
go