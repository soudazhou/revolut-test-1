package com.revolut.tests.zkiss.transfersvc.usecase;

import com.revolut.tests.zkiss.transfersvc.domain.Account;
import com.revolut.tests.zkiss.transfersvc.domain.AccountKey;
import com.revolut.tests.zkiss.transfersvc.domain.TransferRequest;
import com.revolut.tests.zkiss.transfersvc.domain.TransferResult;
import com.revolut.tests.zkiss.transfersvc.persistence.AccountRepo;
import com.revolut.tests.zkiss.transfersvc.util.TestDbRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.skife.jdbi.v2.Handle;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferTest {

    public static final String SORT_CODE = "sort";
    @Rule
    public TestDbRule db = new TestDbRule();

    @Before
    public void setUp() throws Exception {
        db.getDbi().useHandle(h -> {
            insertAccount(h, "Alice", 20);
            insertAccount(h, "Bob", 40);
        });
    }

    @Test
    public void shouldTransferWhenHasBalance() {
        Transfer transfer = new Transfer(request("Alice", "Bob", "13"), db.getDbi());

        TransferResult result = transfer.run();

        assertThat(result.isTransferred()).isTrue();
        db.getDbi().useHandle(h -> {
            AccountRepo repo = h.attach(AccountRepo.class);
            Account alice = repo.find(keyFor("Alice"));
            Account bob = repo.find(keyFor("Bob"));
            assertThat(alice.getBalance()).isEqualByComparingTo("7");
            assertThat(bob.getBalance()).isEqualByComparingTo("53");
        });
    }

    private void insertAccount(Handle handle, String id, int balance) {
        AccountRepo repo = handle.attach(AccountRepo.class);
        Account account = Account.builder()
                .sortCode(SORT_CODE)
                .accountNumber(id)
                .balance(new BigDecimal(balance))
                .build();
        repo.insert(account);
    }

    private TransferRequest request(String from, String to, String amount) {
        return TransferRequest.builder()
                .from(keyFor(from))
                .to(keyFor(to))
                .amount(new BigDecimal(amount))
                .build();
    }

    private AccountKey keyFor(String from) {
        return AccountKey.builder()
                .sortCode(SORT_CODE)
                .accountNumber(from)
                .build();
    }
}