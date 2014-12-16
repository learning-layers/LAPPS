/**
 * @class userPage
 * @memberOf lapps.lappsDirectives
 * @description This directive is a template for the profile page of users.
 */
(function() {
  angular.module('lappsDirectives').directive('userPage', function() {
    return {
      scope: true,
      restrict: 'E',
      templateUrl: 'components/userPage/userPageView.html',
      controller: 'userPageCtrl'
    };
  });
}).call(this);