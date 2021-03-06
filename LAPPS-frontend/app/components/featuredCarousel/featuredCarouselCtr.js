﻿/**
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
                      'platform',
                      function($scope, swaggerApi, platform) {

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
                        $scope.interval = 7000;
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
                         * @description Keeps track of the current slide index.
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

                        var apiParams = {
                          page: 1,
                          pageLength: 5,
                          sortBy: 'random',
                          filterBy: 'minrating',
                          filterValue: '3'
                        };
                        if (platform.currentPlatform.isAllPlatforms !== true) {
                          apiParams.filterBy += ';platform';
                          apiParams.filterValue += ';'
                                  + platform.currentPlatform.name;
                        }

                        swaggerApi.apps
                                .getAllApps(apiParams)
                                .then(
                                        function(response) {
                                          $scope.apps = response.data;
                                          for (var i = 0; i < $scope.apps.length; i++) {
                                            var image = '';
                                            var thumbnail = '';
                                            for (var j = 0; j < $scope.apps[i].artifacts.length; j++) {
                                              if ($scope.apps[i].artifacts[j].type
                                                      .indexOf('thumb') >= 0) {
                                                thumbnail = $scope.apps[i].artifacts[j].url;
                                              } else if ($scope.apps[i].artifacts[j].type
                                                      .indexOf('image') >= 0) {
                                                image = $scope.apps[i].artifacts[j].url;
                                                break;
                                              }
                                            }
                                            $scope.apps[i].image = image;
                                            $scope.apps[i].thumbnail = thumbnail;
                                            $scope.apps[i].platformObj = platform
                                                    .getPlatformByName($scope.apps[i].platform);
                                          }
                                        });
                      }]);
}).call(this);