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
package org.exoplatform.extensions.social.network.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.jcr.Node;

import org.apache.commons.lang.ArrayUtils;
import org.chromattic.api.ChromatticSession;
import org.chromattic.ext.ntdef.NTFolder;
import org.exoplatform.commons.chromattic.ChromatticLifeCycle;
import org.exoplatform.commons.chromattic.ChromatticManager;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;

/**
 *
 * @author tgrall
 */
public class SocialNetworkIntegration {

    private final String CHROMATTIC_LIFECYCLE_NAME = "socialnetworkconfiguration";
    private static final String PARENT_PLATFORM_RELATIVE_PATH = "Extensions";
    private static final String PARENT_SOCIAL_NETWORKS_RELATIVE_PATH = "social-networks";
    private static final String PARENT_RELATIVE_PATH = PARENT_PLATFORM_RELATIVE_PATH + "/" + PARENT_SOCIAL_NETWORKS_RELATIVE_PATH;
    private static final int APPLICATION_DATA_FOLDER_USER = 1;
    private static final int APPLICATION_DATA_FOLDER_PLATFORM = 2;
    private static final String SOCIAL_NETWORK_CONF_ROOT_PATH = "/";
    private static final ThreadLocal<ChromatticSession> session = new ThreadLocal<ChromatticSession>();
    private ChromatticLifeCycle lifeCycle;
    private NodeHierarchyCreator nodeHierarchyCreator;

    public SocialNetworkIntegration(ChromatticManager chromatticManager, NodeHierarchyCreator nodeHierarchyCreator) {
        this.lifeCycle = chromatticManager.getLifeCycle(CHROMATTIC_LIFECYCLE_NAME);
        this.nodeHierarchyCreator = nodeHierarchyCreator;
    }

    /**
     * Create the user preferences for the user. This method has been tested with Twitter as a start. It may be necessary 
     * to extend/create new version of this method to add new social networks
     * 
     * @param exoUserId eXo Login name that is used to select the location/set the owner
     * @param socialNetwork Name of the Social Network (twitter, facebook ,....). Name of the JCR node
     * @param socialNetworkUsername User name associated to the social network for example twitter.com/exoplatform
     * @param token  User token used to access the application.
     * @param tokenSecret User token secret use to access the application.
     */
    public void saveUserSocialNetworkPreferences(String exoUserId, String socialNetwork, String socialNetworkUsername, String token, String tokenSecret) {
        //TODO : refactor access to preferences in one single location
        try {
            String parentNodePath = null;
            parentNodePath = getUserApplicationDataNodePath(exoUserId, true);
            String socialNetworkConfigFileName = parentNodePath + "/" + socialNetwork;
            ChromatticSession chromatticSession = getSession();
            UserSocialNetworkPreferences userSocialConf = chromatticSession.findByPath(UserSocialNetworkPreferences.class, socialNetworkConfigFileName, false);
            if (userSocialConf == null) {
                NTFolder parentNode = chromatticSession.findByPath(NTFolder.class, parentNodePath, false);
                if (parentNode == null) {
                    throw new IllegalStateException("User ApplicationData node couldn't be found.");
                }
                userSocialConf = chromatticSession.create(UserSocialNetworkPreferences.class, socialNetwork);
                chromatticSession.persist(parentNode, userSocialConf);
                userSocialConf.setNetworkName(socialNetwork);
                userSocialConf.setToken(token);
                userSocialConf.setTokenSecret(tokenSecret);
                userSocialConf.setUserName(socialNetworkUsername);
                chromatticSession.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the preferences based on the user id and social network name
     * @param exoUserId eXo user
     * @param socialNetwork name of the social network
     * @return 
     */
    public UserSocialNetworkPreferences getUserSocialNetworkPreferences(String exoUserId, String socialNetwork) {

        //TODO : refactor access to preferences in one single location
        UserSocialNetworkPreferences userSocialConf = null;
        try {

            String parentNodePath = null;
            parentNodePath = getUserApplicationDataNodePath(exoUserId, true);
            String socialNetworkConfigFileName = parentNodePath + "/" + socialNetwork;
            ChromatticSession chromatticSession = getSession();
            userSocialConf = chromatticSession.findByPath(UserSocialNetworkPreferences.class, socialNetworkConfigFileName, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userSocialConf;
    }

    /**
     * Remove the social network preferences for the user and social network.
     * This method just deletes the node from the user Application Data folder.
     * @param exoUserId eXo user id
     * @param socialNetwork name of the social network, that is also the name of the node that stores the preferencess
     */
    public void removeUserSocialNetworkPreferences(String exoUserId, String socialNetwork) {
        UserSocialNetworkPreferences userSocialConf = null;
        try {
            String parentNodePath = null;
            parentNodePath = getUserApplicationDataNodePath(exoUserId, true);
            String socialNetworkConfigFileName = parentNodePath + "/" + socialNetwork;
            ChromatticSession chromatticSession = getSession();
            userSocialConf = chromatticSession.findByPath(UserSocialNetworkPreferences.class, socialNetworkConfigFileName, false);
            chromatticSession.remove(userSocialConf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param userId
     * @param create
     * @return 
     */
    private String getSocialNetworkApplicationDataNodePath(int folderType, String userId, boolean create) {
        String parentNodePath = null;
        try {
            Node applicationNode = null;
            if (folderType == APPLICATION_DATA_FOLDER_PLATFORM) {
                nodeHierarchyCreator.getPublicApplicationNode(SessionProvider.createSystemProvider());
            } else if (folderType == APPLICATION_DATA_FOLDER_USER) {
                if (userId == null) {
                    //TODO: Raise an exception to alert that user cannot be null
                    throw new Exception("User cannot be null when accessing User Application Data Folder");
                }
                nodeHierarchyCreator.getUserApplicationNode(SessionProvider.createSystemProvider(), userId);
            }

            if (!applicationNode.hasNode(PARENT_RELATIVE_PATH)) {
                if (create) {
                    if (!applicationNode.hasNode(PARENT_PLATFORM_RELATIVE_PATH)) {
                        applicationNode.addNode(PARENT_PLATFORM_RELATIVE_PATH, "nt:folder");
                    }
                    applicationNode = applicationNode.addNode(PARENT_RELATIVE_PATH, "nt:folder");
                    applicationNode.addMixin("mix:referenceable");
                    applicationNode.getSession().save();
                    parentNodePath = applicationNode.getPath();
                } else {
                    return null;
                }
            } else {
                parentNodePath = applicationNode.getPath() + "/" + PARENT_RELATIVE_PATH;
            }
            parentNodePath = parentNodePath.split(SOCIAL_NETWORK_CONF_ROOT_PATH, 2)[1];

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return parentNodePath;
    }

    /**
     * Return the path to the application folder in which the preferences will be saved
     * @param userId eXo user in which the folder will be found/created
     * @param create if true the folder will be created when not present
     * @return 
     */
    private String getUserApplicationDataNodePath(String userId, boolean create) {
        return this.getSocialNetworkApplicationDataNodePath(APPLICATION_DATA_FOLDER_USER, userId, create);
    }

    /**
     * Return/Create the social network extension application folder
     * @param create true if the folder should be automatically created if not found (default)
     * @return the location, as string , of the extension location
     */
    private String getApplicationDataNodePath(boolean create) {
        return this.getSocialNetworkApplicationDataNodePath(APPLICATION_DATA_FOLDER_PLATFORM, null, create);
    }

    public ChromatticSession getSession() {
        ChromatticSession session = lifeCycle.getChromattic().openSession();
        return session;
    }
}
