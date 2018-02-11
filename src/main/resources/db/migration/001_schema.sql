create table accounts(
    id varchar(36) primary key,
    account_number varchar(8) not null,
    sort_code varchar(6) not null,
    balance number(10,3) not null
);