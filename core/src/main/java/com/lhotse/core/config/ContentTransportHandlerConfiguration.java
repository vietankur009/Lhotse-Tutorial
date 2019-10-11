package com.lhotse.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The Interface ContentTransportHandlerConfiguration.
 */
@ObjectClassDefinition(name = "Coveo Content Transport Handler Configuration", description = "Configuration will be used for Coveo Content Generation Transport Handler.")
public @interface ContentTransportHandlerConfiguration {

    @AttributeDefinition(name = "Protocol", description = "Protocol on which transport handler will work.", required = true)
    String getProtocol() default "content-parse-";

    @AttributeDefinition(name = "Allowed Paths", description = "Provide the list of allowed paths, on which Transport Handler will work")
    String[] getAllowedPaths() default {"/content/geometrixx","/content/dam/geometrixx"};

    @AttributeDefinition(name = "Allowed Paths", description = "Provide the list of allowed paths, on which Transport Handler will work")
    String[] getMetadataProperties() default {"jcr:title","pageTitle","jcr:created","jcr:createdBy"};
}