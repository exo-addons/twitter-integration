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
package org.exoplatform.extensions.twitter.services;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.extensions.social.network.model.SocialNetworkIntegration;
import org.exoplatform.extensions.social.network.model.UserSocialNetworkPreferences;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 *
 * @author tgrall
 */
public class TwitterService {

    public final static String SOCIAL_APPLICATION_NAME = "twitter";
    // Default Application Token
    private String consumerKey = "e5A7bXNe0K5rWwr9GUSZIA";
    private String secretKey = "d7ru6sWIjMMT5jHWLDdp99lzTOWsCq92Ul4qOYX0tZY";

//437360518
//token : 437360518-XBVhsEcv5d0Lijqpcpmj50kB564VBUqJbA9ECr2k
//tokenSecret : V8nbttrPmWsaShye5Ubo9ZcwnS5i4p65GoZEBvfTI    
    public TwitterService() {
    }

    /**
     * Push a message to Twitter
     * @param userId 
     * @param message 
     */
    public void updateStatus(String userId, String message) {
        try {
            Twitter twitter = this.getTwitter(userId);
            if (twitter != null) {
                twitter.updateStatus(message);
            }
        } catch (Exception ex) {
            Logger.getLogger(TwitterService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Save the Twitter information (token, username, ..) in the user application data folder
     * @param exoUserId
     * @param socialUserName
     * @param token
     * @param tokenSecret 
     */
    public void saveUserPreferences(String exoUserId, String socialUserName, String token, String tokenSecret) {
        SocialNetworkIntegration socialNetConfService = (SocialNetworkIntegration) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(SocialNetworkIntegration.class);
        socialNetConfService.saveUserSocialNetworkPreferences(exoUserId, SOCIAL_APPLICATION_NAME, socialUserName, token, tokenSecret);
    }

    /**
     * Get the user preferences (token, ...) related to the application
     * @param userId
     * @return the User Social Configuration object
     */
    public UserSocialNetworkPreferences getUserPreferences(String userId) {
        SocialNetworkIntegration socialNetConfService = (SocialNetworkIntegration) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(SocialNetworkIntegration.class);
        return socialNetConfService.getUserSocialNetworkPreferences(userId, SOCIAL_APPLICATION_NAME);
    }
    
    
    public void removeUserPreferences(String userId) {
        SocialNetworkIntegration socialNetConfService = (SocialNetworkIntegration) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(SocialNetworkIntegration.class);
        socialNetConfService.removeUserSocialNetworkPreferences(userId, SOCIAL_APPLICATION_NAME);
        
    }

    public Twitter getTwitter() {
        Twitter twitter = null;
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(consumerKey, secretKey);
        return twitter;
    }

    /**
     * 
     * @param userId
     * @return 
     */
    public Twitter getTwitter(String userId) {
        Twitter twitter = null;
        UserSocialNetworkPreferences prefs = this.getUserPreferences(userId);
        if (prefs != null) {
            String token = prefs.getToken();
            String tokenSecret = prefs.getTokenSecret();
            if (token != null && tokenSecret != null) {
                AccessToken accessToken = new AccessToken(token, tokenSecret);
                twitter = new TwitterFactory().getInstance();
                twitter.setOAuthConsumer(consumerKey, secretKey);
                twitter.setOAuthAccessToken(accessToken);
            }
        }
        return twitter;
    }

    public RequestToken getAuthorizationUrl() {

        RequestToken requestToken = null;
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(consumerKey, secretKey);
            requestToken = twitter.getOAuthRequestToken();

        } catch (TwitterException ex) {
            Logger.getLogger(TwitterService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return requestToken;
    }

    public boolean validateTwitterAuthorization(String requestToken, String pin) {
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(consumerKey, secretKey);
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, pin);
        } catch (TwitterException ex) {
            Logger.getLogger(TwitterService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    /*
    
    public static void main(String args[]) throws Exception{
    // The factory instance is re-useable and thread safe.
    Twitter twitter = new TwitterFactory().getInstance();
    twitter.setOAuthConsumer("e5A7bXNe0K5rWwr9GUSZIA", "d7ru6sWIjMMT5jHWLDdp99lzTOWsCq92Ul4qOYX0tZY");
    RequestToken requestToken = twitter.getOAuthRequestToken();
    AccessToken accessToken = null;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    while (null == accessToken) {
    System.out.println("Open the following URL and grant access to your account:");
    System.out.println(requestToken.getAuthorizationURL());
    System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
    String pin = br.readLine();
    try{
    if(pin.length() > 0){
    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
    }else{
    accessToken = twitter.getOAuthAccessToken();
    }
    } catch (TwitterException te) {
    if(401 == te.getStatusCode()){
    System.out.println("Unable to get the access token.");
    }else{
    te.printStackTrace();
    }
    }
    }
    //persist to the accessToken for future reference.
    System.out.println(twitter.verifyCredentials().getId());
    
    System.out.println("token : " + accessToken.getToken());
    
    System.out.println("tokenSecret : " + accessToken.getTokenSecret());
    
    System.exit(0);
    }
    
     * 
     */
    private static void storeAccessToken(long id, AccessToken accessToken) {

        System.out.println(id + "  " + accessToken);

    }
}
