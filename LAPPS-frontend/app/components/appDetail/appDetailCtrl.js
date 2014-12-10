/**
 * @class appDetailCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying the details of a
 *              selected app.
 */

(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'appDetailCtrl',
                  [
                      '$scope',
                      '$routeParams',
                      'swaggerApi',
                      function($scope, $routeParams, swaggerApi) {

                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Maximum amount of tags, that can be
                         *              displayed in the app details.
                         */
                        $scope.MAX_TAGS = 999;

                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Amount of tags displayed in the app
                         *              details in collapsed state.
                         */
                        $scope.DEFAULT_TAGS = 5;
                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description An id is passed as a routing parameter
                         *              in the url. This way the controller can
                         *              decide, which app's details to display.
                         */
                        $scope.appId = $routeParams.appId;

                        /**
                         * @field
                         * @type boolean
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description The state of the long description area.
                         *              Important for applying correct classes,
                         *              when state changes.
                         */
                        $scope.collapsed = true;

                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Current limit on the amount of tags to
                         *              display.
                         */
                        $scope.tagLimit = $scope.DEFAULT_TAGS;

                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Time in ms to wait until the image in
                         *              the carousel is changed.
                         */
                        $scope.interval = 4000;

                        /**
                         * @field
                         * @type app
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Stores the app object retrieved from the
                         *              backend.
                         */
                        $scope.appData = {};

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
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Toggles
                         *              {@link lapps.lappsControllers.appDetailCtrl.$scope.collapsed}.
                         */
                       
                        $scope.expandCollapseDescription = function() {
                          if ($scope.collapsed) {
                            $scope.collapsed = false;
                          } else {
                            $scope.collapsed = true;
                          }
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Toggles
                         *              {@link lapps.lappsControllers.appDetailCtrl.$scope.tagLimit}
                         *              to change the amount of currently
                         *              displayed tags.
                         */
                        $scope.expandCollapseTags = function() {
                          if ($scope.tagLimit == $scope.DEFAULT_TAGS) {
                            $scope.tagLimit = $scope.MAX_TAGS;
                          } else {
                            $scope.tagLimit = $scope.DEFAULT_TAGS;
                          }
                        }

                        /**
                         * @function
                         * @type string
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @param {number|string}
                         *          size size in KB to convert to MB.
                         * @description Converts size in KB to MB with 1 digit
                         *              after the comma. If size is less than
                         *              1024 the value is not changed. The
                         *              string MB or KB is appended to the
                         *              result (12.5 MB).
                         */
                        $scope.convertSize = function(size) {
                          size = parseInt(size, 10);
                          if (size < 1024) {
                            return size + " KB";
                          } else {
                            var div = Math.floor(size / 1024);
                            var rem = size % 1024;
                            return div + "." + Math.round(rem / 100) + " MB";
                          }
                        }
                        /**
                         * @function
                         * @type string
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @param {number}
                         *          utc timestamp.
                         * @description Converts timestamp in date string
                         *              day-month-year.
                         */
                        $scope.convertDate = function(utc) {
                          var d = new Date(utc);
                          var m_names = new Array("January", "February",
                                  "March", "April", "May", "June", "July",
                                  "August", "September", "October", "November",
                                  "December");

                          var curr_date = d.getDate();
                          var curr_month = d.getMonth();
                          var curr_year = d.getFullYear();
                          return (curr_date + "-" + m_names[curr_month] + "-" + curr_year);
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
                        $scope.appData.descriptionMarkDown = marked($scope.markdown);
                        var short = $scope.randShortDescription[Math.floor(Math
                                .random()
                                * $scope.randShortDescription.length)];
                        $scope.appData.shortDescription = short;
                        swaggerApi.apps
                                .getApp({
                                  id: +$scope.appId
                                })
                                .then(
                                        function(data) {
                                          $scope.appData = data;
                                          var short = $scope.randShortDescription[Math
                                                  .floor(Math.random()
                                                          * $scope.randShortDescription.length)];
                                          $scope.appData.shortDescription = short;
                                          var long = '';
                                          for (var j = 0; j < 60; j++) {
                                            var item = $scope.randShortDescription[Math
                                                    .floor(Math.random()
                                                            * $scope.randShortDescription.length)];
                                            long += item
                                                    + ((Math.random() > 0.9)
                                                            ? '\n\n' : ' ');
                                          }
                                          $scope.appData.description = $scope.markdown;
                                          var developer = $scope.randShortDescription[Math
                                                  .floor(Math.random()
                                                          * $scope.randShortDescription.length)];
                                          $scope.appData.descriptionMarkDown = marked($scope.appData.description);

                                          $scope.appData.developedBy = developer
                                                  .split(' ').slice(0, 2).join(
                                                          ' ');
                                          $scope.appData.rating = Math
                                                  .floor(Math.random() * 5);
                                          $scope.appData.tags = developer
                                                  .split(' ');
                                          $scope.appData.license = 'Copyright Me 2014';
                                          $scope.appData.supportPage = 'http://www.google.com';
                                          $scope.appData.images = [];
                                          for (var k = 0; k < $scope.appData.artifacts.length; k++) {
                                            var item = $scope.appData.artifacts[k];
                                            if (item.artifact.type
                                                    .indexOf('image') > -1) {
                                              $scope.appData.images
                                                      .push(item.url);
                                              $scope.appData.images
                                                      .push('http://i.imgur.com/yrxhYri.jpg');
                                              $scope.appData.images
                                                      .push('http://i.imgur.com/AvW7ZGH.jpg');

                                              $scope.appData.images = $scope.appData.images
                                                      .filter(function(item,
                                                              pos, self) {
                                                        return self
                                                                .indexOf(item) == pos;
                                                      })
                                            } else if (item.artifact.type
                                                    .indexOf('thumbnail') > -1) {
                                              $scope.appData.thumbnail = item.url;
                                            }
                                          }
                                        });
                      }]);
}).call(this);