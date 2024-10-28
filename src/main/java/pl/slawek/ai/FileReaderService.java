package pl.slawek.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FileReaderService {

    private final ChatClient chatClient;

    public FileReaderService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    String getResponseFromDocx() {
        TikaDocumentReader reader = new TikaDocumentReader("classpath:Plik.doc");
        String userPropmpt = "Co jest w pliku?";

        Message userMessage = new UserMessage(userPropmpt);

        String collect = reader.get().stream()
                .map(Document::getContent)
                .collect(Collectors.joining());

        Message systemPromptTemplate = new SystemPromptTemplate(collect).createMessage();

        Prompt prompt = new Prompt(List.of(userMessage, systemPromptTemplate));

        return chatClient.prompt(prompt).call().content();
    }

    String getResponseFromPdf() {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader("classpath:/file2.pdf",
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build());

        String userPropmpt = "Co jest w pliku?";

        Message userMessage = new UserMessage(userPropmpt);

        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        List<Document> dokuments = tokenTextSplitter.apply(pdfReader.get());

        String systemPrompt = "Dokumentacja: {dokuments}";

        Message systemPromptTemplate = new SystemPromptTemplate(systemPrompt)
                .createMessage(Map.of("dokuments", dokuments));

        Prompt prompt = new Prompt(List.of(userMessage, systemPromptTemplate));

        return chatClient.prompt(prompt).call().content();
    }
}
