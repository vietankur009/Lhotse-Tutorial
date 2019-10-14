package com.lhotse.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SpecialMarks {
    @Self
    private Resource resource;



    private String firstName;

    private String middleName;

    private String lastName;

    private String linkURL;

    @PostConstruct

    public void init(){
        if (resource == null) {
            return;
        }

        ValueMap properties = resource.adaptTo(ValueMap.class);
        if (properties == null) {
            return;
        }
        firstName = properties.get("firstName", "Default Name");
        middleName = properties.get("middleName" , "Default Middle Name");
        lastName = properties.get("lastName" , "Default Last Name");
        linkURL = properties.get("linkURL" , "Default Link URL");



        if (linkURL == null) {
            return;
        }

        if(linkURL.startsWith("/content")){
            if(linkURL.contains("?")){
                String[] items = linkURL.split(" ?");
                String finalValue = " ";

                for(int i = 1; i < items.length; i = i + 1){
                    finalValue = finalValue + items[i];
                }
                linkURL = items[0] + ".html ?"+ finalValue;
            }else {
                linkURL = linkURL+".html";
            }
        }



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
