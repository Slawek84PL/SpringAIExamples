package pl.slawek.ai;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ai")
public class FileReaderController {

    private final FileReaderService fileReaderService;

    public FileReaderController(FileReaderService fileReaderService) {
        this.fileReaderService = fileReaderService;
    }

    @GetMapping("dok")
    String getDok() {
        return fileReaderService.getResponseFromDocx();
    }

    @GetMapping("pdf")
    String getPdf() {
        return fileReaderService.getResponseFromPdf();
    }
}
