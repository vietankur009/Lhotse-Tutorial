package com.lhotse.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables=Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TutorialModel {

    @Self
    private Resource resource;

    @Inject
    private String title;

    @Inject
    private String subTitle;

    private String resName;

    private String resPath;

    @PostConstruct
    public void init() {

        resName = resource.getName();
        resPath = resource.getPath();
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getResName() {
        return resName;
    }

    public String getResPath() {
        return resPath;
    }
}
