package com.lhotse.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TutorialModel {

    private static final Logger log = LoggerFactory.getLogger(TutorialModel.class);

    @Self
    private Resource resource;

    private String title;

    @Inject
    private String subTitle;

    private String resName;

    private String resPath;


    private List<String> children;

    @PostConstruct
    public void init() {

        log.debug("Start of init Method");
        children = new ArrayList<>();

        if (resource == null) {
            log.error("Resource object is null");
            return;
        }

        ValueMap properties = resource.adaptTo(ValueMap.class);
        if (properties == null) {
            log.error("Properties object is null");
            return;
        }

        title = properties.get("title", "Default Title");

        Iterator<Resource> resItr = resource.listChildren();
        while (resItr.hasNext()) {
            Resource item = resItr.next();
            ValueMap itemProperties = item.adaptTo(ValueMap.class);
            if (itemProperties == null) {
                continue;
            }
            String text = itemProperties.get("text", "");
            children.add(text);
        }

        log.debug("Children {}", children);

        resName = resource.getName();
        resPath = resource.getPath();
        log.debug("End of init Method");
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

    public List<String> getChildren() {
        return children;
    }
}
