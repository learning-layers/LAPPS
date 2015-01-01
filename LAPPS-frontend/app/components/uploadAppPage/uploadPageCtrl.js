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
                      function($scope, $http) {

                        $scope.selectedValue = "";

                        var counter = 0;

                        var currentIndex = 0;

                        $scope.images = [{
                          id: 0,
                          buttonName: "add",
                          buttonTitle: 'Add'
                        }];

                        $scope.videolink = "http://www.youtube.com/embed/oHg5SJYRHA0";

                        $scope.tags = " ";

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

                        $scope.createNewApp = function() {

                          console.log($scope.uploadForm.$valid);
                          // $scope.$broadcast('show-errors-check-validity');

                          if ($scope.uploadForm.$valid) {
                            alert('App Saved');
                            $scope.reset();
                          }
                          /* create json for new app */
                          var data = $scope.newapp;

                        };

                        $scope.reset = function() {
                          $scope.$broadcast('show-errors-reset');
                          $scope.newapp = {
                            name: '',
                            email: ''
                          };
                        }

                        $scope.addAnotherField = function($event) {
                          alert($event.currentTarget.id);
                          if ($event.currentTarget.name == "add") {
                            counter++;
                            $scope.images.push({
                              id: counter,
                              buttonName: 'remove' + counter,
                              buttonTitle: 'Remove'
                            });
                          } else {
                            $scope.findElementInArray($event.currentTarget.id);
                            $scope.images.splice(currentIndex, 1);
                          }
                          $event.preventDefault();
                        };

                        $scope.findElementInArray = function(index) {
                          for (var i = 0; i < $scope.images.length; i++) {
                            if ($scope.images[i].id == index) {
                              currentIndex = $scope.images
                                      .indexOf($scope.images[i]);
                            }
                          }
                        };

                      }]);

}).call(this);