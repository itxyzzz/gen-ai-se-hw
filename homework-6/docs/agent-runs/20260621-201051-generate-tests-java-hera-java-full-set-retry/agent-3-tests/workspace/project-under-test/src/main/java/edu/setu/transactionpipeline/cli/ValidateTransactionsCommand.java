package edu.setu.transactionpipeline.cli;

import edu.setu.transactionpipeline.agent.TransactionValidator;
import edu.setu.transactionpipeline.io.InputTransactionLoader;
import edu.setu.transactionpipeline.io.JsonCodec;
import edu.setu.transactionpipeline.model.ReasonCode;
import edu.setu.transactionpipeline.model.TransactionRecord;
import edu.setu.transactionpipeline.model.ValidationReport;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ValidateTransactionsCommand {
    public static void main(String[] args) {
        int code = new ValidateTransactionsCommand().run(args);
        if (code != 0) {
            System.exit(code);
        }
    }

    public int run(String[] args) {
        try {
            Path samplePath = args.length == 0 ? Path.of("sample-transactions.json") : Path.of(args[0]);
            ValidationReport report = validateOnly(samplePath);
            new JsonCodec().write(Path.of("validation-report.json"), report);
            return 0;
        } catch (Exception ex) {
            return 1;
        }
    }

    public ValidationReport validateOnly(Path samplePath) throws Exception {
        JsonCodec codec = new JsonCodec();
        InputTransactionLoader loader = new InputTransactionLoader(codec);
        TransactionValidator validator = new TransactionValidator();
        List<TransactionRecord> records = loader.load(samplePath);
        List<String> invalidIds = new ArrayList<>();
        Map<String, Integer> reasonCounts = new LinkedHashMap<>();
        int valid = 0;
        for (TransactionRecord record : records) {
            List<String> reasons = validator.validate(record);
            if (reasons.isEmpty()) {
                valid++;
            } else {
                invalidIds.add(record.transactionId());
                for (String reason : reasons) {
                    reasonCounts.merge(reason, 1, Integer::sum);
                }
            }
        }
        reasonCounts.putIfAbsent(ReasonCode.UNSUPPORTED_CURRENCY, reasonCounts.getOrDefault(ReasonCode.UNSUPPORTED_CURRENCY, 0));
        return new ValidationReport("1.0", Instant.now(), records.size(), valid, records.size() - valid, invalidIds, reasonCounts);
    }
}
