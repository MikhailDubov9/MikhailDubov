package com.mipt.mikhaildubov.IO;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class FileProcessorTest {

  @Test
  void testSplitAndMergeFile() throws IOException {
    FileProcessor processor = new FileProcessor();

    Path testFile = Files.createTempFile("test", ".dat");
    byte[] testData = new byte[1500];
    new Random().nextBytes(testData);
    Files.write(testFile, testData);

    String outputDir = Files.createTempDirectory("parts").toString();
    List<Path> parts = processor.splitFile(testFile.toString(), outputDir, 500);

    assertEquals(3, parts.size());

    assertEquals(500, Files.size(parts.get(0)));
    assertEquals(500, Files.size(parts.get(1)));
    assertEquals(500, Files.size(parts.get(2)));

    assertTrue(Files.exists(parts.get(0)));
    assertTrue(Files.exists(parts.get(1)));
    assertTrue(Files.exists(parts.get(2)));

    Path mergedFile = Files.createTempFile("merged", ".dat");
    processor.mergeFiles(parts, mergedFile.toString());

    assertArrayEquals(Files.readAllBytes(testFile), Files.readAllBytes(mergedFile));
  }
}
