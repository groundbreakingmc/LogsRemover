package groundbreaking.logsremover.utils;

import groundbreaking.logsremover.LogsRemover;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

public class FileRemover {

    private final LogsRemover plugin;

    public FileRemover(final LogsRemover plugin) {
        this.plugin = plugin;
    }

    public void removeAll() {
        final String logsDirectory = this.plugin.getLogsDirectory();
        try (final Stream<Path> files = Files.list(Paths.get(logsDirectory))) {
            files.forEach(path -> {
                if (this.getFileExtension(path.getFileName().toString()).equals("log.gz")) {
                    try {
                        Files.delete(path);
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (final IOException ex) {
            this.plugin.getLogger().warning("Failed to read files: " + ex.getMessage());
        }
    }

    public void removeTime() {
        final String logsDirectory = this.plugin.getLogsDirectory();
        try (final Stream<Path> files = Files.list(Paths.get(logsDirectory))) {
            files.forEach(path -> {
                if (this.getFileExtension(path.getFileName().toString()).equals("log.gz")) {
                    final BasicFileAttributes attrs;
                    try {
                        attrs = Files.readAttributes(path, BasicFileAttributes.class);
                        final Instant creationTime = attrs.creationTime().toInstant();

                        final Instant now = Instant.now();
                        final Duration age = Duration.between(creationTime, now);

                        if (age.toHours() > this.plugin.getRemoveAfter()) {
                            Files.delete(path);
                        }
                    } catch (final IOException ex) {
                        this.plugin.getLogger().warning("Failed to read attributes: " + ex.getMessage());
                    }
                }
            });
        } catch (final IOException ex) {
            this.plugin.getLogger().warning("Failed to read files: " + ex.getMessage());
        }
    }

    private String getFileExtension(final String filename) {
        final int dotIndex = filename.indexOf('.');
        return dotIndex == -1 ? "" : filename.substring(dotIndex + 1);
    }
}
