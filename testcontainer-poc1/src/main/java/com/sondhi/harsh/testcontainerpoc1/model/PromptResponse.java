package com.sondhi.harsh.testcontainerpoc1.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;



//@Getter
//@Setter
@Data
@Accessors(chain = true)
public class PromptResponse {
     private String queryPrompt;
     private String llmResponse;

     public PromptResponse(String queryPrompt, String llmResponse) {
          this.queryPrompt = queryPrompt;
          this.llmResponse = llmResponse;
     }

     public String getQueryPrompt() {
          return queryPrompt;
     }

     public String getLlmResponse() {
          return llmResponse;
     }

     //    @Override
//    public String toString() {
//        return "LlamaPromptResponse{" +
//                "#########################----->  " + '\'' +
//                "queryPrompt='" + queryPrompt + '\'' +
//                "#########################----->  " + '\'' +
//                ", llmResponse='" + llmResponse + '\'' +
//                '}';
//    }
}
