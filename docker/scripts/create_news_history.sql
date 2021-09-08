use banking
go

drop table if exists news_history
go

create table news_history
(
    id         int identity primary key,
    title      varchar(max)    not null,
    sentiment  varchar(7)      not null check (sentiment in ('POS', 'NEG', 'NEUTRAL')),
    modifier   numeric(19, 18) not null check (modifier <= 1),
    created_at datetime2       not null default getdate()
)
go