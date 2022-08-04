package com.wes.goddard.controller;

import com.wes.goddard.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * file upload and download
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${goddard.path}")
    private String basePath;

    /**
     * File upload
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        /**
         * file is a temporary file, and it needs to be transfered to a certain location.
         * Otherwise, it will be deleted after this request
         */
        log.info(file.toString());

        // Original file name
        String originalFilename = file.getOriginalFilename();//abc.jpg
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        // Use UUID to generate a new file name, so we can ensure that the file name will not be duplicated.
        String fileName = UUID.randomUUID().toString() + suffix;//dfsdfdfd.jpg

        // Create a target directory
        File dir = new File(basePath);
        // Check if the directory already exists
        if(!dir.exists()){
            // Directory not existed
            dir.mkdirs();
        }

        try {
            // save the temp file to the directory
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * File download
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            // Input stream. Get the file content from the input stream
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            // Output stream. Write the file content to the web browser from this stream
            ServletOutputStream outputStream = response.getOutputStream();

            // Set responding data type to be image/jpeg
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            // Turn off resource
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
