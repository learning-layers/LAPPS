LAPPS
=====

The Layers App Store (LAPPS) is a web-based application showcasing all apps and services that are developed and/or promoted by the Layers project.

##Environment##
* Java SDK 7
* Eclipse (Mars) Java EE (http://www.eclipse.org/downloads/packages/release/Mars/M2)
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
  * Enable "Orginaze imports"
* Import https://github.com/jokeyrhyme/eclipse-formatter-profiles/blob/master/google-style-guide-javascript-eclipse.xml as Eclipse Javascript format (Preferences -> Javascript -> Code Style -> Formatter)
* Configurate the Javascript ->Editor -> Save Actions (rest is Eclipse default):
  * Enable "Perform the selected actions on save"
  * Enable "Format source code"

##Build##
LAPPS Backend (run these commands from your project folder):
* mvn clean         --- clean
* mvn test          --- test backend
* mvn exec:java     --- compile and start on jetty server (port 8080)
* This commands works also inside Eclipse (Run -> Maven build ...)

LAPPS Frontend (run these commands from your project folder):
* npm install       --- install dependencies
* npm test          --- test frontend
* npm start         --- start on node server (port 8000)

##Links##
Project Links:
* https://www3.elearning.rwth-aachen.de/ws14/14ws-29924/Dashboard.aspx
* http://layers.dbis.rwth-aachen.de/jenkins/job/LAPPS%20Backend/

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
