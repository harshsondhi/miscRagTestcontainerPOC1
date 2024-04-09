package com.sondhi.harsh.testcontainerpoc1.service;

import com.sondhi.harsh.testcontainerpoc1.model.PromptResponse;

public interface AiGenerateServiceI{
    PromptResponse generateMessage(String prompt);
}
