package edu.setu.transactionpipeline.agent;

import edu.setu.transactionpipeline.TestFixtures;
import edu.setu.transactionpipeline.model.FraudAssessment;
import edu.setu.transactionpipeline.model.ReasonCode;
import edu.setu.transactionpipeline.model.RiskTier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FraudDetectorTest {
    private final FraudDetector detector = new FraudDetector();

    @Test
    void classifiesHighValueAndOddHourTransactionsForReview() {
        FraudAssessment high = detector.assess(TestFixtures.highValue());
        assertEquals(RiskTier.HIGH, high.riskTier());
        assertTrue(high.reasonCodes().contains(ReasonCode.HIGH_VALUE));

        FraudAssessment odd = detector.assess(TestFixtures.oddHour());
        assertEquals(RiskTier.VERY_HIGH, odd.riskTier());
        assertTrue(odd.reasonCodes().contains(ReasonCode.ODD_HOUR));
    }
}
