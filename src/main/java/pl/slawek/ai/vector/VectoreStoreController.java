package pl.slawek.ai.vector;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ai")
public class VectoreStoreController {

    private final VectoreStoreService vectoreStoreService;

    public VectoreStoreController(VectoreStoreService vectoreStoreService) {
        this.vectoreStoreService = vectoreStoreService;
    }

    @GetMapping("add-data")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addData() {
        vectoreStoreService.loadDokumentsToVectorStore();
    }

    @GetMapping("get-data")
    String loadDataFromVectoreStore() {
        return vectoreStoreService.testVectorDb();
    }
}
