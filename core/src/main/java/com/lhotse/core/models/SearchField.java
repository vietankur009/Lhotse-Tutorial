package com.lhotse.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import java.util.Properties;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchField {

    @Self
    private Resource resource;

    @Inject
    private String name;

    @Inject
    private String path;

    @PostConstruct
    public void init() {
        if (path == null) {
            return;
        }
        if (path.startsWith("/content") && (!path.contains("/dam/"))) {
            if (path.contains("?")) {
                path = path.replace("?", ".html?");
            } else {
                path = path + ".html";
            }
        }
    }

   /*@PostConstruct
    public void init(){
        ValueMap properties = resource.getValueMap();
        properties.get("path", "null");
        if(p!=null) {
            if (!p.startsWith("http://") || !p.startsWith("https://")) {
                path = path.concat(".html");
            }
        }
    }*/

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
