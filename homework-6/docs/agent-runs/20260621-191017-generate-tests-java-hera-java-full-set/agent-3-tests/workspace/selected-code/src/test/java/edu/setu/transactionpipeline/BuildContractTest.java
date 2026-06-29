package edu.setu.transactionpipeline;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BuildContractTest {
    @Test
    void pomDeclaresRequiredBuildAndCoverageTools() throws Exception {
        String pom = Files.readString(Path.of("pom.xml"));
        assertTrue(pom.contains("jackson-databind"));
        assertTrue(pom.contains("junit-jupiter"));
        assertTrue(pom.contains("maven-surefire-plugin"));
        assertTrue(pom.contains("jacoco-maven-plugin"));
        assertTrue(pom.contains("<minimum>0.80</minimum>"));
        assertTrue(pom.contains("exec-maven-plugin"));
    }
}
