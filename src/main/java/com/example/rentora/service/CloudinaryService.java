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

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserService userService;

    public String uploadImage(MultipartFile file, Long propertyId) {
        try{

            User user= userService.getCurrentUser();

            Property prop=propertyRepository.findById(propertyId).orElseThrow(()->new ResourceNotFoundException("Property not found"));

            //only owner can set the images of their property no one else can do that!
            if(!prop.getOwner().getId().equals(user.getId())){
                throw new ResourceNotFoundException("You are not allowed to upload image for this property");
            }

            System.out.println("user id"+ user.getId());
            System.out.println("property id"+ prop.getId());

            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String imageUrl= uploadResult.get("secure_url").toString();

            prop.setImageUrl(imageUrl);

            propertyRepository.save(prop);

            return imageUrl;
        }
        catch (Exception e){
               e.printStackTrace();
               throw new ResourceNotFoundException("Image upload failed");
        }
    }
}
