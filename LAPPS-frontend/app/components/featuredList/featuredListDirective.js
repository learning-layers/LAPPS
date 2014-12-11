/**
 * @class featuredList
 * @memberOf lapps.lappsDirectives
 * @description This directive is a template for the list of featured apps on
 *              the welcome page.
 */
(function() {
  angular.module('lappsDirectives').directive('featuredList', function() {
    return {
      scope: true,
      restrict: 'E',
      templateUrl: 'components/featuredList/featuredListView.html',
      controller: 'featuredListCtrl'
    };
  });
}).call(this);