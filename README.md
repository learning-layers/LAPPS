LAPPS
=====

The Layers App Store (LAPPS) is a web-based application showcasing all apps and services that are developed and/or promoted by the Layers project.

##Environment##
* Java SDK 7
* Eclipse (Luna) Java EE (https://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/lunasr1)
* Maven 3.2.3 (if you want to use maven from command line; http://maven.apache.org/download.cgi)
* Git 2.1.3 (http://git-scm.com/)
* Node.js 0.10.33 (http://nodejs.org/)

Newer versions should also work fine.

##Configuration##
Configure Eclipse:
* Import https://google-styleguide.googlecode.com/svn/trunk/eclipse-java-google-style.xml as Eclipse Java format (Preferences -> Java -> Code Style -> Formatter)
* Configure the Java -> Code Style (rest is Eclipse default):
  * Enable "Format source code"
  * Enable "Remove unused imports"
* Configure the Java -> Editor -> Save Actions (rest is Eclipse default):
  * Enable "Perform the selected actions on save"
  * Enable "Format source code"
  * Enable "Organize imports"
* Import https://raw.githubusercontent.com/jokeyrhyme/eclipse-formatter-profiles/master/google-style-guide-javascript-eclipse.xml as Eclipse JavaScript format (Preferences -> JavasSript -> Code Style -> Formatter)
* Configure the JavaScript -> Editor -> Save Actions (rest is Eclipse default):
  * Enable "Perform the selected actions on save"
  * Enable "Format source code"

##Build##
LAPPS Backend (run these commands from your project folder):
* mvn clean             --- clean
* mvn test              --- test backend
* mvn exec:java         --- compile and start on Jetty (port 8080, execute in LAPPS-backend folder)
* mvn javadoc:javadoc   --- generates JavaDoc documentation
* mvn package           --- generate a servlet that can be deployed to an application server
* mvn test -Dtest=Utils --- runs a "test" that generates mockup data in the database
These commands also work inside Eclipse (Run -> Maven build..), then leave away the mvn prefix.

LAPPS Frontend (run these commands from your project folder):
* npm install        --- install dependencies (will be done automatically when running the start command)
* npm test           --- test frontend
* npm start          --- start on node server (port 8000)
* npm run protractor --- start e2e tests (start the server with npm start first in another console)  
* npm run doc 		 --- create jsdoc documentation in out/
* npm run deploy	 --- create a deployable containing minified files in deploy/
* npm run api        --- fetches the up to date Swagger api from the deploy server for frontend usage
* npm run apilocal   --- fetches the up to date Swagger api from the local server for frontend usage

##Links##
Nightly Builds:
* [![Build Status](http://layers.dbis.rwth-aachen.de/jenkins/job/LAPPS/badge/icon)](http://layers.dbis.rwth-aachen.de/jenkins/job/LAPPS/)
* [Current Version of "develop" Branch](http://buche.informatik.rwth-aachen.de:9080/lapps-0.3-SNAPSHOT/)
* [Swagger API Documentation](http://buche.informatik.rwth-aachen.de:9080/lapps-0.3-SNAPSHOT/swagger-documentation/)
* [Jenkins Home Directory](http://layers.dbis.rwth-aachen.de/jenkins/job/LAPPS/)
* [Backend Documentation](http://layers.dbis.rwth-aachen.de/jenkins/job/LAPPS/de.rwth.dbis.layers.lapps$LAPPS-backend/javadoc/)
* [Frontend Documentation](http://layers.dbis.rwth-aachen.de/jenkins/job/LAPPS/JavaScript_Documentation/)
* [Backend War Files in Archiva](http://role.dbis.rwth-aachen.de:9911/archiva/browse/de.rwth.dbis.layers.lapps)

Learning Room (authentication required):  
* [Shared Documents](https://www3.elearning.rwth-aachen.de/ws14/14ws-29924/collaboration/Lists/SharedDocuments/Forms/AllItems.aspx?RootFolder=%2Fws14%2F14ws-29924%2Fcollaboration%2FLists%2FSharedDocuments%2FLAPPS&FolderCTID=0x0120005A033B78570B2D45A235DFFEE8383BD0&View=%7B31481E6C-CB5F-4BD5-9CC5-643AF904FC96%7D&InitialTabId=Ribbon%2EDocument&VisibilityContext=WSSTabPersistence)

Technology:
* https://jersey.java.net/documentation/latest/index.html
* https://docs.angularjs.org/tutorial/
* http://getbootstrap.com/getting-started/
* http://nodejs.org/documentation/
* http://git-scm.com/docs

Code Style:
* http://google-styleguide.googlecode.com/svn/trunk/javaguide.html
* http://google-styleguide.googlecode.com/svn/trunk/htmlcssguide.xml
* http://google-styleguide.googlecode.com/svn/trunk/javascriptguide.xml
* http://google-styleguide.googlecode.com/svn/trunk/angularjs-google-style.html
