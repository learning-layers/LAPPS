/**
 * @class userPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying the profile of
 *              users.
 */
(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'editPageCtrl',
                  [
                      '$scope',
                      '$filter',
                      '$routeParams',
                      'user',
                      '$http',
                      'swaggerApi',
                      'md5',
                      function($scope, $filter, $routeParams, user, $http,
                              swaggerApi, md5) {

                        $scope.appId = $routeParams.appId;

                        $scope.appTags = [];

                        $scope.app = {};

                        $scope.size = '';

                        $scope.markdownLongDescription = '';

                        $scope.creator = {};

                        this.platforms = [
                            {
                              id: 0,
                              name: 'iOS',
                              icon: 'fa-apple',
                              agentRegEx: /(iPhone|iPad|iPod)/
                            },
                            {
                              id: 1,
                              name: 'Android',
                              icon: 'fa-android',
                              agentRegEx: /Android/
                            },
                            {
                              id: 2,
                              name: 'Windows Phone',
                              icon: 'fa-windows',
                              agentRegEx: /Windows Phone/
                            },
                            {
                              id: 3,
                              name: 'Web Apps',
                              icon: 'fa-globe',
                            },
                            {
                              id: 4,
                              name: 'Windows',
                              icon: 'fa-windows',
                              agentRegEx: /(Windows NT|Windows 2000|Windows XP|Windows 7|Windows 8|Windows 10)/
                            }, {
                              id: 5,
                              name: 'Linux',
                              icon: 'fa-linux',
                              agentRegEx: /(Linux|X11)/
                            }, {
                              id: 6,
                              name: 'Mac OS X',
                              icon: 'fa-apple',
                              agentRegEx: /Mac OS X/
                            }, {
                              id: 7,
                              name: 'All Platforms',
                              icon: 'fa-circle-o',
                            }];

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

                        $scope.expandCollapseDescription = function() {
                          if ($scope.collapsed) {
                            $scope.collapsed = false;
                          } else {
                            $scope.collapsed = true;
                          }
                        }

                        $scope.fetchApp = function() {
                          // TODO: sync with actual user

                          swaggerApi.apps
                                  .getApp({
                                    id: +$scope.appId,
                                  })
                                  .then(
                                          function(response) {

                                            $scope.app = response.data;
                                            $scope.dateCreated = $scope
                                                    .convertDate($scope.app.dateCreated);
                                            $scope.dateModified = new Date(
                                                    $scope
                                                            .convertDate($scope.app.dateModified));
                                            var tags = [];
                                            for (var i = 0; i < $scope.app.tags.length; i++) {
                                              tags
                                                      .push($scope.app.tags[i].value);
                                            }
                                            $scope.appTags = tags.join();
                                            var thumbnail = {};
                                            var images = [];
                                            var video = {
                                              url: '',
                                              description: '',
                                              type: ''
                                            };
                                            for (var j = 0; j < $scope.app.artifacts.length; j++) {
                                              if ($scope.app.artifacts[j].type
                                                      .indexOf('thumb') >= 0) {
                                                thumbnail.url = $scope.app.artifacts[j].url;
                                                thumbnail.description = $scope.app.artifacts[j].description;
                                                thumbnail.type = 'thumb';
                                              } else if ($scope.app.artifacts[j].type
                                                      .indexOf('image') >= 0) {
                                                images
                                                        .push({
                                                          url: $scope.app.artifacts[j].url,
                                                          description: $scope.app.artifacts[j].description,
                                                          type: 'image'
                                                        });
                                              } else if ($scope.app.artifacts[j].type
                                                      .indexOf('video') >= 0) {
                                                video.url = $scope.app.artifacts[j].url;
                                                video.description = $scope.app.artifacts[j].description;
                                                video.type = 'video';
                                              }
                                              ;
                                            }

                                            $scope.size = $scope.app.size;

                                            $scope.thumbnail = thumbnail;
                                            $scope.images = images;
                                            $scope.video = video;

                                            $scope.creator = $scope.app.creator;
                                          });
                        }

                        $scope.showPlatform = function() {

                          var selected = $filter('filter')($scope.platforms, {
                            name: $scope.app.platform
                          });
                          $scope.chosenPlatform = selected[0].name;
                          return ($scope.app.platform && selected.length)
                                  ? selected[0].name : 'Not set';

                        };

                        $scope.validateInput = function(data) {
                          if (!data) { return 'Input required'; }
                        }

                        $scope.sendAppData = function() {

                          $scope.app.artifacts = [];
                          for (var i = 0; i < $scope.images.length; i++) {
                            $scope.app.artifacts.push($scope.images[i]);
                          }
                          $scope.platform = $scope.chosenPlatform;
                          $scope.app.artifacts.push($scope.video);
                          $scope.app.artifacts.push($scope.thumbnail);

                          var tempTags = [];
                          $scope.app.tags = [];
                          tempTags = $scope.appTags
                                  .match(/(?=\S)[^,]+?(?=\s*(,|$))/g);
                          for (var i = 0; i < tempTags.length; i++) {
                            $scope.app.tags.push({
                              value: tempTags[i]
                            });
                          }
                          $scope.app.dateCreated = $scope.app.dateCreated
                                  .toString();
                          $scope.app.dateModified = Date.parse(
                                  $scope.dateModified).toString();
                          ;

                          $scope.app.size = parseInt($scope.size);

                          $scope.app.creator.dateRegistered = $scope.app.creator.dateRegistered
                                  .toString();

                          // console.log($scope.app);
                          swaggerApi.apps.updateApp({
                            accessToken: 'test_token',
                            id: +$scope.appId,
                            body: $scope.app
                          }).then(function(response) {
                            alert('Success');
                          });

                        }

                        $scope.fetchApp();
                      }]);

}).call(this);
