/**
 * @class appListCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying a list of
 *              available apps.
 */
(function() {
  angular.module('lappsControllers').controller(
          'featuredCarouselCtr',
          [
              '$scope',
              '$http',
              function($scope, $http) {
                /**
                 * @field
                 * @type {appListItem[]}
                 * @memberOf lapps.lappsControllers.appListCtrl
                 * @description This array stores a list of available apps with
                 *              a short description to display.
                 */
                $scope.apps = [];

                $scope.currentSlide = 0;
                $scope.onSlideChanged = function(nextSlide, direction) {

                  if (direction == 'next') {
                    $scope.currentSlide = ($scope.currentSlide + 1)
                            % $scope.apps.length;
                  } else if (direction == 'prev') {
                    $scope.currentSlide = ($scope.currentSlide - 1)
                            % $scope.apps.length;
                  }
                };

                $http.get('assets/dummy/appList.json').success(function(data) {
                  $scope.apps = data;
                });
              }]);
}).call(this);