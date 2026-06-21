package edu.setu.transactionpipeline.io;

import edu.setu.transactionpipeline.model.PipelineMessage;

import java.io.IOException;
import java.nio.file.Path;

public final class ProtocolFileMover {
    private final JsonCodec codec;

    public ProtocolFileMover(JsonCodec codec) {
        this.codec = codec;
    }

    public void writeProcessingState(ProtocolPaths paths, PipelineMessage message, String component, int sequenceNumber) throws IOException {
        codec.write(paths.processing().resolve("%03d-%s-%s.json".formatted(
                sequenceNumber,
                safeTransactionId(message),
                component)), message);
    }

    public void writeOutputState(ProtocolPaths paths, PipelineMessage message, String component, int sequenceNumber) throws IOException {
        codec.write(paths.output().resolve("%03d-%s-%s.json".formatted(
                sequenceNumber,
                safeTransactionId(message),
                component)), message);
    }

    private static String safeTransactionId(PipelineMessage message) {
        return message.transaction().transactionId() == null ? "unknown" : message.transaction().transactionId();
    }
}
