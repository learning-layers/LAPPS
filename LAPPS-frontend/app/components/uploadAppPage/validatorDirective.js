/**
 * @class validation
 * @memberof lapps.lappsDirectives
 * @description This directive is used for validation of required inputs.
 */
(function() {
  angular.module('lappsDirectives').directive('validateInput',
          ['$timeout', function($timeout) {
            return {
              restrict: 'A',
              require: '^form',
              link: function(scope, el, attrs, formCtrl) {

                // find the text box element, which has the 'name' attribute
                var inputElement = el[0].querySelector("[name]");
                // convert the native text box element to an angular element
                var inputAngularElement = angular.element(inputElement);
                // get the name on the text box
                var inputName = inputAngularElement.attr('name');
                // only apply the has-error class after the user leaves the text
                // box

                var blurred = false;
                inputAngularElement.bind('blur', function() {
                  blurred = true;
                  el.toggleClass('has-error', formCtrl[inputName].$invalid);
                });

                scope.$watch(function() {

                  return formCtrl[inputName].$invalid
                }, function(invalid) {
                  // toggle the has-error class after the blur
                  // event or if the control becomes valid
                  if (!blurred && invalid) { return }
                  el.toggleClass('has-error', invalid);
                });

                scope.$on('show-errors-check-validity', function() {
                  el.toggleClass('has-error', formCtrl[inputName].$invalid);
                });

                scope.$on('show-errors-reset', function() {
                  $timeout(function() {
                    el.removeClass('has-error');
                  }, 0, false);
                });
              }
            }
          }]);
}).call(this);