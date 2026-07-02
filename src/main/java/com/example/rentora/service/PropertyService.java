package com.example.rentora.service;

import com.example.rentora.dto.PropertyRequestdto;
import com.example.rentora.dto.PropertyResponsedto;
import com.example.rentora.entity.Property;
import com.example.rentora.entity.User;
import com.example.rentora.enums.Role;
import com.example.rentora.exception.ResourceNotFoundException;
import com.example.rentora.repository.PropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


@Service
public class PropertyService {

    private final Logger logger = LoggerFactory.getLogger(PropertyService.class);

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<Property> getAllProperty(){
        return propertyRepository.findAll();
    }

    public PropertyResponsedto save(PropertyRequestdto dto, MultipartFile image){

        User currentUser = userService.getCurrentUser();

        if(currentUser.getRole() != Role.LANDLORD) {
            throw new ResourceNotFoundException("Only Landlord can add property");
        }

        String imgUrl=cloudinaryService.uploadImage(image);

        //convert dto to entity
        Property property = new Property();
        property.setDescription(dto.getDescription());
        property.setLocation(dto.getLocation());
        property.setPrice(dto.getPrice());
        property.setTitle(dto.getTitle());
        property.setPropertyType(dto.getPropertyType());
        property.setAvailable(dto.getAvailability());
        property.setBaths(dto.getBaths());
        property.setBeds(dto.getBeds());
        property.setCity(dto.getCity());
        property.setSqft(dto.getSqft());

        property.setImgUrl(imgUrl);

        property.setNeighborhood(dto.getNeighborhood());

        property.setOwner(currentUser);

        //NOW TIME TO SAVE THE PROPERTY IN REPOSITORY
        Property saved=propertyRepository.save(property);

        //now again convert entity to dto
        PropertyResponsedto response=new PropertyResponsedto();
        response.setId(saved.getId());
        response.setDescription(saved.getDescription());
        response.setLocation(saved.getLocation());
        response.setPrice(saved.getPrice());
        response.setTitle(saved.getTitle());
        response.setPropertyType(saved.getPropertyType());
        response.setAvailability(saved.getAvailable());
        response.setBaths(saved.getBaths());
        response.setBeds(saved.getBeds());
        response.setCity(saved.getCity());
        response.setSqft(saved.getSqft());
        response.setImgUrl(saved.getImgUrl());

        return response;
    }

    //bulk saving
    public  List<PropertyResponsedto> saveAll(List<PropertyRequestdto> dtoList){
        User currentUser = userService.getCurrentUser();

        if(currentUser.getRole() != Role.LANDLORD){
            throw new ResourceNotFoundException("Only Landlords can add property");
        }

         //now dto to entity
        List<Property> properties= dtoList.stream().map(dto->{
            Property p=new Property();
            p.setDescription(dto.getDescription());
            p.setLocation(dto.getLocation());
            p.setPrice(dto.getPrice());
            p.setPropertyType(dto.getPropertyType());
            p.setOwner(currentUser);
            p.setTitle(dto.getTitle());
            p.setAvailable(dto.getAvailability());
            p.setBaths(dto.getBaths());
            p.setBeds(dto.getBeds());
            p.setCity(dto.getCity());
            p.setSqft(dto.getSqft());
            p.setNeighborhood(dto.getNeighborhood());


            return p;
        }).toList();

        List<Property> saved= propertyRepository.saveAll(properties);

        //now again entity to dto
        List<PropertyResponsedto> responseList=saved.stream().map(entity-> {
            PropertyResponsedto response=new PropertyResponsedto();
            response.setId(entity.getId());
            response.setDescription(entity.getDescription());
            response.setLocation(entity.getLocation());
            response.setPrice(entity.getPrice());
            response.setTitle(entity.getTitle());
            response.setPropertyType(entity.getPropertyType());
            response.setAvailability(entity.getAvailable());
            response.setCity(entity.getCity());
            response.setSqft(entity.getSqft());
            response.setBeds(entity.getBeds());
            response.setBaths(entity.getBaths());
            response.setNeighborhood(entity.getNeighborhood());
            response.setImgUrl(entity.getImgUrl());

            return response;
        }).toList();

        return responseList;
    }

    public List<Property> getPropertyByLocation(String location){
        try{
            List<Property> prop=propertyRepository.findByLocation(location);
            if(!prop.isEmpty()){
                return prop;
            }else{
                logger.error("Property not found");
                return null;
            }
        }catch(Exception e){
            logger.error("No Property Found at that Location {}",e.getMessage());
            return null;
        }
    }

    public Property findById(Long id){
        try{
            Optional<Property> prop=propertyRepository.findById(id);
            if(prop.isPresent()){
                logger.info("Property found");
                return prop.get();
            }else{
                logger.error("Property not fetched");
                return null;
            }
        }catch(Exception e){
            logger.error("Error=",e);
            return null;
        }
    }


    //pageable
    public Page<Property> searchProperties(String location, Double minPrice, Double maxPrice, int page, int size){
        Pageable pageable =  PageRequest.of(page, size, Sort.by("price").ascending());
        return propertyRepository.searchProperties(location, minPrice, maxPrice,pageable);
    }

    //deletion of an particular property
    public void delete(Long id){

        Property property= propertyRepository.findById(id).orElseThrow(() -> new RuntimeException("Property not found"));

        User user= userService.getCurrentUser();

        if(!property.getOwner().getId().equals(user.getId())){
            throw new ResourceNotFoundException("User is not the owner of the property");
        }

        propertyRepository.delete(property);
    }

    //update property
    public Property updateProperty(Property property, Long id){

        Property prop=propertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        User user=userService.getCurrentUser();

        if(!prop.getOwner().getId().equals(user.getId())){
            throw new AccessDeniedException("You are not the owner of this property");
        }


        prop.setPropertyType(property.getPropertyType());
        prop.setPrice(property.getPrice());
        prop.setAvailable(property.getAvailable());

        return propertyRepository.save(prop);
    }
}
