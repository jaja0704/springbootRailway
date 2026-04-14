package org.example;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sights")
public class Sight {

    @Id
    private String id;
    private String sightName;
    private String zone;
    private String category;
    private String photoURL;
    private String address;
    private String description;


    public String getId(){
        return id;
    }
    public void setId(String temp){
        id = temp;
    }
    public String getSightName(){
        return sightName;
    }
    public void setSightName(String temp){
        sightName = temp;
    }
    public String getZone(){
        return zone;
    }
    public void setZone(String temp){
        zone = temp;
    }
    public String getCategory(){
        return category;
    }
    public void setCategory(String temp){
        category = temp;
    }
    public String getPhotoURL(){
        return photoURL;
    }
    public void setPhotoURL(String temp){
        photoURL = temp;
    }
    public String getAddress(){
        return address;
    }
    public void setAddress(String temp){
        address = temp;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String temp){
        description = temp;
    }



}
