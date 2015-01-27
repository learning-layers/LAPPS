/**
 * @class applyAsDevPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for the application form for
 *              users, who want to become developers.
 */
(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'applyAsDevPageCtrl',
                  [
                      '$scope',
                      'user',
                      '$modal',
                      '$location',
                      'swaggerApi',
                      function($scope, user, $modal, $location, swaggerApi) {
                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.applyAsDevPageCtrl
                         * @description This text of the input field is sent to
                         *              the administrator via email.
                         */
                        $scope.applicationMessage = '';

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.applyAsDevPageCtrl
                         * @description Submits the message
                         *              {@link lapps.lappsControllers.applyAsDevPageCtrl.$scope.applicationMessage}
                         *              with user data (name, email) to the
                         *              backend, so that it can be forwarded as
                         *              an email to the administrator.
                         */
                        $scope.submit = function() {
                          if ($scope.applicationMessage.trim().length > 0) {

                            swaggerApi.users
                                    .apply({
                                      accessToken: user.token,
                                      oidcId: +user.data.oidcId,
                                      body: $scope.applicationMessage
                                    })
                                    .then(
                                            function(response) {

                                              if (response.status == 200) {
                                                var modalInstance = $modal
                                                        .open({
                                                          templateUrl: 'components/applyAsDevPage/submitConfirmView.html',
                                                          controller: 'submitConfirmCtrl',
                                                          size: 'xs',
                                                        });
                                                modalInstance.result
                                                        .then(
                                                                function(isOk) {
                                                                  $location
                                                                          .path('/apps');
                                                                }, function() {
                                                                });

                                              } else {
                                                var modalInstance = $modal
                                                        .open({
                                                          templateUrl: 'components/applyAsDevPage/errorNotificationView.html',
                                                          controller: 'submitConfirmCtrl',
                                                          size: 'xs',
                                                        });
                                              }

                                            });
                          }

                        }
                      }]);
}).call(this);