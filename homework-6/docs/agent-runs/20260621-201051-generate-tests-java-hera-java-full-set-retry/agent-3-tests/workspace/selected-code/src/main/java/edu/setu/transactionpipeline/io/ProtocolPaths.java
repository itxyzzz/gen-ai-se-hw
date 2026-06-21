package edu.setu.transactionpipeline.io;

import java.nio.file.Path;

public record ProtocolPaths(Path sharedRoot, Path input, Path processing, Path output, Path results) {
    public static ProtocolPaths under(Path sharedRoot) {
        return new ProtocolPaths(
                sharedRoot,
                sharedRoot.resolve("input"),
                sharedRoot.resolve("processing"),
                sharedRoot.resolve("output"),
                sharedRoot.resolve("results"));
    }
}
