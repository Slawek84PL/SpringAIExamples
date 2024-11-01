package pl.slawek.ai.vector;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VectoreStoreService {

    private final ChatClient chatClient;
    private final VectorStore simpleVectorStore;

    public VectoreStoreService(ChatClient.Builder builder, VectorStore simpleVectorStore) {
        this.chatClient = builder.build();
        this.simpleVectorStore = simpleVectorStore;
    }

    String testVectorDb() {


        String userPropmpt = "Co jest w pliku?";

        Message userMessage = new UserMessage(userPropmpt);

        String systemPrompt = "Dokumentacja: {dokuments}";

        List<Document> similarDocuments = simpleVectorStore.similaritySearch(
                SearchRequest.query(userPropmpt)
                        .withTopK(8));

        Message systemPromptTemplate = new SystemPromptTemplate(systemPrompt)
                .createMessage(Map.of("dokuments", similarDocuments));

        Prompt prompt = new Prompt(List.of(userMessage, systemPromptTemplate));

        return chatClient.prompt(prompt).call().content();
    }

    void loadDokumentsToVectorStore() {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader("classpath:file2.pdf");
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        List<Document> dokuments = tokenTextSplitter.apply(pdfReader.get());

        simpleVectorStore.add(dokuments);
    }

}
