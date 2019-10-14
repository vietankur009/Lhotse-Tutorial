package com.lhotse.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ChildList {

    private static final Logger log = LoggerFactory.getLogger(TutorialModel.class);

    @Self
    private Resource resource;

    private String text;

    private String subtitle;

    private String sling_resourceType;

    private List<String> children;

    @PostConstruct
    public void init() {
        log.debug("Start of init Method");

        children = new ArrayList<>();

        ValueMap prop = resource.adaptTo(ValueMap.class);

        if (prop == null) {
            return;
        }

        text = prop.get("text", "I don't want text");
        subtitle = prop.get("subtitle", "I don't want subtitle");
        sling_resourceType = prop.get("sling_resourceType", "I don't want sling");

        Iterator<Resource> resoItr= resource.listChildren();
        while(resoItr.hasNext()){
            Resource resoItem = resoItr.next();
            log.debug("resoItem {}",resoItem);
            ValueMap propResoItem = resoItem.adaptTo(ValueMap.class);
            if(propResoItem==null){
                continue;
            }
            String nameOf = propResoItem.get("nameOf","@!!!");
            children.add(nameOf);
            log.debug("children {}",children);
        }

        log.debug("End of init Method");
    }

    public String getText() {
        return text;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getSling_resourceType() {
        return sling_resourceType;
    }

    public List<String> getChildren() {
        return children;
    }

}
