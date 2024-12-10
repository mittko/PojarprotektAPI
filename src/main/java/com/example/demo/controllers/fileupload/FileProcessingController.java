package com.example.demo.controllers.fileupload;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class FileProcessingController {

    private static final String UPLOAD_DIR = "D:/app_version_uploaded";

    @PostMapping("/upload_app_version")
    public ResponseEntity<String> uploadLargeFile(HttpServletRequest request) throws IOException {
        String fileName = request.getHeader("X-Filename");
        if (fileName == null || fileName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File name is missing in headers");
        }

        File file = new File(fileName);
        if(!file.exists()) {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        }
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        try (InputStream inputStream = request.getInputStream()) {

            OutputStream out = new FileOutputStream(filePath.toFile());

            byte[] buffer = new byte[10 * 1024 * 1024]; // 8KB buffer size
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            out.close();
            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }

    // method not to download the file to the server and then send it to the client but streaming it to the client directly
    @GetMapping("/download_app_version")
    public ResponseEntity<StreamingResponseBody> downloadStreamFile(@RequestParam(value = "fileName") String fileName) {
        String filePath  = "D:\\";
        final File file =  new File(filePath + File.separator+fileName);
        StreamingResponseBody stream = outputStream -> {
            byte[] b = new byte[1024 * 2];
            try(InputStream inputStream = new FileInputStream(file)) {
                int r;
                while ((r = inputStream.read(b)) != -1) {
                    outputStream.write(b,0, r);
                }
                outputStream.flush(); // Ensure all data is sent
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(file.length());
        headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("file.zip").build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(stream);
    }
    @GetMapping(path = "/download")
    public ResponseEntity<?> downloadFile(@RequestParam(value = "fileName") String fileName) throws FileNotFoundException {
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

    @PostMapping(path = "/upload_file")
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String fileUploadStatus = "";
        String filePath = "D:\\" + multipartFile.getOriginalFilename();
        try(FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.flush();
            fileUploadStatus = "File Uploaded Successfully";
        } catch (Exception e) {
            fileUploadStatus = "Error in uploading file " + e.getMessage();
        }
        return fileUploadStatus;
    }
   /* @RequestMapping(value="/download_zip", produces="application/zip")
    public void zipFiles(HttpServletResponse response) throws IOException {
        File fileDir = new File("D:\\update\\PojarprotektHttpClient");
        //setting headers
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"test.zip\"");
        double len = getFileSize(fileDir);
        response.addHeader("contentLength",len+"");

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

        // create a list to add files to be zipped

        // package files
        zip(zipOutputStream,fileDir,"D:\\");

        zipOutputStream.close();
        zipOutputStream.closeEntry();

    }

    private void zip(ZipOutputStream zipOutputStream, File _file, String root) throws IOException {

        if(_file == null || _file.listFiles() == null) {
            return;
        }
        for (File file : Objects.requireNonNull(_file.listFiles())) {
           // System.out.println(file.getCanonicalPath());
            if(!file.isDirectory()) {
                //new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
                String name = file.getPath();
                name = name.replace(root,"");
                zipOutputStream.putNextEntry(new ZipEntry(name));
                FileInputStream fileInputStream = new FileInputStream(file);

                IOUtils.copy(fileInputStream, zipOutputStream);

                fileInputStream.close();

            } else {
                zip(zipOutputStream,file,root);
            }
        }
    }

    private double getFileSize(File file) {
        double res = 0;
        if(file.listFiles() == null) {
            return 0;
        }
        for(File file1 : file.listFiles()) {
            if(file1.isFile()) {
                res += file1.length();
            } else {
                res += getFileSize(file1);
            }
        }
        return res;
    }*/

}
