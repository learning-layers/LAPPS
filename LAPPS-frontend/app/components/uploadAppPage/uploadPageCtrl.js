/**
 * @class uploadPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for uploading apps
 */
(function() {
  angular.module('lappsControllers').controller(
          'uploadPageCtrl',
          [
              '$scope',
              '$http',
              'swaggerApi',
              'user',
              'platform',
              function($scope, $http, swaggerApi, user, platform) {
                $scope.userId = 390532463;

                $scope.showImg = false;

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

                $scope.thumbnail = {};

                $scope.images = [];

                $scope.update = function() {
                  $scope.showImg = true;
                }

                this.platforms = platform.platforms;

                $scope.createNewApp = function() {
                  // $scope.$broadcast('show-errors-check-validity');

                  if ($scope.uploadForm.$valid) {
                    $scope.newapp.creator.oidcId = $scope.userId;

                    // $scope.fetchUserData();

                    // split and get rid of white spaces

                    if ($scope.tags.value) {
                      var tempTags = [];
                      tempTags = $scope.tags.value
                              .match(/(?=\S)[^,]+?(?=\s*(,|$))/g);

                      for (var i = 0; i < tempTags.length; i++) {
                        $scope.newapp.tags.push({
                          value: tempTags[i]
                        });
                      }
                    }

                    // create new artifacts from each image url

                    for (var i = 0; i < $scope.images.length; i++) {
                      $scope.newapp.artifacts.push({
                        url: $scope.images[i].url,
                        description: $scope.images[i].description,
                        type: 'image/png'
                      })
                    }
                    // create another artifact for video
                    if ($scope.video.url) {
                      $scope.newapp.artifacts.push({
                        url: $scope.video.url,
                        description: $scope.video.description,
                        type: 'video/youtube'
                      })
                    }
                    // create another artifact for thumbnail
                    $scope.newapp.artifacts.push({
                      url: $scope.thumbnail.url,
                      description: '',
                      type: 'thumbnail'
                    })
                    if ($scope.dateModified) {
                      $scope.newapp.dateModified = Date.parse(
                              $scope.dateModified).toString();
                    }

                    $scope.newapp.size = parseInt($scope.size.value);

                    // $scope.fetchUserData();
                    // console.log($scope.newapp);

                    // post json
                    swaggerApi.apps.createApp({
                      accessToken: 'test_token',
                      body: $scope.newapp
                    }).then(function() {
                      alert('done!')
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
              }]);
}).call(this);