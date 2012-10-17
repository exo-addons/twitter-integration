Welcome to the exo-twitter-integration wiki!

Take a look to this video to see it in action: http://www.youtube.com/watch?v=KN0jsOdauPU


How to Build
============

    mvn clean install


How to Run
==========

* Deploy dependencies in your application server : 
** http://twitter4j.org/
** JSTL if not present
* Copy the `twitter.service-1.0.jar` into your application server classpath (for example $TOMCAT/lib )
* Deploy the `texo-extension-twitter.war` into your web container
* Drop the "Twitter Integration" portlet on the Profile page

