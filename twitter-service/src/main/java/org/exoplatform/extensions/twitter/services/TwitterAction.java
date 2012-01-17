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
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import org.apache.commons.chain.Context;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.command.action.Action;

/**
 *
 * @author tgrall
 */
public class TwitterAction implements Action {

    public boolean execute(Context context) throws Exception {
        Property property = (Property) context.get("currentItem");
        Node node = property.getParent();

        // since I do not know how to add a listener when the full activity
        // is savec I manage this with the following "hack"
        // This Action is execute each time an activity add a property (TOO MUCH!!!)
        // when the "title" is added I execute my code
        // take the user from latest modfier since it is set when updating the activity (?)
        //   - I would prefer to have this in a complete entity
        if (property.getName().equals("soc:title")) {
            String userId = null;
            String message = property.getValue().getString();
            
            if (node.hasProperty("exo:lastModifier") && message != null) {
                userId = node.getProperty("exo:lastModifier").getString();
                checkMessage(userId, message);
            }
            
        }
        
        
        return true;
    }
    
    
    
    private void checkMessage(String userId, String message) {

        
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
