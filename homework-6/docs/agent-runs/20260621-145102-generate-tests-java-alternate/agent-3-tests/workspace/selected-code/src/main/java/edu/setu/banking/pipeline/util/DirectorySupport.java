package edu.setu.banking.pipeline.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Stream;

public final class DirectorySupport {
  private DirectorySupport() {}

  public static void prepareShared(Path sharedDir) throws IOException {
    if (Files.exists(sharedDir)) {
      Path archiveRoot = sharedDir.getParent() == null ? Path.of("archive") : sharedDir.getParent().resolve("archive");
      Files.createDirectories(archiveRoot);
      int index = 1;
      Path target;
      do {
        target = archiveRoot.resolve(String.format("shared-%03d", index++));
      } while (Files.exists(target));
      Files.move(sharedDir, target, StandardCopyOption.ATOMIC_MOVE);
    }
    Files.createDirectories(sharedDir.resolve("input"));
    Files.createDirectories(sharedDir.resolve("processing"));
    Files.createDirectories(sharedDir.resolve("output"));
    Files.createDirectories(sharedDir.resolve("results"));
  }

  public static void deleteTree(Path root) throws IOException {
    if (!Files.exists(root)) {
      return;
    }
    try (Stream<Path> paths = Files.walk(root)) {
      for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
        Files.deleteIfExists(path);
      }
    }
  }
}
