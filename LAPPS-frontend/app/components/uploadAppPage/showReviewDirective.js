/**
 * @class showImage
 * @memberOf lapps.lappsDirectives
 * @description This directive is used for managing the template of uploadPage
 */
(function() {
  angular.module('lappsDirectives').directive('showReview', function() {
    return {
      restrict: 'A',
      require: 'ngModel',
      priority: 1,
      link: function(scope, elm, attr, ngModelCtrl) {

        elm.unbind('input').unbind('keydown').unbind('change');
        elm.bind('blur', function() {
          scope.$apply(function() {
            if (elm.val()) {
              ngModelCtrl.$setViewValue(elm.val());
            }
          });
        });
      }
    };
  })
}).call(this);