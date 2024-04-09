package com.sondhi.harsh.testcontainerpoc1.controller;

import com.sondhi.harsh.testcontainerpoc1.model.PromptResponse;
import com.sondhi.harsh.testcontainerpoc1.service.AiGenerateServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LLamaController {

    private final AiGenerateServiceI aiGenerateService;

    public LLamaController(AiGenerateServiceI aiGenerateService) {
        this.aiGenerateService = aiGenerateService;
    }

    @GetMapping("api/v1/ai/generate")
    public ResponseEntity<PromptResponse> generate(
            @RequestParam(value = "promptMessage", defaultValue = "Why is the sky blue?")
            String promptMessage) {
        final PromptResponse aiResponse =aiGenerateService.generateMessage(promptMessage);
        return ResponseEntity.status(HttpStatus.OK).body(aiResponse);
    }
}
