/**
 * @class validation
 * @memberof lapps.lappsDirectives
 * @description This directive is used for validation of number of tags.
 */
(function() {
  angular.module('lappsDirectives').directive('checkTags', function($timeout) {
    return {
      restrict: 'A',
      require: '^form',
      link: function(scope, el, attrs) {

      }
    }
  });
}).call(this);