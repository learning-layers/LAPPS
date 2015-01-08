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
                      'swaggerApi',
                      'user',
                      function($scope, $http, swaggerApi, user) {

                        $scope.userId = user.oidcData.clientId;

                        $scope.newapp = {
                          "id": 0,
                          "name": "",
                          "platform": "",
                          "minPlatformRequired": "",
                          "downloadUrl": "",
                          "version": "",
                          "size": 0,
                          "sourceUrl": "",
                          "supportUrl": "",
                          "rating": 0,
                          "dateCreated": "",
                          "dateModified": "",
                          "license": "",
                          "shortDescription": "",
                          "longDescription": "",
                          "creator": {
                            "oidcId": 0,
                            "email": "",
                            "username": "",
                            "role": 0,
                            "dateRegistered": "",
                            "description": "",
                            "website": ""
                          },
                          "artifacts": [],
                          "tags": []
                        }
                        // app tags
                        $scope.tags = {};

                        $scope.size = {};

                        var counter = 0;

                        var currentIndex = 0;

                        $scope.dateModified = '';

                        $scope.newFields = [{
                          id: 0,
                          buttonName: 'add',
                          buttonTitle: 'Add'
                        }];

                        $scope.video = {};

                        $scope.thumbnail;

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

                        $scope.fetchUserData = function() {

                          swaggerApi.users
                                  .getUser({
                                    oidcId: +$scope.userId,
                                  })
                                  .then(
                                          function(response) {

                                            $scope.newapp.creator.iodcId = user.oidcData.clientId;
                                            $scope.currentUser = response.data;
                                            $scope.newapp.creator.email = $scope.currentUser.email;
                                            $scope.newapp.creator.username = $scope.currentUser.username;
                                            $scope.newapp.creator.role = user
                                                    .roleIdToRoleName($scope.currentUser.role);
                                            $scope.newapp.creator.memberScince = $scope.currentUser.dateRegistered;
                                            $scope.newapp.website = $scope.currentUser.website;

                                          });
                        }

                        $scope.createNewApp = function() {

                          // $scope.$broadcast('show-errors-check-validity');

                          if ($scope.uploadForm.$valid) {

                            // split and get rid of white spaces
                            console.log("tags:" + $scope.tags.value);
                            if ($scope.tags.value) {
                              var tempTags = [];
                              tempTags = $scope.tags.value
                                      .match(/(?=\S)[^,]+?(?=\s*(,|$))/g);

                              for (var i = 0; i < tempTags.length; i++) {
                                $scope.newapp.tags.push({
                                  id: $scope.hashCode(tempTags[i]),
                                  value: tempTags[i]
                                });
                              }
                            }

                            // create new artifacts from each image url

                            for (var i = 0; i < $scope.images.length; i++) {
                              $scope.newapp.artifacts.push({
                                url: $scope.images[i].url,
                                description: $scope.images[i].description,
                                type: 'image'
                              })
                            }
                            // create another artifact for video
                            if ($scope.video.url) {
                              $scope.newapp.artifacts.push({
                                url: $scope.video.url,
                                description: $scope.video.description,
                                type: 'video'
                              })
                            }
                            // create another artifact for thumbnail
                            $scope.newapp.artifacts.push({
                              url: $scope.thumbnail,
                              description: '',
                              type: 'thumb'
                            })
                            if ($scope.dateModified) {
                              $scope.newapp.dateModified = Date.parse(
                                      $scope.dateModified).toString();
                            }

                            $scope.newapp.size = parseInt($scope.size.value);

                            // $scope.fetchUserData();
                            console.log($scope.newapp);

                            // post json
                            swaggerApi.apps.createApp({
                              accessToken: 'test_token',
                              body: $scope.newapp
                            });

                            // $scope.reset();
                          }

                        };

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
                        $scope.reset = function() {

                          $scope.newapp = {
                            "id": 0,
                            "name": "",
                            "platform": "",
                            "minPlatformRequired": "",
                            "downloadUrl": "",
                            "version": "",
                            "size": 0,
                            "sourceUrl": "",
                            "supportUrl": "",
                            "rating": 0,
                            "dateCreated": "",
                            "dateModified": "",
                            "license": "",
                            "shortDescription": "",
                            "longDescription": "",
                            "creator": {
                              "oidcId": 0,
                              "email": "",
                              "username": "",
                              "role": 0,
                              "dateRegistered": "",
                              "description": "",
                              "website": ""
                            },
                            "artifacts": [],
                            "tags": []
                          }
                        }

                        $scope.hashCode = function(str) {
                          var hashValue = 0;
                          if (str.length == 0) return hashValue;
                          for (i = 0; i < str.length; i++) {
                            char = str.charCodeAt(i);
                            hashValue = ((hashValue << 5) - hashValue) + char;
                            hashValue = hashValue & hashValue;
                          }
                          console.log("hashvalue:" + hashValue);
                          return hashValue;
                        }

                      }]);

}).call(this);