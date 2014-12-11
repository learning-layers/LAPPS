/**
 * @class featuredCarouselCtr
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying a carousel with
 *              slides for featured apps
 */
(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'featuredCarouselCtr',
                  [
                      '$scope',
                      'swaggerApi',
                      function($scope, swaggerApi) {
                        /**
                         * @field
                         * @type app[]
                         * @memberOf lapps.lappsControllers.featuredCarouselCtr
                         * @description Stores the app objects retrieved from
                         *              the backend.
                         */
                        $scope.apps = [];
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.featuredCarouselCtr
                         * @description Time in ms to wait until the image in
                         *              the carousel is changed.
                         */
                        $scope.interval = 4000;
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.featuredCarouselCtr
                         * @description Stores the currently displayed slide id
                         *              in the carousel.
                         */
                        $scope.currentSlide = 0;

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.featuredCarouselCtr
                         * @param {object}
                         *          nextSlide The next slide in the carousel.
                         * @param {object}
                         *          direction The direction of the slide change.
                         * @description Converts size in KB to MB with 1 digit
                         *              after the comma. If size is less than
                         *              1024 the value is not changed. The
                         *              string MB or KB is appended to the
                         *              result (12.5 MB).
                         */
                        $scope.onSlideChanged = function(nextSlide, direction) {
                          if (direction == 'next') {
                            $scope.currentSlide = ($scope.currentSlide + 1)
                                    % $scope.apps.length;
                          } else if (direction == 'prev') {
                            $scope.currentSlide = ($scope.currentSlide - 1)
                                    % $scope.apps.length;
                            if ($scope.currentSlide < 0) {
                              $scope.currentSlide = $scope.apps.length - 1;
                            }
                          }

                          // fix for chrome redraw bug
                          $('.featured-info-panel').toggle();
                          $('.featured-info-panel').toggle();
                        };

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
                                .getAllApps()
                                .then(
                                        function(data) {
                                          $scope.apps = data.slice(0, 5);
                                          for (var i = 0; i < $scope.apps.length; i++) {
                                            var short = $scope.randShortDescription[Math
                                                    .floor(Math.random()
                                                            * $scope.randShortDescription.length)];
                                            $scope.apps[i].shortDescription = short;
                                            var long = '';
                                            for (var j = 0; j < 10; j++) {
                                              var item = $scope.randShortDescription[Math
                                                      .floor(Math.random()
                                                              * $scope.randShortDescription.length)];
                                              long += item + ' ';
                                            }
                                            $scope.apps[i].description = long;
                                            $scope.apps[i].images = [];
                                            for (var k = 0; k < $scope.apps[i].artifacts.length; k++) {
                                              var item = $scope.apps[i].artifacts[k];
                                              if (item.artifact.type
                                                      .indexOf('image') > -1) {
                                                $scope.apps[i].images
                                                        .push(item.url);
                                              }
                                            }
                                          }
                                        });
                      }]);
}).call(this);