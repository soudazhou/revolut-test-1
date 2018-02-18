package com.revolut.tests.zkiss.transfersvc.usecase;

import com.revolut.tests.zkiss.transfersvc.domain.Account;
import com.revolut.tests.zkiss.transfersvc.domain.AccountKey;
import com.revolut.tests.zkiss.transfersvc.domain.TransferRequest;
import com.revolut.tests.zkiss.transfersvc.domain.TransferResult;
import com.revolut.tests.zkiss.transfersvc.persistence.AccountRepo;
import com.revolut.tests.zkiss.transfersvc.util.TestDbRule;
import org.junit.Rule;
import org.junit.Test;
import org.skife.jdbi.v2.Handle;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferTest {

    @Rule
    public TestDbRule db = new TestDbRule();

    @Test
    public void shouldTransferWhenHasBalance() {
        db.getDbi().withHandle(h -> {
            insertAccount(h, "Alice", 20);
            insertAccount(h, "Bob", 40);
            return null;
        });

        Transfer transfer = new Transfer(request("Alice", "Bob", "13"), db.getDbi());

        TransferResult result = transfer.run();

        assertThat(result.isTransferred()).isTrue();
    }

    private void insertAccount(Handle handle, String id, int balance) {
        AccountRepo repo = handle.attach(AccountRepo.class);
        Account account = Account.builder()
                .sortCode("sort")
                .accountNumber(id)
                .balance(new BigDecimal(balance))
                .build();
        repo.insert(account);
    }

    private TransferRequest request(String from, String to, String amount) {
        return TransferRequest.builder()
                .from(AccountKey.builder()
                        .sortCode("sort")
                        .accountNumber(from)
                        .build())
                .to(AccountKey.builder()
                        .sortCode("sort")
                        .accountNumber(to)
                        .build())
                .amount(new BigDecimal(amount))
                .build();
    }
}