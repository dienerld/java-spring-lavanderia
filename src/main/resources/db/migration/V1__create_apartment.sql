create table apartments (
number varchar(10) not null primary key,
name_resident varchar(30) not null,
phone varchar(11),
password varchar(64) not null,
token_login UUID,
create_at timestamp default 'now()',
update_at timestamp default 'now()'
);
