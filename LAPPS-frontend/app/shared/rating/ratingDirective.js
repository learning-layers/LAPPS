/**
 * @class starRating
 * @memberOf lapps.lappsDirectives
 * @description This directive is shows the rating of an app.
 */
(function() {
  angular.module('lappsDirectives').directive('starRating', function() {
    return {
      restrict: 'E',
      templateUrl: 'shared/rating/ratingView.html',
      link: function(scope, iElement, iAttrs, ctrl) {
        iAttrs.$observe('value', function(value) {

          var f = parseFloat(value);
          if (f > 5) {
            f = 5;
          } else if (f < 0) {
            f = 0;
          }
          scope.value = window.Math.ceil(f);
        });
      }
    };
  });
}).call(this);