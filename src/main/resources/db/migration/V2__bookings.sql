create table bookings(
id UUID not null primary key,
apartment_FK varchar(10) not null references apartments(number),
"date" date not null,
"hour" int not null,
machine int not null,
create_at timestamp default 'now()'
);