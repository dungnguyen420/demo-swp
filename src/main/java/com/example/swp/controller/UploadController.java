package com.example.swp.controller;

import com.example.swp.Service.impl.CloudinaryService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/uploadFile")
public class UploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    public String showUploadForm(){
        return "upload_form";
    }

    @PostMapping
    public String uploadFile(@NotNull @RequestParam("file")MultipartFile file, Model model){
        try{
            String imageUrl = cloudinaryService.uploadImage(file, "uploads");
            model.addAttribute("message","Upload successful");
            model.addAttribute("imageUrl",imageUrl);
        }catch (Exception e){
            model.addAttribute("message","Upload fail");
        }
        return "upload_resuld";
    }
}
