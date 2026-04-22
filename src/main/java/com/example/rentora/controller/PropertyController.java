package com.example.rentora.controller;

import com.example.rentora.dto.PropertyRequestdto;
import com.example.rentora.dto.PropertyResponsedto;
import com.example.rentora.entity.Property;
import com.example.rentora.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/property")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping("/add")
    public PropertyResponsedto CreateProperty(@RequestBody PropertyRequestdto dto){
        return propertyService.save(dto);
    }

    //bulk saving
    @PostMapping("/bulk")
    public List<PropertyResponsedto> saveAll(@RequestBody List<PropertyRequestdto> dto){
        return propertyService.saveAll(dto);
    }

    @GetMapping
    public List<Property> findAll(){
        return propertyService.getAllProperty();
    }

    @GetMapping("/{id}")
    public Property findById(@PathVariable Long id){
        return propertyService.findById(id);
    }

    @GetMapping("/{location}")
    public List<Property> findByLocation(@PathVariable String location){
        return propertyService.getPropertyByLocation(location);
    }

    @GetMapping("/search")
    public Page<Property> searchProperties( @RequestParam(required = false) String location,
                                            @RequestParam(required = false) Double minPrice,
                                            @RequestParam(required = false) Double maxPrice,
                                            @RequestParam(defaultValue = "0")int page,
                                            @RequestParam(defaultValue = "3") int size)
    {

      return propertyService.searchProperties(location, minPrice, maxPrice, page, size);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id){
        propertyService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Property updateProperty(@RequestBody Property property, @PathVariable Long id){
        return propertyService.updateProperty(property,id);
    }
}
