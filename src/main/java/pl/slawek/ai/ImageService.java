package pl.slawek.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    private final ChatClient chatClient;
    private final OpenAiImageModel imageModel;


    public ImageService(ChatClient.Builder chatClient, OpenAiImageModel imageModel) {
        this.chatClient = chatClient.build();
        this.imageModel = imageModel;
    }

    String getPictureUrl() {
        ImageOptions imageOptions = OpenAiImageOptions.builder()
                .withQuality("hd")
                .withN(1)
                .withHeight(1024)
                .withWidth(1024)
                .withModel(OpenAiImageApi.ImageModel.DALL_E_3.getValue())
                .build();

        ImagePrompt imagePrompt = new ImagePrompt("Wykonaj zdjęcie programisty w ciemnym lesie w nowoczesnym stylu jako real foto", imageOptions);

        String response = imageModel.call(imagePrompt).getResult().getOutput().getUrl();

        return response;
    }

}
