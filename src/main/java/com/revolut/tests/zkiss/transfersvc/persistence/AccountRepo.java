package com.revolut.tests.zkiss.transfersvc.persistence;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean;

public interface AccountRepo {
    @SqlQuery("select * from accounts where sort_code = :sc and account_number = :an")
    @MapResultAsBean
    AccountRecord find(@Bind("sc") String sortCode, @Bind("an") String accountNumber);

    @SqlUpdate("insert into accounts (id, version, sort_code, account_number, balance, opened_at)" +
            " values (:id, :version, :sortCode, :accountNumber, :balance, :openedAt)")
    void insert(@BindBean AccountRecord accountRecord);
}
