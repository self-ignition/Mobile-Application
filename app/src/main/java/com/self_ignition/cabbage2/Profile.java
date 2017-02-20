package com.self_ignition.cabbage2;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by r-bur on 20/02/2017.
 */

public class Profile {
    private String username = "";
    private String email = "";
    private Bitmap profileImage = null;
    private List<String> allergens = new ArrayList<>();

    //GETTERS
    public Bitmap getProfileImage() {
        return profileImage;
    }
    public List<String> getAllergens() {
        return allergens;
    }
    public String getEmail() {
        return email;
    }
    public String getUsername() {
        return username;
    }

    //SETTERS
    public void setAllergens(List<String> allergens) {
        this.allergens = allergens;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void LoadProfile(){
        //// TODO: 20/02/2017 load profile from local storage
    }

    public void SaveProfile(){
        //// TODO: 20/02/2017 save profile to local storage
    }
}
