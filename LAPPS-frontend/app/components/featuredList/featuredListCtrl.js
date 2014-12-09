/**
 * @class featuredListCtr
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying a list of featured
 *              apps.
 */
(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'featuredListCtrl',
                  [
                      '$scope',
                      'swaggerApi',
                      function($scope, swaggerApi) {
                        $scope.currentTab = '';
                        $scope.apps = [];

                        $scope.setTab = function(tab) {
                          if ((tab == 'top' || tab == 'new')
                                  && $scope.currentTab != tab) {
                            $scope.currentTab = tab;
                            $scope.getApps(tab);
                          }
                        }

                        $scope.markdown = 'LAPPS\n'
                                + '=====\n'
                                + '\n'
                                + 'The Layers App Store (LAPPS) is a web-based application showcasing all apps and services that are developed and/or promoted by the Layers project.\n'
                                + '\n'
                                + '##Environment##\n'
                                + '* Java SDK 7\n'
                                + '* Eclipse (Luna) Java EE (https://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/lunasr1)\n'
                                + '* Maven 3.2.3 (if you want to use maven from command line; http://maven.apache.org/download.cgi)\n'
                                + '* Git 2.1.3 (http://git-scm.com/)\n'
                                + '* Node.js 0.10.33 (http://nodejs.org/)\n'
                                + '\n'
                                + 'Newer versions should also work fine.\n'
                                + '\n'
                                + '##Configuration##\n'
                                + 'Configure Eclipse:\n'
                                + '* Import https://google-styleguide.googlecode.com/svn/trunk/eclipse-java-google-style.xml as Eclipse Java format (Preferences -> Java -> Code Style -> Formatter)\n'
                                + '* Configurate the Java -> Code Style (rest is Eclipse default):\n'
                                + '  * Enable "Format source code"\n'
                                + '  * Enable "Remove unused imports"\n'
                                + '* Configure the Java -> Editor -> Save Actions (rest is Eclipse default):\n'
                                + '  * Enable "Perform the selected actions on save"\n'
                                + '  * Enable "Format source code"\n'
                                + '  * Enable "Organize imports"\n'
                                + '* Import https://raw.githubusercontent.com/jokeyrhyme/eclipse-formatter-profiles/master/google-style-guide-javascript-eclipse.xml as Eclipse JavaScript format (Preferences -> JavasSript -> Code Style -> Formatter)\n'
                                + '* Configure the JavaScript -> Editor -> Save Actions (rest is Eclipse default):\n'
                                + '  * Enable "Perform the selected actions on save"\n'
                                + '  * Enable "Format source code"\n'
                                + '\n'
                                + '##Build##\n'
                                + 'LAPPS Backend (run these commands from your project folder):\n'
                                + '* mvn clean           --- clean\n'
                                + '* mvn test            --- test backend\n'
                                + '* mvn exec:java       --- compile and start on jetty server (port 8080, please execute in LAPPS-backend folder)\n'
                                + '* mvn javadoc:javadoc --- generates JavaDoc documentation\n'
                                + '* This commands also work inside Eclipse (Run -> Maven build..), then leave the mvn prefix.\n'
                                + '\n'
                                + 'LAPPS Frontend (run these commands from your project folder):\n'
                                + '* npm install        --- install dependencies (will be done automatically when running the start command)\n'
                                + '* npm test           --- test frontend\n'
                                + '* npm start          --- start on node server (port 8000)\n'
                                + '* npm run protractor --- start e2e tests (start the server with npm start first in another console)  \n'
                                + '* npm run doc 		 --- create jsdoc documentation in out/\n'
                                + '* npm run deploy	 --- create a deployable containing minified files in deploy/\n'
                                + '\n'
                                + '##Links##\n'
                                + '\n'
                                + 'Nightly Builds:\n'
                                + '* [![Build Status](http://layers.dbis.rwth-aachen.de/jenkins/job/LAPPS/badge/icon)](http://layers.dbis.rwth-aachen.de/jenkins/job/LAPPS/)\n'
                                + '* [Current Version of "develop" Branch](http://buche.informatik.rwth-aachen.de:9080/lapps-0.2-SNAPSHOT/)\n'
                                + '* [Swagger API Documentation](http://buche.informatik.rwth-aachen.de:9080/lapps-0.2-SNAPSHOT/swagger-documentation/)\n'
                                + '* [Jenkins Home Directory](http://layers.dbis.rwth-aachen.de/jenkins/job/LAPPS/)\n'
                                + '* [Backend Documentation](http://layers.dbis.rwth-aachen.de/jenkins/job/LAPPS/de.rwth.dbis.layers.lapps$LAPPS-backend/javadoc/)\n'
                                + '* [Frontend Documentation](http://layers.dbis.rwth-aachen.de/jenkins/job/LAPPS/JavaScript_Documentation/)\n'
                                + '* [Backend War Files in Archiva](http://role.dbis.rwth-aachen.de:9911/archiva/browse/de.rwth.dbis.layers.lapps)\n'
                                + '\n'
                                + 'Learning Room (authentication required):  \n'
                                + '* [Shared Documents](https://www3.elearning.rwth-aachen.de/ws14/14ws-29924/collaboration/Lists/SharedDocuments/Forms/AllItems.aspx?RootFolder=%2Fws14%2F14ws-29924%2Fcollaboration%2FLists%2FSharedDocuments%2FLAPPS&FolderCTID=0x0120005A033B78570B2D45A235DFFEE8383BD0&View=%7B31481E6C-CB5F-4BD5-9CC5-643AF904FC96%7D&InitialTabId=Ribbon%2EDocument&VisibilityContext=WSSTabPersistence)\n'
                                + '\n'
                                + 'Technology:\n'
                                + '* https://jersey.java.net/documentation/latest/index.html\n'
                                + '* https://docs.angularjs.org/tutorial/\n'
                                + '* http://getbootstrap.com/getting-started/\n'
                                + '* http://nodejs.org/documentation/\n'
                                + '* http://git-scm.com/docs\n'
                                + '\n'
                                + 'Code Style:\n'
                                + '* http://google-styleguide.googlecode.com/svn/trunk/javaguide.html\n'
                                + '* http://google-styleguide.googlecode.com/svn/trunk/htmlcssguide.xml\n'
                                + '* http://google-styleguide.googlecode.com/svn/trunk/javascriptguide.xml\n'
                                + '* http://google-styleguide.googlecode.com/svn/trunk/angularjs-google-style.html\n';
                        marked.setOptions({
                          renderer: new marked.Renderer(),
                          gfm: true,
                          tables: true,
                          breaks: false,
                          pedantic: false,
                          sanitize: true,
                          smartLists: true,
                          smartypants: false
                        });
                        $scope.markdownOut = marked($scope.markdown);
                        $scope.randShortDescription = [
                            'Culpa aute lorem commodo reprehenderit deserunt cillum ea voluptate fugiat in dolore pariatur. Excepteur esse.',
                            'Aute exercitation nisi mollit eiusmod reprehenderit lorem laborum. Laboris qui aliquip occaecat ut elit, proident.',
                            'In eiusmod velit ullamco labore do sint consectetur id minim quis anim ea dolore cupidatat.',
                            'Fugiat sunt nostrud et do sed magna ex ipsum laborum. Laboris minim exercitation occaecat sit.',
                            'Pariatur. Aliqua. Tempor culpa in deserunt anim consectetur fugiat sunt adipiscing aliquip amet, nisi dolore.',
                            'Labore pariatur. Dolore esse excepteur laboris occaecat dolor cillum et duis est sit ipsum eu.',
                            'Cillum dolore irure nostrud pariatur. Officia consequat. Fugiat sed dolor eu duis elit, consectetur ea.',
                            'Sed aliqua. In laborum. Dolore consequat. Aliquip deserunt adipiscing anim pariatur. Sunt reprehenderit amet, ut.',
                            'Ea ad consectetur quis pariatur. Anim nulla in eiusmod in excepteur nisi minim fugiat in.',
                            'Dolore elit, amet, ad in nulla qui aliquip est eiusmod non excepteur minim ipsum exercitation.',
                            'Esse nulla irure excepteur velit minim nisi in labore nostrud non sint occaecat amet, reprehenderit.',
                            'Enim eu cupidatat id voluptate nisi deserunt tempor est ex officia ut aute ipsum exercitation.',
                            'Sint mollit voluptate qui sunt excepteur aute quis labore pariatur. Dolore adipiscing dolor esse magna.',
                            'Ex consectetur enim non labore elit, nulla ea ipsum reprehenderit lorem duis adipiscing eu laboris.',
                            'Velit voluptate cupidatat nisi ut dolor exercitation nostrud reprehenderit eiusmod aute ut fugiat dolore elit.',
                            'Velit veniam, et quis fugiat ut ut amet, eu anim exercitation irure lorem labore commodo.',
                            'Proident, exercitation anim in non labore sunt do lorem ea laborum. Amet, ut ut enim.',
                            'Ut qui duis aliqua. Cillum fugiat sint pariatur. Irure do officia laboris in commodo incididunt.',
                            'Velit sint aliqua. Sunt officia in aute anim consequat. Ullamco lorem ut consectetur dolor sit.',
                            'Aliquip laborum. Ut aute dolor reprehenderit dolore nostrud est eiusmod sunt ipsum non consectetur do.'];

                        $scope.getApps = function(tab) {
                          /*
                           * $http.get('assets/dummy/appList.json').success(function(data) {
                           * $scope.apps = data;
                           * 
                           * if (tab == 'new') { $scope.apps.sort(function(a, b) {
                           * return b.created - a.created; }); } else {
                           * $scope.apps.sort(function(a, b) { return b.rating -
                           * a.rating; }); } });
                           */

                          swaggerApi.apps
                                  .getAllApps()
                                  .then(
                                          function(data) {
                                            $scope.apps = data;
                                            for (var i = 0; i < $scope.apps.length; i++) {
                                              var short = $scope.randShortDescription[Math
                                                      .floor(Math.random()
                                                              * $scope.randShortDescription.length)];
                                              $scope.apps[i].shortDescription = short;
                                              $scope.apps[i].images = [];
                                              $scope.apps[i].rating = Math
                                                      .floor(Math.random() * 5);
                                              for (var k = 0; k < $scope.apps[i].artifacts.length; k++) {
                                                var item = $scope.apps[i].artifacts[k];
                                                if (item.artifact.type
                                                        .indexOf('thumbnail') > -1) {
                                                  $scope.apps[i].thumbnail = item.url;
                                                }
                                              }
                                            }
                                            if (tab == 'new') {
                                              $scope.apps.sort(function(a, b) {
                                                return b.dateCreated
                                                        - a.dateCreated;
                                              });
                                            } else {
                                              $scope.apps.sort(function(a, b) {
                                                return b.rating - a.rating;
                                              });
                                            }
                                          });
                        }

                        $scope.setTab('new');
                      }]);
}).call(this);