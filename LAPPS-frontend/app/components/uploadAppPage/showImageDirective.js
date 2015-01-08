/**
 * @class showImage
 * @memberOf lapps.lappsDirectives
 * @description This directive is used for managing the template of uploadPage
 */
(function() {
  angular.module('lappsDirectives').directive('showImage', function() {
    return {
      restrict: 'A',
      require: '^form',
      link: function(scope, el, attr, ctrl) {

        // find the text box element, which has the 'name' attribute
        var inputElement = el[0].querySelector("[name]");
        // convert the native text box element to an angular element
        var inputAngularElement = angular.element(inputElement);
        // get the name on the text box
        var inputName = inputAngularElement.attr('name');

        scope.$watch(uploadForm[inputName].$valid, function(validity) {
          console.log('valid now');
        })
        scope.$watch(uploadForm[inputName].$invalid, function(validity) {
          console.log('invalid now');
        })

      }
    };
  })
}).call(this);