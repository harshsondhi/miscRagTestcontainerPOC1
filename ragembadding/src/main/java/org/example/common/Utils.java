package org.example.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Scanner;

public class Utils {

    public static final String OPENAI_API_KEY = "sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    public static PathMatcher glob(String glob) {
        return FileSystems.getDefault().getPathMatcher("glob:" + glob);
    }

    public static void starConversationWithAssistant(Assistant assistant){

        Logger log = LoggerFactory.getLogger(Assistant.class);
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                log.info("==================================================");
                log.info("User: ");
                String userQuery = scanner.nextLine();
                log.info("==================================================");

                if ("exit".equalsIgnoreCase(userQuery)) {
                    break;
                }

                String agentAnswer = assistant.answer(userQuery);
                log.info("==================================================");
                log.info("Assistant: " + agentAnswer);
            }
        }

    }
    public static Path toPath(String relativePath) {
        try {
            URL fileUrl = Utils.class.getClassLoader().getResource(relativePath);
            assert fileUrl != null;
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
