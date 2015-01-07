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

        var maxTag = 2;

        scope.$watch(attrs.ngModel, function(v) {
          var tags = [];
          if (v) {
            tags = v.split(',');
          }
          if (tags.length > maxTag) {

            // el.toggleClass('has-error', uploadForm['tags'].$invalid);

            console.log("fuck u");
            return "Wrong!";
            // find the text box element, which has the 'name' attribute
          }
        });

      }
    }
  });
}).call(this);