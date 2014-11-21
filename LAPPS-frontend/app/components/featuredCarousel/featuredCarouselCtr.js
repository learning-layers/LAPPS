/**
 * @class featuredCarouselCtr
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying a carousel with
 *              slides for featured apps
 */
(function() {
  angular.module('lappsControllers').controller(
          'featuredCarouselCtr',
          [
              '$scope',
              '$http',
              function($scope, $http) {

                $scope.apps = [];

                $scope.interval = 4000;
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
                  $scope.apps = data.slice(0, 3);
                });
              }]);
}).call(this);