package edu.setu.transactionpipeline.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SharedDirectoryManager {
    public ProtocolPaths prepareFreshSharedTree(Path sharedRoot, Path archiveRoot) throws IOException {
        if (Files.exists(sharedRoot)) {
            Files.createDirectories(archiveRoot);
            Files.move(sharedRoot, nextArchivePath(archiveRoot));
        }
        ProtocolPaths paths = ProtocolPaths.under(sharedRoot);
        Files.createDirectories(paths.input());
        Files.createDirectories(paths.processing());
        Files.createDirectories(paths.output());
        Files.createDirectories(paths.results());
        return paths;
    }

    public Path nextArchivePath(Path archiveRoot) throws IOException {
        Files.createDirectories(archiveRoot);
        int index = 1;
        Path candidate;
        do {
            candidate = archiveRoot.resolve("shared-%03d".formatted(index));
            index++;
        } while (Files.exists(candidate));
        return candidate;
    }
}
