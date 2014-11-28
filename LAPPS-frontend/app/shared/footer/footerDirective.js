/**
 * @class lappsHeader
 * @memberOf lapps.lappsDirectives
 * @description This directive is a template for the header.
 */
(function() {
  angular.module('lappsDirectives').directive('lappsFooter', function() {
    return {
      restrict: 'E',
      templateUrl: 'shared/footer/footerView.html'
    };
  });
}).call(this);