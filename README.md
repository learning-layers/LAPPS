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
* Configurate the Java -> Code Style (rest is Eclipse default):
  * Enable "Format source code"
  * Enable "Remove unused imports"
* Configurate the Java -> Editor -> Save Actions (rest is Eclipse default):
  * Enable "Perform the selected actions on save"
  * Enable "Format source code"
  * Enable "Organize imports"
* Import https://raw.githubusercontent.com/jokeyrhyme/eclipse-formatter-profiles/master/google-style-guide-javascript-eclipse.xml as Eclipse JavaScript format (Preferences -> JavasSript -> Code Style -> Formatter)
* Configurate the JavaScript ->Editor -> Save Actions (rest is Eclipse default):
  * Enable "Perform the selected actions on save"
  * Enable "Format source code"

##Build##
LAPPS Backend (run these commands from your project folder):
* mvn clean           --- clean
* mvn test            --- test backend
* mvn exec:java       --- compile and start on jetty server (port 8080)
* mvn javadoc:javadoc --- generates JavaDoc documentation
* This commands works also inside Eclipse (Run -> Maven build..), then leave the mvn prefix.

LAPPS Frontend (run these commands from your project folder):
* npm install        --- install dependencies (will be done automatically when running the start command)
* npm test           --- test frontend
* npm start          --- start on node server (port 8000)
* npm run protractor --- start e2e tests (start the server with npm start first in another console)  

##Links##

Build Status:
* [![Build Status](http://layers.dbis.rwth-aachen.de/jenkins/job/LAPPS/badge/icon)](http://layers.dbis.rwth-aachen.de/jenkins/job/LAPPS/)(http://layers.dbis.rwth-aachen.de/jenkins/job/LAPPS/)

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

#Happy Coding!#
