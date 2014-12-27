/**
 * @class uploadPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for uploading apps
 */
(function() {
  angular.module('lappsControllers').controller('uploadPageCtrl',
          ['$scope', '$http', function($scope, $http) {

            $scope.items = [{
              'functionName': 'addAnother()',
              'buttonName': 'Add'
            }, {
              'functionName': 'addAnother()',
              'buttonName': 'Add'
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
              $scope.$broadcast('show-errors-check-validity');

              if ($scope.userForm.$valid) {
                alert('User saved');
                $scope.reset();
              }

              $scope.newapp.tags = $scope.tags.split(',');
              /* create json for new app */
              var data = $scope.newapp;

            };

            $scope.removeCurrent = function() {

            };

            $scope.addAnother = function() {
              alert("Hecks to the yea!");
              $scope.items.push({
                'functionName': 'removeCurrent()',
                'buttonName': 'Remove'
              });
            };
          }]);
}).call(this);