package com.lhotse.core.handler;

import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.replication.*;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.WCMMode;
import com.lhotse.core.config.ContentTransportHandlerConfiguration;

import com.lhotse.core.models.ParsedContentResult;
import org.apache.commons.lang.StringUtils;

import org.apache.http.HttpStatus;
import org.apache.sling.api.resource.*;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.engine.SlingRequestProcessor;
import org.apache.sling.settings.SlingSettingsService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

/**
 * This class will be used a custom transport handler for connecting with Coveo.
 */
@Component(service = TransportHandler.class, immediate = true)
@Designate(ocd = ContentTransportHandlerConfiguration.class)
public class ContentTransportHandler implements TransportHandler {

    private final Logger log = LoggerFactory.getLogger(ContentTransportHandler.class);

    private String protocol;

    private String[] allowedPaths;

    private static final String SUB_SERVICE = "coveo-service";

    private String resourceURL;

    private JSONObject metadataJSON;

    private String resourcePrimaryType;

    private String[] configuredMetadataProperties;

    private ReplicationActionType replicationType;

    private boolean isAuthorEnvironment = false;

    @Reference
    private SlingSettingsService slingSettingsService;

    @Reference
    private RequestResponseFactory requestResponseFactory;

    @Reference
    private SlingRequestProcessor slingRequestProcessor;

    @Reference
    private ResourceResolverFactory resolverFactory;

    /**
     * Activate/ Modified.
     *
     * @param configuration the Coveo Transport Handler Configuration.
     */
    @Modified
    protected void activate(ContentTransportHandlerConfiguration configuration) {

        log.debug("Start of activate Method");
        protocol = configuration.getProtocol();
        allowedPaths = configuration.getAllowedPaths();
        configuredMetadataProperties = configuration.getMetadataProperties();
        isAuthorEnvironment = slingSettingsService.getRunModes().contains("author");
        log.debug("End of activate Method, Configured Protocol is {}", protocol);
    }

    /**
     * @param agentConfig Agent Config Object.
     * @return boolean value.
     */
    @Override
    public boolean canHandle(AgentConfig agentConfig) {

        log.debug("Start of canHandle Method");
        String transportURI = agentConfig.getTransportURI();
        log.debug("Configured Transport URL is {}", transportURI);
        return (isAuthorEnvironment && StringUtils.isNotBlank(transportURI) && transportURI.startsWith(protocol));
    }

    /**
     * @param transportContext       Transport Context Object
     * @param replicationTransaction Replication Transaction Object.
     * @return Replication Result Object.
     * @throws ReplicationException exception.
     */
    @Override
    public ReplicationResult deliver(TransportContext transportContext, ReplicationTransaction replicationTransaction) throws ReplicationException {

        log.debug("Inside deliver Method");
        ReplicationAction action = replicationTransaction.getAction();
        final ReplicationLog replicationLog = replicationTransaction.getLog();
        replicationType = action.getType();
        if (replicationType == ReplicationActionType.TEST) {
            return ReplicationResult.OK;
        }
        resourceURL = action.getPath();

        for (String x : allowedPaths) {
            replicationLog.info("allowed paths are : " + x);
        }
        if (!isValidPagePath()) {
            replicationLog.info("Agent Will work only for pages. It will not work for DAM Assets or /etc configs.");
            return ReplicationResult.OK;
        }
        updateResourceURL();
        return performOperation(replicationTransaction);
    }

    /**
     * this method is used to update the resource URL with .html if it is a page.
     */
    private void updateResourceURL() {

        try (ResourceResolver serviceResourceResolver = resolverFactory.getServiceResourceResolver(Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, SUB_SERVICE))) {
            Resource resource = serviceResourceResolver.getResource(resourceURL);
            if (resource == null) {
                return;
            }
            Page page = resource.adaptTo(Page.class);
            if (page != null) {
                log.debug("Page URL {}", page.getPath());
                resourceURL += ".html";
                log.debug("Resource URL {}", resourceURL);
                setJcrContentMetadataAsJSON(page);
            }
            resourcePrimaryType = resource.getResourceType();
        } catch (LoginException re) {
            log.error("Login Exception {}", re.getMessage());
        }
    }

    /**
     * This method is used to get the JSON values from page jcr content node.
     * @param page page object.
     */
    private void setJcrContentMetadataAsJSON(Page page) {

        metadataJSON = new JSONObject();
        if (page == null) {
            return;
        }
        Resource jcrContentResource = page.getContentResource();
        ValueMap properties = jcrContentResource.adaptTo(ValueMap.class);
        if (properties == null) {
            return;
        }
        for (String property : configuredMetadataProperties) {
            String value = properties.get(property, StringUtils.EMPTY);
            if (StringUtils.isNotBlank(value)) {
                try {
                    metadataJSON.put(property, value);
                } catch (JSONException je) {
                    log.error(je.getMessage());
                }
            }
        }
        log.debug("Metadata JSON : {}", metadataJSON);
    }

    /**
     * Check whether the replicated page/Asset url is valid for the replication agent.
     *
     * @return boolean value;
     */
    private boolean isValidPagePath() {

        boolean result = false;
        for (String allowedPath : allowedPaths) {
            if (resourceURL.startsWith(allowedPath)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * @param replicationTransaction Replication Transaction Object.
     * @return ReplicationResult OK if 200 response sent.
     * @throws ReplicationException exception.
     */
    private ReplicationResult performOperation(ReplicationTransaction replicationTransaction) throws ReplicationException {

        log.debug("Inside performOperation Method");
        final ReplicationLog replicationLog = replicationTransaction.getLog();
        replicationLog.info("Page URL : " + resourceURL);
        replicationLog.info("Action Type : " + replicationType);
        try {
            String html = getHTMLForRequestedPage(replicationLog);
            String replicatedResourcePath = resourceURL.replace(".html", StringUtils.EMPTY);
            ParsedContentResult parsedContentResult = new ParsedContentResult(resourcePrimaryType, replicatedResourcePath, html, metadataJSON);
            pageHTMLGenerationSuccessLogMessages(replicationLog, parsedContentResult);
            return ReplicationResult.OK;
        } catch (ReplicationException re) {
            return new ReplicationResult(false, HttpStatus.SC_INTERNAL_SERVER_ERROR, "Replication Failed");
        }
    }

    /**
     * This method will be used to get the HTML of a request page.
     *
     * @param replicationLog replication log object.
     * @return String value of HTML.
     */
    private String getHTMLForRequestedPage(ReplicationLog replicationLog) throws ReplicationException {

        HttpServletRequest req = requestResponseFactory.createRequest("GET", resourceURL);
        WCMMode.DISABLED.toRequest(req);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpServletResponse resp = requestResponseFactory.createResponse(out);
        try (ResourceResolver serviceResourceResolver = resolverFactory.getServiceResourceResolver(Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, SUB_SERVICE))) {
            slingRequestProcessor.processRequest(req, resp, serviceResourceResolver);
        } catch (IOException | LoginException | ServletException e) {
            replicationLog.error("Exception occurred  : {}", e.getMessage());
            throw new ReplicationException(e.getMessage());
        }
        return out.toString();
    }

    /**
     * method will be used to generate success log message after generating html of the page successfully.
     *
     * @param replicationLog      replication log object.
     * @param parsedContentResult ParsedContentResult object.
     */
    private void pageHTMLGenerationSuccessLogMessages(ReplicationLog replicationLog, ParsedContentResult parsedContentResult) {

        replicationLog.info("Resource Type : " + parsedContentResult.getResourceType());
        replicationLog.info("Resource URL : " + parsedContentResult.getResourcePath());
        replicationLog.info("Page HTML : " + parsedContentResult.getPageHTML());
        replicationLog.info("Metadata : " + parsedContentResult.getMetadataJSON());
        replicationLog.info("Status : Page HTML Generation Successful.");
        replicationLog.info("Status Code: 200 OK");
    }
}