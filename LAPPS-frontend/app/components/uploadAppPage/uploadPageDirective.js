/**
 * @class uploadPage
 * @memberOf lapps.lappsDirectives
 * @description This directive is used for managing the template of uploadPage
 */
(function() {
  angular.module('lappsDirectives').directive('uploadPageDirective',
          function() {
            return {
              scope: true,
              restrict: 'E',
              templateUrl: 'components/uploadAppPage/uploadPageView.html',
              controller: 'uploadPageCtrl'
            };
          })
}).call(this);