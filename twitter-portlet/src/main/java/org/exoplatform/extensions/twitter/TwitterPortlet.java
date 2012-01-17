/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *
 */
package org.exoplatform.extensions.twitter;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.extensions.social.network.model.UserSocialNetworkPreferences;
import org.exoplatform.extensions.twitter.services.TwitterService;

import org.exoplatform.social.webui.Utils;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 *
 * @author tgrall
 */
public class TwitterPortlet extends GenericPortlet {

    private final static String JSP_VIEW_OWNER = "/WEB-INF/jsp/TwitterPortlet/view-as-owner.jsp";
    private final static String JSP_VIEW_VIEWER = "/WEB-INF/jsp/TwitterPortlet/view-as-simpleviewer.jsp";
    private final static String SESSION_OAUTH_TOKEN = "twitterAuthToken";

    @Override
    protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        String pageToShow = JSP_VIEW_VIEWER;
        
        if (Utils.isOwner()) {
            pageToShow = JSP_VIEW_OWNER;
            String user = request.getRemoteUser();
            TwitterService twitterService = (TwitterService) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(TwitterService.class);
            UserSocialNetworkPreferences preferences = twitterService.getUserPreferences(user);
            request.setAttribute("isTwitterConfigured", (preferences != null));
            if (preferences == null) {
                    RequestToken requestToken;
                    requestToken = twitterService.getAuthorizationUrl();
                    request.getPortletSession().setAttribute(SESSION_OAUTH_TOKEN, requestToken);
                    request.setAttribute(SESSION_OAUTH_TOKEN, requestToken);

            } else {
                
            }

        } else {
        }
        PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(pageToShow);
        dispatcher.include(request, response);
    }

    @Override
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        String action = request.getParameter("action");
        if (action != null && action.equals("save")) {
            String pin =  request.getParameter("pin");
            // get the object from the session
            RequestToken requestToken = (RequestToken)request.getPortletSession().getAttribute(SESSION_OAUTH_TOKEN);
            TwitterService twitterService = (TwitterService) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(TwitterService.class);
            AccessToken accessToken = null;
            try {
                accessToken = twitterService.getTwitter().getOAuthAccessToken(requestToken, pin);
            } catch (TwitterException ex) {
                Logger.getLogger(TwitterPortlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            twitterService.saveUserPreferences( request.getRemoteUser() , accessToken.getScreenName(), accessToken.getToken(), accessToken.getTokenSecret() );
        } else if (action != null && action.equals("revoke")) {
            TwitterService twitterService = (TwitterService) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(TwitterService.class);
            twitterService.removeUserPreferences( request.getRemoteUser()  );
        }

    }

}
