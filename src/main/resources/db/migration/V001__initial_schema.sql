create table accounts(
    id varchar(36) primary key,
    account_number varchar(8) not null,
    sort_code varchar(6) not null,
    balance number(10,3) not null,
    opened_at timestamp not null
);

create table transactions(
    id varchar(36) primary key,
    account_id varchar(36) not null,
    amount number(10,3) not null,
    type varchar(32) not null,
    issued_at timestamp not null,

    foreign key (account_id) references accounts (id)
);