package com.mipt.mikhaildubov.IO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextFileAnalyzer {

  public static class AnalysisResult {

    private final long lineCount;
    private final long wordCount;
    private final long charCount;
    private final Map<Character, Long> charFrequency;

    AnalysisResult(long lineCount, long wordCount, long charCount,
        Map<Character, Long> charFrequency) {
      this.lineCount = lineCount;
      this.wordCount = wordCount;
      this.charCount = charCount;
      this.charFrequency = charFrequency;
    }

    public long getLineCount() {
      return lineCount;
    }

    public long getWordCount() {
      return wordCount;
    }

    public long getCharCount() {
      return charCount;
    }

    public Map<Character, Long> getCharFrequency() {
      return charFrequency;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{lineCount = ").append(lineCount)
          .append(", wordCount = ").append(wordCount)
          .append(", charCount = ").append(charCount)
          .append(", charFrequency = {");

      boolean first = true;
      for (Map.Entry<Character, Long> entry : charFrequency.entrySet()) {
        if (!first) {
          sb.append(", ");
        }

        first = false;

        char c = entry.getKey();
        String current;
        switch (c) {
          case '\n':
            current = "\\n";
            break;

          case ' ':
            current = "space";
            break;

          default:
            current = String.valueOf(c);
        }

        sb.append(current).append(": ").append(entry.getValue());
      }
      sb.append("}}");

      return sb.toString();
    }
  }

  public AnalysisResult analyzeFile(String filePath) throws IOException {
    long lineCount = 0;
    long wordCount = 0;
    long charCount = 0;
    Map<Character, Long> charFrequency = new HashMap<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;

      while ((line = reader.readLine()) != null) {
        lineCount++;

        String trimmedLine = line.trim();
        if (!trimmedLine.isEmpty()) {
          wordCount += trimmedLine.split("\\s+").length;
        }

        charCount += line.length();

        for (char c : line.toCharArray()) {
          charFrequency.put(c, charFrequency.getOrDefault(c, 0L) + 1);
        }
      }
    }

    return new AnalysisResult(lineCount, wordCount, charCount, charFrequency);
  }

  public void saveAnalysisResult(AnalysisResult result, String outputPath) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
      writer.write(result.toString());
    }
  }
}