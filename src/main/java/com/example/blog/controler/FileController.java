package com.example.blog.controler;


        import org.springframework.core.io.PathResource;
        import org.springframework.core.io.Resource;
        import org.springframework.http.HttpHeaders;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.PathVariable;
        import com.example.blog.service.FileStorageService;

        import java.io.IOException;
        import java.net.URI;
        import java.net.URISyntaxException;

@Controller
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException, URISyntaxException {
        PathResource fileResource = new PathResource(fileStorageService.loadFileAsResource(filename));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                .body(fileResource);
    }
}

