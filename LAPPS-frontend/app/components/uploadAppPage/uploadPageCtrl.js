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