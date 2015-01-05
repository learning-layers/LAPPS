/**
 * @class appEditPage
 * @memberOf lapps.lappsDirectives
 * @description This directive is a template for editing the app
 */
(function() {
  angular.module('lappsDirectives').directive('editPahe', function() {
    return {
      scope: true,
      restrict: 'E',
      templateUrl: 'components/editAppPage/editPageView.html',
      controller: 'editPageCtrl'
    };
  });
}).call(this);