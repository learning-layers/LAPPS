/**
  * @class lappsHeader
  * @memberOf lapps.lappsDirectives
  * @description This directive is a template for the header.
  */
(function () {
  angular.module('lappsDirectives').directive('lappsHeader', function () {
    return {
      restrict: 'E',
      templateUrl: 'shared/header/headerView.html'
    };
  });
}).call(this);