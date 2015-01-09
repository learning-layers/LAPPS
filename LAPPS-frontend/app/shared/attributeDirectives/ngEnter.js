/**
 * @class ngEnter
 * @memberOf lapps.lappsAttibuteDirectives
 * @description This directive is used to execute a method, when an element
 *              receives a enter key-down
 */
(function() {
  angular.module('lappsAttibuteDirectives').directive('ngEnter', function() {
    return function(scope, element, attrs) {
      element.bind("keydown keypress", function(event) {
        if (event.which === 13) {
          scope.$apply(function() {
            scope.$eval(attrs.ngEnter);
          });

          event.preventDefault();
        }
      });
    };
  });
}).call(this);