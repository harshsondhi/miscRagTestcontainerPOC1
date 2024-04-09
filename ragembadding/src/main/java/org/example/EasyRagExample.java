package org.example;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.example.common.Assistant;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments;
import static org.example.common.Utils.*;


import java.util.List;

public class EasyRagExample {

    final static String  OPENAI_KEY ="sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    public static void main(String[] args) {
        List<Document> documents = loadDocuments(toPath("documents/"),glob("*.pdf"));
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(OpenAiChatModel.withApiKey(OPENAI_KEY))
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(getRetriver(documents))
                .build();
        starConversationWithAssistant(assistant);
    }
    private static ContentRetriever getRetriver(List<Document> document){
        InMemoryEmbeddingStore<TextSegment> embaddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(document,embaddingStore);
        return  EmbeddingStoreContentRetriever.from(embaddingStore);

    }

}
