/**
 * @class tooltip
 * @memberOf lapps.lappsDirectives
 * @description This directive is used for showing tooltips
 */
(function() {
  angular.module('lappsDirectives').directive('tooltip', function() {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        $(element).hover(function() {
          // on mouseenter
          $(element).tooltip('show');
        }, function() {
          // on mouseleave
          $(element).tooltip('hide');
        });
      }
    };
  })
}).call(this);