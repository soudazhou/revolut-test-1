package com.revolut.tests.zkiss.transfersvc.validation;

import lombok.Data;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PrecisionValidatorTest {

    private Validator validator;

    @Before
    public void setUp() throws Exception {
        HibernateValidatorConfiguration configure = Validation.byProvider(HibernateValidator.class).configure();
        configure.messageInterpolator()
        validator =;
    }

    @Test
    public void shouldAllowNull() {
        TestClass testClass = new TestClass();

        Set<ConstraintViolation<TestClass>> violations = validator.validate(testClass);

        assertThat(violations).isEmpty();
    }

    @Test
    public void shouldAllowLowerPrecision() {
        TestClass testClass = new TestClass();
        testClass.setAmount(new BigDecimal("12.22"));

        Set<ConstraintViolation<TestClass>> violations = validator.validate(testClass);

        assertThat(violations).isEmpty();
    }

    @Test
    public void shouldAllowEqualPrecision() {
        TestClass testClass = new TestClass();
        testClass.setAmount(new BigDecimal("12.2222"));

        Set<ConstraintViolation<TestClass>> violations = validator.validate(testClass);

        assertThat(violations).isEmpty();
    }

    @Test
    public void shouldNotAllowHigherPrecision() {
        TestClass testClass = new TestClass();
        testClass.setAmount(new BigDecimal("12.22221"));

        Set<ConstraintViolation<TestClass>> violations = validator.validate(testClass);

        assertThat(violations).hasSize(1);
        ConstraintViolation<TestClass> violation = violations.stream().findAny().get();
        // Ignoring the problem of how to configure hibernate to use custom message bundles on top of its own ones
        assertThat(violation.getMessage()).isEqualTo("{com.revolut.tests.zkiss.transfersvc.validation.Precision.message}");
    }

    @Data
    public static class TestClass {
        @Precision(4)
        private BigDecimal amount;
    }

}