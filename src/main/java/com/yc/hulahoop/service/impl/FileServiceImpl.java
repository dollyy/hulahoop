package com.yc.hulahoop.service.impl;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.service.FileService;
import com.yc.hulahoop.util.FTPUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service("fileService")
public class FileServiceImpl implements FileService{

    @Override
    public String upload(MultipartFile file,String imgType) {
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID() + "." + fileExtensionName;
        try {
            FTPUtil.uploadFile(uploadFileName,file.getInputStream(),imgType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadFileName;
    }
}
