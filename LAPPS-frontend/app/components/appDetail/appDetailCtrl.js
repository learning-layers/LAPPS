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
                      '$sce',
                      '$timeout',
                      function($scope, $routeParams, swaggerApi, platform,
                              $document, convert, $timeout, $location, $modal,
                              user, md5, $sce, $timeout) {
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
                         * @type boolean
                         * @description True if the visitor is an admin.
                         */
                        $scope.isAdmin = function() {
                          return user.isAdmin();
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @type boolean
                         * @description True if the visitor is logged in
                         */
                        $scope.mayComment = function() {
                          return user.signedIn;
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
                              }).then($location.path('/apps'));
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
                          $scope.stopVideo();
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

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Stops currently playing video
                         */
                        $scope.stopVideo = function() {

                          var iframe = document.getElementsByTagName("iframe")[0];
                          if (iframe) {
                            iframe = iframe.contentWindow;
                          }
                          if (iframe) {

                            iframe.postMessage('{"event":"command","func":"'
                                    + 'pauseVideo' + '","args":""}', '*');
                          }
                        }
                        var entityMap = {
                          '&': '&amp;',
                          '<': '&lt;',
                          '>': '&gt;',
                          '"': '&quot;',
                          "'": '&#39;',
                          '/': '&#x2F;'
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
                                                        url: $scope.app.artifacts[j].url
                                                                + ($scope.app.artifacts[j].url
                                                                        .indexOf('?') > 0
                                                                        ? '&'
                                                                        : '?')
                                                                + 'enablejsapi=1',
                                                        description: $scope.app.artifacts[j].description
                                                      });
                                            }
                                          }

                                          if (images.length <= 0) { // to
                                            // display
                                            // placeholder
                                            // image if
                                            // no images
                                            // available
                                            images.push(null);
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

                        $timeout(function() {
                          $scope.requestTimedOut = true
                        }, 500);

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
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Stores the current rating the user wants
                         *              to give an app.
                         */
                        $scope.currentRating = 3;
                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Stores the current comment text the user
                         *              wants to give an app.
                         */
                        $scope.currentComment = '';
                        /**
                         * @field
                         * @type comment[]
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Stores the currently displayed comments.
                         */
                        $scope.comments = [];
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Stores the current page of comments.
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
                         *              comments.
                         */
                        $scope.maxPage = 5;
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Max amount of pages to display in the
                         *              page selector.
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
                          $scope.getComments();
                          $('html, body').animate({
                            scrollTop: ($('#comments').first().offset().top)
                          }, 500);

                        }

                        $scope.loadOwnComment = function() {
                          if (!user.signedIn) { return; }

                          swaggerApi.apps.getAllComments({
                            appId: +$scope.appId,
                            page: $scope.currentPage,
                            pageLength: 10,
                            filterBy: 'creator',
                            filterValue: user.data.oidcId

                          }).then(function(response) {

                            if (response.data.length == 1) {// has already a
                              // comment
                              $scope.currentComment = response.data[0].content;
                              $scope.currentRating = response.data[0].rating;
                            }
                          });
                        }
                        var showCommentSubmitConfirmation = function() {
                          var modalInstance = $modal
                                  .open({
                                    templateUrl: 'components/appDetail/submitConfirmView.html',
                                    controller: 'submitConfirmCtrl',
                                    size: 'xs',
                                  });
                          modalInstance.result.then(function(isOk) {
                            if (isOk) {
                              $scope.changePage(1);
                            }
                          }, function() {
                          });
                        }
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Submits a comment to the backend.
                         */
                        $scope.submitComment = function() {
                          if (!user.signedIn) { return; }
                          swaggerApi.apps.getAllComments({
                            appId: +$scope.appId,
                            page: $scope.currentPage,
                            pageLength: 1,
                            filterBy: 'creator',
                            filterValue: user.data.oidcId

                          }).then(function(response) {

                            if (response.data.length == 0)// empty = first
                            // comment for this
                            // app
                            {
                              swaggerApi.apps.createComment({
                                accessToken: user.token,
                                appId: +$scope.appId,
                                body: {
                                  "id": 0,
                                  "content": $scope.currentComment,
                                  "rating": $scope.currentRating,
                                  "updateDate": "",
                                  "releaseDate": "",
                                  "user": {
                                    "oidcId": +user.data.oidcId,
                                    "email": "",
                                    "username": "",
                                    "role": 0,
                                    "dateRegistered": "",
                                    "description": "",
                                    "website": ""
                                  }
                                }
                              }).then(function(response) {
                                showCommentSubmitConfirmation();
                              });
                            } else { // update existing comment
                              swaggerApi.apps.updateComment({
                                accessToken: user.token,
                                appId: +$scope.appId,
                                id: +response.data[0].id,
                                body: {
                                  "id": 0,
                                  "content": $scope.currentComment,
                                  "rating": $scope.currentRating,
                                  "updateDate": "",
                                  "releaseDate": "",
                                  "user": {
                                    "oidcId": +user.data.oidcId,
                                    "email": "",
                                    "username": "",
                                    "role": 0,
                                    "dateRegistered": "",
                                    "description": "",
                                    "website": ""
                                  }
                                }
                              }).then(function(response) {
                                showCommentSubmitConfirmation();
                              });
                            }

                          });
                        }
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @param {string}
                         *          id comment id
                         * @description Deletes a a selected comment.
                         */
                        $scope.deleteComment = function(id) {
                          if (!user.signedIn) { return; }
                          var modalInstance = $modal
                                  .open({
                                    templateUrl: 'components/appDetail/deleteCommentConfirmView.html',
                                    controller: 'deleteConfirmCtrl',
                                    size: 'xs',
                                  });
                          modalInstance.result.then(function(isOk) {
                            if (isOk) {
                              swaggerApi.apps.deleteComment({
                                accessToken: user.token,
                                appId: +$scope.appId,
                                id: +id,
                              }).then(function(response) {
                                $scope.getComments();
                              });
                            }
                          }, function() {
                          });

                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Fetches all comments for the respective
                         *              app.
                         */
                        $scope.getComments = function() {
                          $scope.comments = [];

                          swaggerApi.apps
                                  .getAllComments({
                                    appId: +$scope.appId,
                                    page: $scope.currentPage,
                                    pageLength: 10,
                                    order: 'desc'
                                  })
                                  .then(
                                          function(response) {
                                            $scope.comments = response.data;
                                            $scope.maxPage = +response
                                                    .headers('numberOfPages');
                                            for (var i = 0; i < $scope.comments.length; i++) {
                                              $scope.comments[i].dateCreatedConverted = convert
                                                      .dateTime($scope.comments[i].releaseDate);
                                              $scope.comments[i].dateModifiedConverted = convert
                                                      .dateTime($scope.comments[i].updateDate);
                                              $scope.comments[i].avatar = 'https://s.gravatar.com/avatar/'
                                                      + md5
                                                              .createHash($scope.comments[i].user.email
                                                                      .trim()
                                                                      .toLowerCase()
                                                                      || $scope.comments[i].user.username)
                                                      + '?s='
                                                      + 60
                                                      + '&d=identicon';
                                            }

                                          });
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

                        $scope.getComments();

                        if (!user.token) {// token not yet set

                          $timeout(function() {
                            $scope.loadOwnComment();
                          }, 450);
                        } else {
                          $scope.loadOwnComment();
                        }
                      }]);
}).call(this);