package edu.setu.transactionpipeline.agent;

import edu.setu.transactionpipeline.TestFixtures;
import edu.setu.transactionpipeline.model.PipelineMessage;
import edu.setu.transactionpipeline.model.ProcessingStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SettlementProcessorTest {
    @Test
    void mapsLowRiskToSettledAndRiskToReview() {
        TransactionValidator validator = new TransactionValidator();
        FraudDetector detector = new FraudDetector();
        SettlementProcessor settlement = new SettlementProcessor();

        PipelineMessage low = settlement.processMessage(detector.processMessage(validator.processMessage(TestFixtures.message(TestFixtures.lowRisk()))));
        assertEquals(ProcessingStatus.SETTLED, low.status());

        PipelineMessage high = settlement.processMessage(detector.processMessage(validator.processMessage(TestFixtures.message(TestFixtures.highValue()))));
        assertEquals(ProcessingStatus.REVIEW_REQUIRED, high.status());
    }
}
