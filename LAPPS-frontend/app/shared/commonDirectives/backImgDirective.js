/**
 * @class backImg
 * @memberOf lapps.lappsDirectives
 * @description This directive works similar to ng-src on background-image urls
 */

(function() {
  angular.module('lappsDirectives').directive('backImg', function() {
    return function(scope, element, attrs) {
      attrs.$observe('backImg', function(value) {
        element.css({
          'background-image': 'url(' + value + ')',
          'background-size': 'cover'
        });
      });
    };
  });
}).call(this);