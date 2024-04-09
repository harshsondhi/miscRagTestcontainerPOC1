package org.example;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.bge.small.en.v15.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.example.common.Assistant;

import java.util.List;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;
import org.example.common.*;

public class NaiveRagExample {

    static String  OPENAI_KEY ="sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    public static void main(String[] args) {
         Assistant assistant = getAssistant("documents/miles-of-smiles-terms-of-use.txt");
         Utils.starConversationWithAssistant(assistant);
    }

    private static Assistant getAssistant(String documentPath){
        ChatLanguageModel chatLanguageModel = OpenAiChatModel.builder()
                .apiKey(OPENAI_KEY)
                .modelName("gpt-3.5-turbo")
                .build();

        DocumentParser documentParser =new TextDocumentParser();
        Document document = loadDocument(Utils.toPath(documentPath),documentParser);

        DocumentSplitter splitter = DocumentSplitters.recursive(300,0);
        List<TextSegment> segments = splitter.split(document);

        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        List<Embedding> embedding = embeddingModel.embedAll(segments).content();

        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embedding,segments);

        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2)
                .minScore(0.5)
                .build();

        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        return AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .contentRetriever(contentRetriever)
                .chatMemory(chatMemory)
                .build();
    }

}
