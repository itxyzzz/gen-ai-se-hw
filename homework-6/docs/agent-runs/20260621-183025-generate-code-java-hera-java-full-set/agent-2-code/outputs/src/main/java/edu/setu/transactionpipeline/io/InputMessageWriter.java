package edu.setu.transactionpipeline.io;

import edu.setu.transactionpipeline.model.PipelineMessage;

import java.io.IOException;
import java.nio.file.Path;

public final class InputMessageWriter {
    private final JsonCodec codec;

    public InputMessageWriter(JsonCodec codec) {
        this.codec = codec;
    }

    public Path writeInitialMessage(ProtocolPaths paths, PipelineMessage message, int sequenceNumber) throws IOException {
        String transactionId = message.transaction().transactionId() == null ? "unknown" : message.transaction().transactionId();
        Path target = paths.input().resolve("%03d-%s-transaction-validator.json".formatted(sequenceNumber, transactionId));
        codec.write(target, message);
        return target;
    }
}
