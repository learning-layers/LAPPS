/**
 * @class featuredListCtr
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying a list of featured
 *              apps.
 */
(function() {
  angular.module('lappsControllers').controller('featuredListCtrl',
          ['$scope', '$http', function($scope, $http) {

            $scope.currentTab = '';
            $scope.apps = [];

            $scope.setTab = function(tab) {
              if ((tab == 'top' || tab == 'new') && $scope.currentTab != tab) {
                $scope.currentTab = tab;
                $scope.getApps(tab);
              }
            }
            $scope.getApps = function(tab) {
              $http.get('assets/dummy/appList.json').success(function(data) {
                $scope.apps = data;
                if (tab == 'new') {
                  $scope.apps.sort(function(a, b) {
                    return b.created - a.created;
                  });
                } else {
                  $scope.apps.sort(function(a, b) {
                    return b.rating - a.rating;
                  });
                }
              });

            }

            $scope.setTab('new');

          }]);
}).call(this);