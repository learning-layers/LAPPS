/**
 * @class tags
 * @memberOf lapps.lappsDirectives
 * @description This directive shows tags of an app.
 */
(function() {
  angular.module('lappsDirectives').directive('tags', function() {
    return {
      restrict: 'E',
      templateUrl: 'shared/tags/tagsView.html',
      link: function(scope, iElement, iAttrs, ctrl) {
        scope.minLimit = +iAttrs.min || 5;
        scope.noLink = false;
        if (iAttrs.nolink) {
          scope.noLink = iAttrs.nolink;
        }
        scope.tagLimit = scope.minLimit;
        scope.maxLimit = +iAttrs.max || 20;
        iAttrs.$observe('value', function(value) {
          if (value) {
            scope.tags = JSON.parse(value);
          }
        });
      }
    };
  });
}).call(this);