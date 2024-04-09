package com.sondhi.harsh.testcontainerpoc1.service.implimentaion;

import com.sondhi.harsh.testcontainerpoc1.model.PromptResponse;
import com.sondhi.harsh.testcontainerpoc1.service.AiGenerateServiceI;
import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;


@Service
public class AiGenerateServiceImplmpl implements AiGenerateServiceI {

    private final ChatClient chatClient;

    public AiGenerateServiceImplmpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public PromptResponse generateMessage(String prompt) {
        final String llamaMessage = chatClient.call(prompt);
        return  new PromptResponse(prompt,llamaMessage);
    }
}
