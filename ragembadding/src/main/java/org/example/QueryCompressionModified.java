package org.example;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.bge.small.en.v15.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.example.common.Assistant;
import org.example.common.Utils;
import org.example.custom.build.*;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

public class QueryCompressionModified {

    static String  OPENAI_KEY ="sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    public static void main(String[] args) {
        Assistant assistant = getAssistant("documents/biography-of-john-doe.txt");
        Utils.starConversationWithAssistant(assistant);
    }


    private static Assistant getAssistant(String docPath){

        Document document = loadDocument(Utils.toPath(docPath), new TextDocumentParser());
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor ingester = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300,0))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        ingester.ingest(document);

//        ChatLanguageModel chatLanguageModel = OpenAiChatModel.builder()
//                .apiKey(OPENAI_KEY)
//                .build();
//        You can run any model from https://ollama.ai/library by following these steps:
//        Run "docker run -d -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama"
//        Run "docker exec -it ollama ollama run mistral" <- specify the desired model here



        ChatLanguageModel chatLanguageModel = CustomBuilder.buildOllamaChatModel();

        QueryTransformer queryTransformer = new CompressingQueryTransformer(chatLanguageModel);

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(1)
                .minScore(0.6)
                .build();

        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .queryTransformer(queryTransformer)
                .contentRetriever(contentRetriever)
                .build();


        return AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();


    }


}
