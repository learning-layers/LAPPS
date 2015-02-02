/**
 * @class deleteConfirmCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for a modal delete confirmation
 *              dialog
 */
(function() {
  angular.module('lappsControllers').controller('deleteConfirmCtrl',
          ['$scope', '$modalInstance', function($scope, $modalInstance) {

            $scope.ok = function() {
              $modalInstance.close(true);
            };

            $scope.cancel = function() {
              $modalInstance.dismiss('cancel');
            };
          }]);
}).call(this);