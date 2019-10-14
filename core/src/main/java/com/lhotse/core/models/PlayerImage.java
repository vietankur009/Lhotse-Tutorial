package com.lhotse.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class PlayerImage {
    @Self
    private Resource resource;


    private String firstName;

    private String middleName;

    private String lastName;

    private String linkURL;



    private String fileReference;


    @PostConstruct
    public void init (){
        if (resource == null) {

            return;
        }

        ValueMap properties = resource.adaptTo(ValueMap.class);
        if (properties == null) {

            return;
        }
        fileReference = properties.get("fileReference", "Image");
        firstName = properties.get("firstName", "Default Name");
        middleName = properties.get("middleName" , "Default Middle Name");
        lastName = properties.get("lastName" , "Default Last Name");
        linkURL = properties.get("linkURL" , "Default Link URL");

    }

    public String getFileReference() {
        return fileReference;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLinkURL() {
        return linkURL;
    }

}
