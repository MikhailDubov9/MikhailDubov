package com.mipt.mikhaildubov.IO;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {

  public List<Path> splitFile(String sourcePath, String outputDir, int partSize)
      throws IOException {
    List<Path> partPaths = new ArrayList<>();
    Path sourceFile = Paths.get(sourcePath);
    String fileName = sourceFile.getFileName().toString();

    try (FileChannel sourceChannel = FileChannel.open(sourceFile, StandardOpenOption.READ)) {
      long fileSize = sourceChannel.size();
      long bytesRead = 0;
      int partNumber = 1;

      ByteBuffer buffer = ByteBuffer.allocate(partSize);

      while (bytesRead < fileSize) {
        buffer.clear();
        int bytes = sourceChannel.read(buffer);
        if (bytes <= 0) {
          break;
        }

        buffer.flip();
        String partFileName = fileName + ".part" + partNumber;
        Path partPath = Paths.get(outputDir, partFileName);

        try (FileChannel partChannel = FileChannel.open(partPath,
            StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
          partChannel.write(buffer);
        }

        partPaths.add(partPath);
        bytesRead += bytes;
        partNumber++;
      }
    }

    return partPaths;
  }

  public void mergeFiles(List<Path> partPaths, String outputPath) throws IOException {
    Path outputFile = Paths.get(outputPath);

    try (FileChannel outputChannel = FileChannel.open(outputFile,
        StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

      for (Path partPath : partPaths) {
        if (!Files.exists(partPath)) {
          throw new IOException("Part file not found: " + partPath);
        }

        try (FileChannel partChannel = FileChannel.open(partPath, StandardOpenOption.READ)) {
          ByteBuffer buffer = ByteBuffer.allocate(8192);

          while (partChannel.read(buffer) > 0) {
            buffer.flip();
            outputChannel.write(buffer);
            buffer.compact();
          }
        }
      }
    }
  }
}
