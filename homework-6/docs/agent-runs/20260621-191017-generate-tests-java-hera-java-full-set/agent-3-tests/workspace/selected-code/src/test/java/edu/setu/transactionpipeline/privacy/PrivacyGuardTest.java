package edu.setu.transactionpipeline.privacy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrivacyGuardTest {
    private final PrivacyGuard guard = new PrivacyGuard();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void rejectsRawSensitiveValuesAndAllowsSafeResults() throws Exception {
        String account = "ACC-" + "1001";
        String description = "Monthly " + "rent payment";
        assertThrows(IllegalArgumentException.class, () -> guard.verifyPrivacySafe(mapper.readTree("{\"value\":\"" + account + "\"}")));
        assertThrows(IllegalArgumentException.class, () -> guard.verifyPrivacySafe(mapper.readTree("{\"description\":\"" + description + "\"}")));
        assertDoesNotThrow(() -> guard.verifyPrivacySafe(mapper.readTree("{\"transaction_id\":\"TXN001\",\"status\":\"settled\"}")));
    }
}
