<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<portlet:defineObjects />

<div style="heigh:100%"  class="UIProfile">
  <div class="Container">
 	<div class="UIBasicInfoSection">
		<div id="UITitleBar" class="UISocTitleBar">
			<h5 class="TitleBar ClearFix">Twitter Integration <img src="/exo-extension-twitter/images/twitter-logo.png" width="80px" valign="top"></h5>
		</div>
  		<div class="PortletContent">
			<c:if test="${isTwitterConfigured}">
			    Your Twitter account is associated with your profile. You can now post activities directly on Twitter, just put <b>#tw</b> in your message.
                            
                            <br>
                            <br>
                            <portlet:actionURL var="action">
                                <portlet:param name="action" value="revoke" />
                            </portlet:actionURL>

                            <a href="<%= action %>">Revoke Twitter access</a>.
                            
			</c:if>	
			<c:if test="${ ! isTwitterConfigured}">
				You do not have any Twitter account associated with your profile. You can do it using the following steps:
                                <portlet:actionURL var="action"/>
				<form action="<%= action %>" method="post">
			            <input type="hidden" name="action" value="save"  />
				<ul>
					<li>1. Click <b><a href="${twitterAuthToken.authorizationURL}" target="_blank">here</a></b>  to get the a Pin number</a></li> 
					<li>2. Enter the Pin number here : <input name="pin" style="width: 100px"/> </li> 
					<li>3. <input type="submit" name="Save" value="Save"> the Pin number </li> 
				</ul> 
				</form>
			</c:if>
  		</div>
	</div>
  </div>
</div>



