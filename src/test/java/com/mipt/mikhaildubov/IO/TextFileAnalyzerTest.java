package com.mipt.mikhaildubov.IO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mipt.mikhaildubov.IO.TextFileAnalyzer.AnalysisResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TextFileAnalyzerTest {

  @Test
  void testAnalyzeFile() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    Path testFile = Files.createTempFile("test", ".txt");
    Files.write(testFile, Arrays.asList("Hello world!", "This is test."));

    AnalysisResult result = analyzer.analyzeFile(testFile.toString());

    assertEquals(2, result.getLineCount());
    assertEquals(5, result.getWordCount());
    assertTrue(result.getCharCount() > 0);
    assertTrue(result.getCharFrequency().containsKey('!'));
    assertTrue(result.getCharFrequency().containsKey(' '));
    assertTrue(result.getCharFrequency().containsKey('t'));
  }

  @Test
  void testSaveAnalysisResult() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    Map<Character, Long> charFrequency = Map.of('!', 1L, '\n', 1L);
    AnalysisResult result = new AnalysisResult(2, 5, 20, charFrequency);

    Path outputFile = Files.createTempFile("analysis", ".txt");
    analyzer.saveAnalysisResult(result, outputFile.toString());

    assertTrue(Files.size(outputFile) > 0);

    String output = Files.readString(outputFile);
    assertTrue(output.contains("wordCount = 5"));
    assertTrue(output.contains("!: 1"));
  }
}
