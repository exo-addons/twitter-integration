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

import org.chromattic.api.annotations.FormattedBy;
import org.chromattic.api.annotations.Name;
import org.chromattic.api.annotations.NamingPrefix;
import org.chromattic.api.annotations.PrimaryType;
import org.chromattic.api.annotations.Property;
import org.chromattic.ext.format.BaseEncodingObjectFormatter;


@PrimaryType(name = "plf:userSocialNetworkPreferences")
@FormattedBy(BaseEncodingObjectFormatter.class)
@NamingPrefix("plf")
public abstract class UserSocialNetworkPreferences {

  @Name
  public abstract String getName();   
  
  
  @Property(name = "plf:networkName" )
  public abstract String getNetworkName();

  public abstract void setNetworkName(String networkName);

  
  @Property(name = "plf:userName")
  public abstract String getUserName();
          
  public abstract void setUserName(String userName);

  
  
  @Property(name = "plf:userId")
  public abstract String getUserID();

  public abstract void setUserId(String userId);

  
  
  @Property(name = "plf:token")
  public abstract String getToken();

  public abstract void setToken(String token);
  
  
  
  @Property(name = "plf:tokenSecret")
  public abstract String getTokenSecret();

  public abstract void setTokenSecret(String tokenSecret);

}
