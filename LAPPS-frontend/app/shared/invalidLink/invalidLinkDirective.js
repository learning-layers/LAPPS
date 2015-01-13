/**
 * @class invalidLink
 * @memberOf lapps.lappsDirectives
 * @description This directive is a template for a notification, when an invalid
 *              link was followed.
 */
(function() {
  angular.module('lappsDirectives').directive('invalidLink', function() {
    return {
      restrict: 'E',
      templateUrl: 'shared/invalidLink/invalidLinkView.html'
    };
  });
}).call(this);