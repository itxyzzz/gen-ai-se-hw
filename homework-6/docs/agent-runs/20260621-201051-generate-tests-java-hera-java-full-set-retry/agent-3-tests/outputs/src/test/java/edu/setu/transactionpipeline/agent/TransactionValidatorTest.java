package edu.setu.transactionpipeline.agent;

import edu.setu.transactionpipeline.TestFixtures;
import edu.setu.transactionpipeline.model.PipelineMessage;
import edu.setu.transactionpipeline.model.ProcessingStatus;
import edu.setu.transactionpipeline.model.ReasonCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionValidatorTest {
    private final TransactionValidator validator = new TransactionValidator();

    @Test
    void acceptsValidLowRiskRecord() {
        PipelineMessage result = validator.processMessage(TestFixtures.message(TestFixtures.lowRisk()));
        assertEquals(ProcessingStatus.VALIDATED, result.status());
        assertTrue(result.reasonCodes().isEmpty());
    }

    @Test
    void rejectsUnsupportedCurrencyAndNonPositiveAmountWithStableCodes() {
        PipelineMessage unsupported = validator.processMessage(TestFixtures.message(TestFixtures.unsupportedCurrency()));
        assertEquals(ProcessingStatus.REJECTED, unsupported.status());
        assertTrue(unsupported.reasonCodes().contains(ReasonCode.UNSUPPORTED_CURRENCY));

        PipelineMessage nonPositive = validator.processMessage(TestFixtures.message(TestFixtures.nonPositiveAmount()));
        assertEquals(ProcessingStatus.REJECTED, nonPositive.status());
        assertTrue(nonPositive.reasonCodes().contains(ReasonCode.NON_POSITIVE_AMOUNT));
    }
}
