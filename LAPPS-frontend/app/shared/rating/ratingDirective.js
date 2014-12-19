/**
 * @class starRating
 * @memberOf lapps.lappsDirectives
 * @description This directive is a template for the header.
 */
(function() {
  angular.module('lappsDirectives').directive('starRating', function() {
    return {
      restrict: 'E',
      templateUrl: 'shared/rating/ratingView.html',
      link: function(scope, iElement, iAttrs, ctrl) {
        iAttrs.$observe('value', function(value) {
          scope.value = window.Math.ceil(parseFloat(value));
        });
      }
    };
  });
}).call(this);