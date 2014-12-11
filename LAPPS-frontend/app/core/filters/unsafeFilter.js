/**
 * @class unsafeFilter
 * @memberOf lapps.lappsFilters
 * @description This filter is needed to use markdown html in lapps' app
 *              descriptions
 */
(function() {
  angular.module('lappsFilters').filter('unsafe', ['$sce', function($sce) {
    return function(val) {
      return $sce.trustAsHtml(val);
    };
  }]);
}).call(this);