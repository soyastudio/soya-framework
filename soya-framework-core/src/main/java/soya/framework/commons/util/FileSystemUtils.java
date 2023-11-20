package soya.framework.commons.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import static java.nio.file.FileVisitOption.FOLLOW_LINKS;

public abstract class FileSystemUtils {

    public static boolean deleteRecursively(File root) {
        if (root == null) {
            return false;
        }

        try {
            return deleteRecursively(root.toPath());
        }
        catch (IOException ex) {
            return false;
        }
    }

    public static boolean deleteRecursively(Path root) throws IOException {
        if (root == null) {
            return false;
        }
        if (!Files.exists(root)) {
            return false;
        }

        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
        return true;
    }

    public static void copyRecursively(File src, File dest) throws IOException {
        AssertUtils.requireNonNull(src, "Source File must not be null");
        AssertUtils.requireNonNull(dest, "Destination File must not be null");
        copyRecursively(src.toPath(), dest.toPath());
    }

    public static void copyRecursively(Path src, Path dest) throws IOException {
        AssertUtils.requireNonNull(src, "Source Path must not be null");
        AssertUtils.requireNonNull(dest, "Destination Path must not be null");
        BasicFileAttributes srcAttr = Files.readAttributes(src, BasicFileAttributes.class);

        if (srcAttr.isDirectory()) {
            Files.walkFileTree(src, EnumSet.of(FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Files.createDirectories(dest.resolve(src.relativize(dir)));
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.copy(file, dest.resolve(src.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        else if (srcAttr.isRegularFile()) {
            Files.copy(src, dest);
        }
        else {
            throw new IllegalArgumentException("Source File must denote a directory or file");
        }
    }

}
