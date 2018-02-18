package com.revolut.tests.zkiss.transfersvc.persistence;

import com.revolut.tests.zkiss.transfersvc.domain.Account;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface AccountRepo {

    @SqlQuery("select * from accounts where sort_code = :sc and account_number = :accnum")
    @RegisterMapper(AccountRecordMapper.class)
    Account find(@Bind("sc") String sortCode, @Bind("accnum") String accountNumber);

    @SqlUpdate("insert into accounts (id, version, sort_code, account_number, balance, opened_at)" +
            " values (:id, :version, :sortCode, :accountNumber, :balance, :openedAt)")
    void insert(@BindBean Account account);

    class AccountRecordMapper implements ResultSetMapper<Account> {
        @Override
        public Account map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return Account.builder()
                    .id(r.getString("id"))
                    .version(r.getLong("version"))
                    .sortCode(r.getString("sort_code"))
                    .accountNumber(r.getString("account_number"))
                    .balance(r.getBigDecimal("balance"))
                    .openedAt(r.getTimestamp("opened_at").toInstant())
                    .build();
        }
    }
}
