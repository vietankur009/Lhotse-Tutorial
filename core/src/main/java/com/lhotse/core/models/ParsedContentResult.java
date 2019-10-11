package com.lhotse.core.models;

import org.apache.sling.commons.json.JSONObject;

/**
 * This class will be used as a model class for providing all the required details of activated resource.
 */
public class ParsedContentResult {

    private String resourceType;

    private String resourcePath;

    private String pageHTML;

    private JSONObject metadataJSON;

    /**
     * returns resource type as string
     *
     * @return resource type as string
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * returns page/Asset page
     *
     * @return page/Asset path as string
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * returns page HTML
     *
     * @return page HTML as string
     */
    public String getPageHTML() {
        return pageHTML;
    }

    /**
     * Empty default constructor
     */
    public ParsedContentResult() {/*Empty Constructor*/}

    /**
     * Returns JSON object for metadata json.
     */
    public JSONObject getMetadataJSON() {
        return metadataJSON;
    }

    /**
     * Parameterized Constructor
     *
     * @param resourceType page/asset resource type
     * @param resourcePath page/asset resource path
     * @param pageHTML     if it is a page then page html
     */
    public ParsedContentResult(String resourceType, String resourcePath, String pageHTML, JSONObject metadataJSON) {

        this.resourceType = resourceType;
        this.resourcePath = resourcePath;
        this.pageHTML = pageHTML;
        this.metadataJSON = metadataJSON;
    }

    /**
     * Overridden toString method
     *
     * @return string value.
     */
    @Override
    public String toString() {
        return "resourceType : " + resourceType +
                ", resourcePath : " + resourcePath +
                ", Page HTML : " + pageHTML +
                ", Metadata JSON : " + metadataJSON;

    }
}
