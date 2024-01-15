package com.pubnub.api.endpoints.files;

import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("UnstableApiUsage")
public interface TestsWithFiles {
    TemporaryFolder folder = new TemporaryFolder();

    TemporaryFolder getTemporaryFolder();

    default File getTemporaryFile(String filename, String... content) {
        try {
            File file = getTemporaryFolder().newFile(filename);
            ArrayList<String> lines = new ArrayList<>();
            Collections.addAll(lines, content);
            Files.write(file.toPath(), lines);
            return file;
        } catch (IOException ex) {
            //fail
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

}
