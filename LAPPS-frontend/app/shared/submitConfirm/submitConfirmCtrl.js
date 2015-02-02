/**
 * @class submitConfirmCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for a modal submit confirmation
 *              dialog
 */
(function() {
  angular.module('lappsControllers').controller('submitConfirmCtrl',
          ['$scope', '$modalInstance', function($scope, $modalInstance) {

            $scope.ok = function() {
              $modalInstance.close(true);
            };

          }]);
}).call(this);