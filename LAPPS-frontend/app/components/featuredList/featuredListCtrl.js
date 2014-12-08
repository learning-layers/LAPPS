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