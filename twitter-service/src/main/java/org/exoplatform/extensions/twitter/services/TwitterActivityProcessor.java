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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.social.core.BaseActivityProcessorPlugin;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
/**
 *
 * @author tgrall
 */
public class TwitterActivityProcessor extends BaseActivityProcessorPlugin {

    
    
  public TwitterActivityProcessor(InitParams params) {
    super(params);
  }
    
    @Override
    public void processActivity(ExoSocialActivity activity) {
        if (activity != null ) {
           // this.findTags( activity.getActivityStream().getPrettyId() , activity.getTitle());
        }
    }

    
    private void findTags(String userId, String message) {
        String token; // = "437360518-XBVhsEcv5d0Lijqpcpmj50kB564VBUqJbA9ECr2k";
        String tokenSecret; // = "V8nbttrPmWsaShye5Ubo9ZcwnS5i4p65GoZEBvfTI";
        // search for the # sign
        Pattern pattern = Pattern.compile("#([^\\s]+)|#([^\\s]+)$");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            
            String hashtag = matcher.group().substring(1);
            if (hashtag.equals("tw")) {
                TwitterService twitterService = (TwitterService) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(TwitterService.class);
                twitterService.updateStatus(userId, message);
            }
            
        }
        
        
    }
    
   
    
    
}
