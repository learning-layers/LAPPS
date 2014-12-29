/**
 * @class uploadPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for uploading apps
 */
(function() {
  angular.module('lappsControllers').controller('uploadPageCtrl',
          ['$scope', '$http', function($scope, $http) {

            var counter = 0;

            var currentIndex = 0;

            $scope.images = [{
              id: 0,
              buttonName: "add",
              buttonTitle: 'Add'
            }];

            $scope.videolink = "http://www.youtube.com/embed/oHg5SJYRHA0";

            $scope.tags = " ";

            $scope.platforms = [{
              id: 1,
              name: 'Windows'
            }, {
              id: 2,
              name: 'Linux'
            }, {
              id: 3,
              name: 'Mac OS'
            }, {
              id: 4,
              name: 'IOS'
            }, {
              id: 5,
              name: 'Android'
            }, {
              id: 6,
              name: 'WindowsPhone'
            }, {
              id: 7,
              name: 'Others'
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
                  currentIndex = $scope.images.indexOf($scope.images[i]);
                }
              }
            };

          }]);

}).call(this);