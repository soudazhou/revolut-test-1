package com.revolut.tests.zkiss.transfersvc.usecase;

import com.revolut.tests.zkiss.transfersvc.domain.Account;
import com.revolut.tests.zkiss.transfersvc.domain.SortCodeAccountNumber;
import com.revolut.tests.zkiss.transfersvc.domain.TransferRequest;
import com.revolut.tests.zkiss.transfersvc.domain.TransferResult;
import com.revolut.tests.zkiss.transfersvc.persistence.AccountRepo;
import com.revolut.tests.zkiss.transfersvc.util.TestDbRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.skife.jdbi.v2.Handle;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferTest {

    public static final String SORT_CODE = "sort";
    @Rule
    public TestDbRule db = new TestDbRule();

    private String aliceId;
    private String bobId;

    @Before
    public void setUp() throws Exception {
        db.getDbi().useHandle(h -> {
            aliceId = insertAccount(h, "Alice", 20).getId();
            bobId = insertAccount(h, "Bob", 40).getId();
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
            assertThat(alice.getVersion()).isEqualTo(2);
            assertThat(bob.getBalance()).isEqualByComparingTo("53");
            assertThat(bob.getVersion()).isEqualTo(2);
        });
    }

    @Test
    public void shouldRegisterTransactionsWhenTransferred() {
        Transfer transfer = new Transfer(
                request("Alice", "Bob", "13.5", "Thanks for the loan"),
                db.getDbi()
        );

        TransferResult result = transfer.run();

        assertThat(result.isTransferred()).isTrue();
        db.getDbi().useHandle(h -> {
            Map<String, Object> aliceTx = h.createQuery("select * from transactions where account_id = :accountId")
                    .bind("accountId", aliceId)
                    .first();
            assertThat(aliceTx.get("amount")).isEqualTo(new BigDecimal("13.500"));
            assertThat(aliceTx.get("type")).isEqualTo("OUT");
            assertThat(aliceTx.get("message")).isEqualTo("Thanks for the loan");

            Map<String, Object> bobTx = h.createQuery("select * from transactions where account_id = :accountId")
                    .bind("accountId", bobId)
                    .first();
            assertThat(bobTx.get("amount")).isEqualTo(new BigDecimal("13.500"));
            assertThat(bobTx.get("type")).isEqualTo("IN");
            assertThat(bobTx.get("message")).isEqualTo("Thanks for the loan");
        });
    }

    /*
     * Not using error code constants that are defined in production code.
     * This is because I want this test to fail in case the error codes change.
     * Error codes are a part of our API contract and consumers have to adapt.
     * Until they do we need to ensure backwards compatibility so they need to remain the same.
     * So these tests also act like consumer contract tests
     */

    @Test
    public void shouldNotTransferIfInsufficientBalance() {
        Transfer transfer = new Transfer(request("Bob", "Alice", "100.4"), db.getDbi());

        TransferResult result = transfer.run();

        assertThat(result.isTransferred()).isFalse();
        assertThat(result.getErrorCode()).isEqualTo("from.insufficient-funds");
        db.getDbi().useHandle(h -> {
            assertThat(txCount(h)).isEqualTo(0);
            AccountRepo repo = h.attach(AccountRepo.class);

            Account bob = repo.find(keyFor("Bob"));
            assertThat(bob.getBalance()).isEqualByComparingTo("40");

            Account alice = repo.find(keyFor("Alice"));
            assertThat(alice.getBalance()).isEqualByComparingTo("20");
        });
    }

    @Test
    public void shouldFailWhenFromDoesNotExist() {
        Transfer transfer = new Transfer(request("missing", "Alice", "14"), db.getDbi());

        TransferResult result = transfer.run();

        assertThat(result.isTransferred()).isFalse();
        assertThat(result.getErrorCode()).isEqualTo("from.not-found");
        db.getDbi().useHandle(h -> {
            assertThat(txCount(h)).isEqualTo(0);
            AccountRepo repo = h.attach(AccountRepo.class);
            Account alice = repo.find(keyFor("Alice"));
            assertThat(alice.getBalance()).isEqualByComparingTo("20");
        });
    }

    @Test
    public void shouldFailWhenToDoesNotExist() {
        Transfer transfer = new Transfer(request("Alice", "missing", "14"), db.getDbi());

        TransferResult result = transfer.run();

        assertThat(result.isTransferred()).isFalse();
        assertThat(result.getErrorCode()).isEqualTo("to.not-found");
        db.getDbi().useHandle(h -> {
            assertThat(txCount(h)).isEqualTo(0);
            AccountRepo repo = h.attach(AccountRepo.class);
            Account alice = repo.find(keyFor("Alice"));
            assertThat(alice.getBalance()).isEqualByComparingTo("20");
        });
    }

    private Integer txCount(Handle h) {
        return h.createQuery("select count(*) from transactions")
                .map((index, r, ctx) -> r.getInt(1))
                .first();
    }

    private Account insertAccount(Handle handle, String id, int balance) {
        AccountRepo repo = handle.attach(AccountRepo.class);
        Account account = Account.builder()
                .sortCode(SORT_CODE)
                .accountNumber(id)
                .balance(new BigDecimal(balance))
                .version(1)
                .build();
        repo.insert(account);
        return account;
    }

    private TransferRequest request(String from, String to, String amount) {
        return request(from, to, amount, null);
    }

    private TransferRequest request(String from, String to, String amount, String message) {
        return TransferRequest.builder()
                .from(keyFor(from))
                .to(keyFor(to))
                .amount(new BigDecimal(amount))
                .message(message)
                .build();
    }

    private SortCodeAccountNumber keyFor(String from) {
        return SortCodeAccountNumber.builder()
                .sortCode(SORT_CODE)
                .accountNumber(from)
                .build();
    }
}