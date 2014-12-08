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
                        $scope.MAX_TAGS = 999;
                        $scope.DEFAULT_TAGS = 5;
                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description An id is passed as a routing parameter
                         *              in the url. This way the controller can
                         *              decide, which (detail)-page of an app to
                         *              display.
                         */
                        $scope.appId = $routeParams.appId;

                        $scope.currentVersion = "ios";
                        $scope.collapsed = true;
                        $scope.tagLimit = $scope.DEFAULT_TAGS;

                        $scope.interval = 4000;
                        $scope.chosenVersion = function(event) {
                          $scope.currentVersion = event.target.id;
                        };

                        $scope.appData = {};

                        $scope.expandCollapseDescription = function() {
                          if ($scope.collapsed) {
                            $scope.collapsed = false;
                          } else {
                            $scope.collapsed = true;
                          }
                        }

                        $scope.expandCollapseTags = function() {
                          if ($scope.tagLimit == $scope.DEFAULT_TAGS) {
                            $scope.tagLimit = $scope.MAX_TAGS;
                          } else {
                            $scope.tagLimit = $scope.DEFAULT_TAGS;
                          }
                        }
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
                                          $scope.appData.description = long;
                                          var developer = $scope.randShortDescription[Math
                                                  .floor(Math.random()
                                                          * $scope.randShortDescription.length)];

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