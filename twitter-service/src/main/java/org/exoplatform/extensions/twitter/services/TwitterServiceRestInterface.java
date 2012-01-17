package org.exoplatform.extensions.twitter.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.rest.resource.ResourceContainer;
import twitter4j.auth.RequestToken;


@Path("/twitter-service")
public class TwitterServiceRestInterface implements ResourceContainer {
    
    
    @Path("/auth-url")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public RequestToken getAuthorizationUrl() {
        return this.getTwitterService().getAuthorizationUrl();
    }
    
    @Path("/validate-auth")
    @GET
    public boolean validateTwitterAuthorization(@QueryParam ("requestToken") String requestToken, @QueryParam ("pin") String pin) {
        return this.getTwitterService().validateTwitterAuthorization(requestToken,pin);
    }
    
    private TwitterService getTwitterService() {
        return (TwitterService)ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(TwitterService.class);

    }
    
}
