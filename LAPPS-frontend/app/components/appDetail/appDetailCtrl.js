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
                       

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Toggles
                         *              {@link lapps.lappsControllers.appDetailCtrl.$scope.collapsed}.
                         */

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
                        $scope.markdown = 'Dummy Description\n'
                                + '=====\n'
                                + '\n'
                                + 'Culpa aute lorem commodo reprehenderit deserunt cillum ea voluptate fugiat in dolore pariatur. Excepteur esse.\n'
                                + '\n'
                                + '##Cupidatat Nisi##\n'
                                + '* Fugiat sunt nostrud et do sed.\n'
                                + '* Sed aliqua.\n'
                                + '* Velit voluptate cupidatat.\n'
                                + '* Ut qui duis aliqua.\n'
                                + '* Ea ad consectetur quis pariatur. (http://nodejs.org/)\n'
                                + '\n'
                                + 'Velit voluptate cupidatat nisi ut dolor exercitation nostrud reprehenderit eiusmod aute ut fugiat dolore elit. Cillum dolore irure nostrud pariatur. Officia consequat. Fugiat sed dolor eu duis elit, consectetur ea.\n'
                                + '\n'
                                + '##Commodo Reprehenderit##\n'
                                + 'Sunt excepteur aute:\n'
                                + '* In laborum. Dolore consequat. Aliquip deserunt adipiscing anim pariatur. Sunt reprehenderit amet: https://www.google.com/laksjdalkjdlkjaslkdjalskdj\n'
                                + '* Cillum fugiat sint pariatur:\n'
                                + '  * Nulla ea ipsum reprehenderit lorem\n'
                                
                                + '* Cillum dolore irure nostrud pariatur:\n'
                                + '  * Proident, exercitation anim in non labore.\n'
                                + '  * Ea ad consectetur quis pariatur.\n'
                                + '  * Aliquip laborum.\n' +
                                + '\n'
                                + 'Lbore commodo:\n'
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
