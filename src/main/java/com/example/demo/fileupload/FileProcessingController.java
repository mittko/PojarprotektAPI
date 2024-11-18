package com.example.demo.fileupload;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
public class FileProcessingController {

    @GetMapping(path = "/download/{fileName:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable(value = "fileName") String fileName) throws FileNotFoundException {
       // Checking whether the file requested for download exists or not
      String filePath  = "D:\\";
        // Creating new file instance
      File file = new File(filePath + File.separator+fileName);
        // Creating a new InputStreamResource object
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        // Creating a new instance of HttpHeaders Object
        HttpHeaders httpHeaders = new HttpHeaders();
        // Setting up values for contentType and headerValue
        String contentType = "application/octet-stream";
        String headerValue = "attachment; file = \"" + resource.getFilename() + "\"";


        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,headerValue)
                .header("contentLength",file.length()+"")
                .body(resource);
    }
}
