package pl.slawek.ai;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ai")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("joke")
    String getJoke() {
        return chatService.getJoke();
    }

    @GetMapping("katastrofa")
    String getDisasterString() {
        return chatService.getDisasterString();
    }

    @GetMapping("nobel")
    String getNobelWinner() {
        return chatService.getNobelWinner();
    }

    @GetMapping("story")
    StoryDto getStory() {
        return chatService.getStory();
    }

    @GetMapping("disaster")
    DisasterAddDTO getDisaster() {
        return chatService.getDisasterRecord();
    }
}
