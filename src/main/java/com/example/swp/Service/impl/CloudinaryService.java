package com.example.swp.Service.impl;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file, String folderName){
        if (file.isEmpty()){
            throw new RuntimeException("File is empty!");
        }
        try{
            Map result = cloudinary.uploader().upload(
              file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folderName
                    )
            );
            return result.get("secure_url").toString();
        }catch(Exception e){
            throw new RuntimeException("Upload failed:" + e.getMessage());
        }
    }
}
