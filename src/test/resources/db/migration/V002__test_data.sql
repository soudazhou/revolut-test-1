insert into accounts (id, account_number, sort_code, balance, opened_at)
values ('alice', '00000001', '112233', 100, '2018-01-01 00:00:00');
insert into accounts (id, account_number, sort_code, balance, opened_at)
values ('bob', '00000002', '112233', 100, '2018-01-02 00:00:00');

insert into transactions (id, account_id, amount, type, issued_at)
values ('alice-to-bob-alice', 'alice', 50, 'OUT', '2018-01-15 14:00:00');
insert into transactions (id, account_id, amount, type, issued_at)
values ('alice-to-bob-bob', 'bob', 50, 'IN', '2018-01-15 14:00:00');
