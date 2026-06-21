package edu.setu.transactionpipeline.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SharedDirectoryManagerTest {
    @TempDir
    Path tempDir;

    @Test
    void archivesExistingSharedTreeWithZeroPaddedName() throws Exception {
        Path shared = tempDir.resolve("shared");
        Path archive = tempDir.resolve("archive");
        Files.createDirectories(shared.resolve("results"));
        Files.writeString(shared.resolve("results").resolve("summary.json"), "{}");

        ProtocolPaths paths = new SharedDirectoryManager().prepareFreshSharedTree(shared, archive);

        assertTrue(Files.exists(archive.resolve("shared-001").resolve("results").resolve("summary.json")));
        assertTrue(Files.isDirectory(paths.input()));
        assertTrue(Files.isDirectory(paths.processing()));
        assertTrue(Files.isDirectory(paths.output()));
        assertTrue(Files.isDirectory(paths.results()));
    }
}
