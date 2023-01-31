package io.github.simonharmonicminor.code.style;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
class FileUtil {

  @SneakyThrows
  public File copyContentToTempFile(String resourceFileName, String tempFileName) {
    File temp = File.createTempFile(tempFileName, ".tmp");
    try (OutputStream out = Files.newOutputStream(temp.toPath())) {
      try (InputStream resourceStream = FileUtil.class.getClassLoader()
          .getResourceAsStream(resourceFileName)) {
        byte[] buffer = new byte[1024];
        int bytes = resourceStream.read(buffer);
        while (bytes >= 0) {
          out.write(buffer, 0, bytes);
          bytes = resourceStream.read(buffer);
        }
      }
    }
    return temp;
  }
}
