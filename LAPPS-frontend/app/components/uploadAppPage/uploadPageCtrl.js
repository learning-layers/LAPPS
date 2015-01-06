/**
 * @class uploadPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for uploading apps
 */
(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'uploadPageCtrl',
                  [
                      '$scope',
                      '$http',
                      function($scope, $routeParams, $http) {
                        // app tags
                        this.appTags = "";

                        var counter = 0;

                        var currentIndex = 0;

                        $scope.newFields = [{
                          id: 0,
                          buttonName: 'add',
                          buttonTitle: 'Add'
                        }];

                        this.videoUrl = "";

                        $scope.images = [];

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

                        $scope.newapp = {
                          artifacts: [],
                          tags: []
                        };

                        $scope.createNewApp = function() {

                          $scope.$broadcast('show-errors-check-validity');

                          if ($scope.uploadForm.$valid) {
                            // split tags
                            $scope.newapp.tags = $scope.appTags.split(',');
                            // create new artifacts from each image url
                            for (var i = 0; i < $scope.images.length; i++) {
                              $scope.newapp.artifacts.push({
                                url: $scope.images[i],
                                type: 'image'
                              })
                            }
                            // create another artifact for video
                            $scope.newapp.artifacts.push({
                              url: $scope.videoUrl,
                              type: 'video'
                            })

                            alert('App Saved');
                            $scope.reset();
                            // create json for new app
                            var data = $scope.newapp;
                          }

                        };

                        $scope.reset = function() {
                          $scope.$broadcast('show-errors-reset');
                          $scope.newapp = {
                            id: ' ', // have to get latest id?
                            name: ' ',
                            platform: ' ',
                            supportedPlatformVersion: ' ',
                            downloadUrl: ' ',
                            version: ' ',
                            sizeKB: 0,
                            sourceUrl: ' ',
                            supportUrl: ' ',
                            developer: {
                              name: ' ',
                              oidcId: 0
                            },
                            rating: 0,
                            tags: [],
                            dateCreated: ' ',
                            dateModified: ' ',
                            licence: ' ',
                            shortDescription: ' ',
                            longDescription: ' ',
                            thumbnail: ' ',
                            artifacts: []

                          };
                        }

                        $scope.addAnotherField = function($event) {
                          if ($event.currentTarget.name == 'add') {
                            counter++;
                            $scope.newFields.push({
                              id: counter,
                              buttonName: 'remove' + counter,
                              buttonTitle: 'Remove'
                            });
                          } else {
                            $scope.findElementInArray($event.currentTarget.id);
                            $scope.newFields.splice(currentIndex, 1);
                          }
                          $event.preventDefault();
                        };

                        $scope.findElementInArray = function(index) {
                          for (var i = 0; i < $scope.newFields.length; i++) {
                            if ($scope.newFields[i].id == index) {
                              currentIndex = $scope.newFields
                                      .indexOf($scope.newFields[i]);
                            }
                          }
                        };

                      }]);

}).call(this);