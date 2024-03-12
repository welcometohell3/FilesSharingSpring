package org.example.filessharingsystemspring.controller;


import jakarta.servlet.http.HttpSession;
import org.example.filessharingsystemspring.entity.Files;
import org.example.filessharingsystemspring.entity.Users;
import org.example.filessharingsystemspring.repository.UsersRepository;
import org.example.filessharingsystemspring.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@Controller
@RequestMapping("/files")
public class FilesController {
    @Autowired
    private FileService filesService;

    @Autowired
    private UsersRepository usersRepository;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, HttpSession session)
            throws IOException, SQLException {
        String userName = (String) session.getAttribute("userName");
        Users user = usersRepository.findByName(userName);
        if(!file.isEmpty()){
            filesService.uploadFile(file, user);
        }
        return "redirect:/catalog";
    }


    @PostMapping("/share")
    public String handleFileShare(@RequestParam(name = "recipient") String recipient, @RequestParam(name = "selectedFile") Long selectedFile) {
        Users user = usersRepository.findByName(recipient);
        Files file = filesService.getFileById(selectedFile);
        filesService.shareFile(file, user);
        return "redirect:/share";
    }



    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) throws SQLException {
        Files file = filesService.getFileById(fileId);
        InputStreamResource resource = new InputStreamResource(file.getFileData().getBinaryStream());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentLength(file.getFileData().length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable Long fileId) {
        filesService.deleteFile(fileId);
        return "redirect:/catalog";
    }

}
