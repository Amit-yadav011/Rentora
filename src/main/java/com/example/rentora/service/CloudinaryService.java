package com.example.rentora.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.rentora.entity.Property;
import com.example.rentora.entity.User;
import com.example.rentora.exception.ResourceNotFoundException;
import com.example.rentora.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;


    public String uploadImage(MultipartFile file) {
        try {

            Map<String, Object> uploadResult =
                    cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {

            e.printStackTrace();
            throw new ResourceNotFoundException("Image upload failed");
        }
    }
}
