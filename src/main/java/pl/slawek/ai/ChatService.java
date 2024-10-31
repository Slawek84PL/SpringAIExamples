package pl.slawek.ai;

import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    String getJoke() {
        return chatClient.prompt("Powiedz żart o programistach").call().content();
    }

    String getDisasterString() {
        return chatClient.prompt("Opisz jakąś katastrofę w 20 słowach").call().content();
    }

    String getNobelWinner() {
        OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
                .withTemperature(0.8)
                .withModel(OpenAiApi.ChatModel.GPT_4_O_MINI)
                .build();

        String prompString = """
                Napisz nazwiska pięciu polskich noblistwów w kolejniści chronilogicznej,
                wraz z rokime przynania nagrody oraz za co zostala przyznana
                """;

        Prompt prompt = new Prompt(prompString, chatOptions);

        return chatClient.prompt(prompt).call().content();
    }


    StoryDto getStory() {

        OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
                .withTemperature(1.0)
                .withModel(OpenAiApi.ChatModel.GPT_4_TURBO)
                .build();

        String promptString = """
            Napisz po polsku historię z gatunku {gatunek} na temat: {temat}
            """;

        return chatClient.prompt()
                .user(u -> u.text(promptString)
                        .param("gatunek", "Komedia")
                        .param("temat", "praca"))
                .options(chatOptions)
                .call()
                .entity(StoryDto.class);
    }

    DisasterAddDTO getDisasterRecord() {
        String promptString = "Podaj dane jakiejś katastrofy";

        return chatClient.prompt(promptString)
                .call()
                .entity(DisasterAddDTO.class);
    }
}
