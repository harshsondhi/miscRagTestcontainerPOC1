package org.example.custom.build;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

import static java.time.Duration.ofSeconds;

public class CustomBuilder {

    static String MODEL_NAME = "llama2:latest";  //"orca-mini", "mistral", "llama2", "codellama", "phi" or "tinyllama"
    static String BASE_URL = "http://localhost:11434/"; //http://localhost:11434/
    static public ChatLanguageModel buildOllamaChatModel(){

        return OllamaChatModel.builder()
                .baseUrl(BASE_URL)
                .modelName(MODEL_NAME)
               .maxRetries(5)
                .timeout(ofSeconds(300))
                .temperature(.05)
                .build();
    }
}
