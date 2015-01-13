/**
 * @class errorSrc
 * @memberOf lapps.lappsDirectives
 * @description This directive provides a fallback src, if an image src could
 *              not be loaded.
 */

(function() {
  angular.module('lappsDirectives').directive('errorSrc', function() {
    return {
      link: function(scope, element, attrs) {
        element.bind('error', function() {
          if (attrs.src != attrs.errorSrc) {
            attrs.$set('src', attrs.errorSrc);
          }
        });
      }
    }
  });
}).call(this);