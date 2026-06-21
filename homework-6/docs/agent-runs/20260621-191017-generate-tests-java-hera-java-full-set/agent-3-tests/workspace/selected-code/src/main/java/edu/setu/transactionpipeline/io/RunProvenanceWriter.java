package edu.setu.transactionpipeline.io;

import edu.setu.transactionpipeline.cli.PipelineOptions;
import edu.setu.transactionpipeline.model.RunProvenance;
import edu.setu.transactionpipeline.privacy.PrivacyGuard;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;

public final class RunProvenanceWriter {
    private final JsonCodec codec;
    private final PrivacyGuard privacyGuard;
    private final Clock clock;

    public RunProvenanceWriter(JsonCodec codec, PrivacyGuard privacyGuard, Clock clock) {
        this.codec = codec;
        this.privacyGuard = privacyGuard;
        this.clock = clock;
    }

    public RunProvenance fromOptions(PipelineOptions options) {
        return new RunProvenance(
                "1.0",
                "run-" + Instant.now(clock).toString().replace(":", "").replace(".", "-"),
                Instant.now(clock),
                options.sourceSpecRunId(),
                options.sourceSpecPath(),
                options.sourceSpecFingerprint(),
                options.pipelineVersionId(),
                options.pipelineInventoryPath(),
                options.pipelinePackageFingerprint());
    }

    public Path write(Path sharedRoot, RunProvenance provenance) throws IOException {
        Path target = sharedRoot.resolve("run-provenance.json");
        codec.write(target, provenance);
        privacyGuard.verifyPrivacySafe(codec.readTree(target));
        return target;
    }
}
