package org.example.filessharingsystemspring.service;


import org.example.filessharingsystemspring.entity.Files;
import org.example.filessharingsystemspring.entity.Users;
import org.example.filessharingsystemspring.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private FilesRepository filesRepository;

    public void uploadFile(MultipartFile file, Users user) throws IOException, SQLException {
        Files file1 = new Files();
        file1.setName(file.getOriginalFilename());
        Blob blob = new SerialBlob(file.getBytes());
        file1.setFileData(blob);
        file1.addUser(user);
        filesRepository.save(file1);
    }

    public void shareFile(Files file, Users user) {
        if (!file.getUsers().contains(user)) {
            file.addUser(user);
            filesRepository.save(file);
        }
    }
    public List<Files> getFilesByUserId(Long userId) {
        return filesRepository.findByUsersId(userId);
    }

    public Files getFileById(Long fileId) {
        return filesRepository.findById(fileId).orElse(null);
    }

    public void deleteFile(Long id){
        filesRepository.deleteById(id);
    }
}
