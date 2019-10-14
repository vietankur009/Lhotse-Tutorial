package com.lhotse.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageComp {

   /* @Self
    private  Resource resource;

    private String resName;

    private String resPath;*/

    @Inject
    private String title;

    @Inject
    private String fileReference;

    @Inject
    private String subtitle;

    @Inject
    private String description;

    @Inject
    private String url;

    @Inject
    private String label;

    @PostConstruct
    public void init(){
        /*resName = resource.getName();
        resPath = resource.getPath();*/

        if (url == null) {
            return;
        }
        if (url.startsWith("/content") && (!url.contains("/dam/"))) {
            if (url.contains("?")) {
                url = url.replace("?", ".html?");
            } else {
                url = url + ".html";
            }
        }
    }

   /* public Resource getResource() {
        return resource;
    }

    public String getResName() {
        return resName;
    }

    public String getResPath() {
        return resPath;
    }*/

    public String getTitle() {
        return title;
    }

    public String getFileReference() {
        return fileReference;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getLabel() {
        return label;
    }
}
