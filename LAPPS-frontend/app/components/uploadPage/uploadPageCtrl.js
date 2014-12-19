/**
 * @class uploadPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for uploading apps
 */
(function() {
  angular.module('lappsControllers').controller('uploadPageCtrl',
          ['$scope', '$http', function($scope, $http) {

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
              /* create json for new app */

              $scope.newapp.tags = $scope.tags.split(',');

              var data = $scope.newapp;

              /* post to server */
            }
          }]);
}).call(this);