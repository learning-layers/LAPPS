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
                      'platform',
                      '$document',
                      'convert',
                      '$timeout',
                      '$location',
                      '$modal',
                      'user',
                      'md5',
                      function($scope, $routeParams, swaggerApi, platform,
                              $document, convert, $timeout, $location, $modal,
                              user, md5) {
                        /**
                         * @field
                         * @type object
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Just gets the Math object into the scope
                         *              for use in bindings
                         */
                        $scope.Math = window.Math;
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
                         * @description Time in ms to wait until the image in
                         *              the carousel is changed.
                         */
                        $scope.interval = 7000;

                        /**
                         * @field
                         * @type app
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Stores the app object retrieved from the
                         *              backend.
                         */
                        $scope.app = {};

                        /**
                         * @field
                         * @type object
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Stores information about alternative
                         *              platforms for this app
                         */

                        $scope.alternativePlatforms = [];

                        /**
                         * @field
                         * @type boolean
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Set to true after a fixed period of
                         *              time. Used to show 'invalid-link'
                         *              notifications.
                         */
                        $scope.requestTimedOut = false;

                        if (marked) {
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
                        }

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
                         * @type boolean
                         * @description True if the visitor is the developer of
                         *              the app or an admin.
                         */
                        $scope.mayEdit = function() {
                          return (user.isAdmin() || (user.isDeveloper() && user.data.sub == $scope.app.creator.oidcId));
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Opens a modal confirmation dialog and
                         *              deletes the current app.
                         */
                        $scope.deleteApp = function() {
                          var modalInstance = $modal
                                  .open({
                                    templateUrl: 'components/appDetail/deleteConfirmView.html',
                                    controller: 'deleteConfirmCtrl',
                                    size: 'xs',
                                  });

                          modalInstance.result.then(function(isOk) {
                            if (isOk) {
                              swaggerApi.apps.deleteApp({
                                accessToken: user.token,
                                id: $scope.app.id
                              }).then($location.path("/apps"));
                            }
                          }, function() {
                          });
                        }
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Stores the currently displayed slide id
                         *              in the carousel.
                         */
                        $scope.currentSlide = 0;

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @param {object}
                         *          nextSlide The next slide in the carousel.
                         * @param {object}
                         *          direction The direction of the slide change.
                         * @description Keeps track of the current slide index.
                         */
                        $scope.onSlideChanged = function(nextSlide, direction) {
                          if (direction == 'next') {
                            $scope.currentSlide = ($scope.currentSlide + 1)
                                    % ($scope.app.images.length + $scope.app.videos.length);
                          } else if (direction == 'prev') {
                            $scope.currentSlide = ($scope.currentSlide - 1)
                                    % ($scope.app.images.length + $scope.app.videos.length);
                            if ($scope.currentSlide < 0) {
                              $scope.currentSlide = ($scope.app.images.length + $scope.app.videos.length) - 1;
                            }
                          }
                        }
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Stops the automatic carousel rotation
                         */
                        $scope.stopCarousel = function() {
                          $scope.interval = -1;
                        }

                        var entityMap = {
                          "&": "&amp;",
                          "<": "&lt;",
                          ">": "&gt;",
                          '"': '&quot;',
                          "'": '&#39;',
                          "/": '&#x2F;'
                        };

                        function escapeHtml(string) {
                          return String(string).replace(/[&<>"'\/]/g,
                                  function(s) {
                                    return entityMap[s];
                                  });
                        }
                        swaggerApi.apps
                                .getApp({
                                  id: +$scope.appId
                                })
                                .then(
                                        function(response) {
                                          $scope.app = response.data;
                                          if (marked) {
                                            $scope.app.longDescriptionMarkdown = marked($scope.app.longDescription);
                                          } else {// since marked cannot safely
                                            // escape malicius html and
                                            // angularjs is told to accept
                                            // the output, escape it
                                            // explicitly
                                            $scope.app.longDescriptionMarkdown = escapeHtml($scope.app.longDescription);
                                          }

                                          var thumbnail = '';
                                          var images = [];
                                          var videos = [];
                                          // videos.push({ url:
                                          // '//www.youtube.com/embed/tHwntRpLobU',
                                          // description: 'Maru' });
                                          for (var j = 0; j < $scope.app.artifacts.length; j++) {
                                            if ($scope.app.artifacts[j].type
                                                    .indexOf('thumb') >= 0) {
                                              thumbnail = $scope.app.artifacts[j].url;
                                            } else if ($scope.app.artifacts[j].type
                                                    .indexOf('image') >= 0) {
                                              images
                                                      .push({
                                                        url: $scope.app.artifacts[j].url,
                                                        description: $scope.app.artifacts[j].description
                                                      });
                                            } else if ($scope.app.artifacts[j].type
                                                    .indexOf('video') >= 0) {
                                              videos
                                                      .push({
                                                        url: $scope.app.artifacts[j].url,
                                                        description: $scope.app.artifacts[j].description
                                                      });
                                            }
                                          }

                                          $scope.app.thumbnail = thumbnail;
                                          $scope.app.images = images;
                                          $scope.app.videos = videos;
                                          $scope.app.platformObj = platform
                                                  .getPlatformByName($scope.app.platform);
                                          $scope.app.sourceUrlShort = convert
                                                  .url($scope.app.sourceUrl);
                                          $scope.app.supportUrlShort = convert
                                                  .url($scope.app.supportUrl);

                                          $scope.size = convert
                                                  .size($scope.app.size);

                                          $scope.dateCreated = convert
                                                  .date($scope.app.dateCreated);
                                          $scope.dateModified = convert
                                                  .date($scope.app.dateModified);
                                        });
                        swaggerApi.apps.getAllPlatformsForApp({
                          appId: +$scope.appId
                        }).then(function(response) {
                          for (var i = 0; i < response.data.length; i++) {
                            if ($scope.appId != response.data[i].id) {
                              $scope.alternativePlatforms.push({
                                id: response.data[i].id,
                                platform: response.data[i].platform
                              });
                            }
                          }

                          if ($scope.alternativePlatforms.length <= 0) {
                            $scope.alternativePlatforms.push({
                              id: $scope.appId,
                              platform: '-'
                            });
                          } else {
                            $scope.alternativePlatforms.sort(comparePlaftorms);
                          }
                        });

                        $scope.comments = [];
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Stores the current page of comments
                         */
                        $scope.currentPage = 1;
                        if ($routeParams.page) {
                          $scope.currentPage = +$routeParams.page;
                        }
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Stores the amount of pages for the
                         *              comments
                         */
                        $scope.maxPage = 10;
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Max amount of pages to display in the
                         *              page selector
                         */
                        $scope.maxDisplayPage = 6;

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Changes the current page and requests
                         *              the comments of this page.
                         */
                        $scope.changePage = function(pageNumber) {

                          $scope.currentPage = +pageNumber;
                          $location.search('page', pageNumber);
                          // TODO: api call
                        }

                        $scope.submitComment = function() {
                          // TODO: implement api call
                          var modalInstance = $modal
                                  .open({
                                    templateUrl: 'components/appDetail/submitConfirmView.html',
                                    controller: 'submitConfirmCtrl',
                                    size: 'xs',
                                  });
                        }
                        $scope.getComments = function() {
                          for (i = 0; i < 15; i++) {
                            var comment = {
                              user: {
                                oidcId: -1556155057,
                                email: 'kayleefrye@test.foobar',
                                username: 'Kaylee Frye'
                              },
                              dateCreated: 1420888318000,
                              dateModified: 1420888336000,
                              dateCreatedConverted: convert
                                      .dateTime(1420888318000),
                              dateModifiedConverted: convert
                                      .dateTime(1420888336000),
                              rating: 3,
                              content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse hendrerit turpis eu tellus vulputate iaculis. Donec non diam iaculis, eleifend justo eget, vulputate lacus. Mauris nec eros ac lectus ultrices ultrices eu ultricies purus. Suspendisse sodales efficitur purus, quis iaculis erat imperdiet id. Vivamus nisi mauris, eleifend feugiat egestas ac.'
                            }
                            comment.avatar = 'https://s.gravatar.com/avatar/'
                                    + md5.createHash(comment.user.email.trim()
                                            .toLowerCase()
                                            || comment.user.username) + '?s='
                                    + 60 + '&d=identicon';

                            $scope.comments.push(comment);
                          }

                          $scope.currentRating = 3;
                          $scope.currentComment = '';
                        }

                        // important to get click events from iframes:
                        // when starting a video: stop carousel
                        window.iFrameVideoLoaded = function() {
                          var iFrameChildren = $document.find('iframe')
                                  .contents();
                          iFrameChildren.on('click', $scope.stopCarousel);
                        }
                        /**
                         * @function
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @param {object}
                         *          a
                         * @param {object}
                         *          b
                         * @description Compares the names of two platforms
                         *              (case insensitive). Used for sorting.
                         */
                        function comparePlaftorms(a, b) {
                          if (a.platform.toLowerCase() < b.platform
                                  .toLowerCase()) return -1;
                          if (a.platform.toLowerCase() > b.platform
                                  .toLowerCase()) return 1;
                          return 0;
                        }

                        $timeout(function() {
                          $scope.requestTimedOut = true
                        }, 500);
                        $scope.getComments();
                      }]);
}).call(this);