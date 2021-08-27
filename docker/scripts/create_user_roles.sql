use banking
go

drop table if exists user_roles
go

create table user_roles
(
    user_id int not null,
    role_id int not null,

    constraint PK_user_roles primary key (user_id, role_id),
    constraint FK_users_user_roles foreign key (user_id)
        references users (user_id) on delete cascade,
    constraint FK_roles_user_roles foreign key (role_id)
        references roles (role_id) on delete cascade
)
go