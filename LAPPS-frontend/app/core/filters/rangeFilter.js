/**
 * @class rangeFilter
 * @memberOf lapps.lappsFilters
 * @description This filter is needed to loop over a range, instead over a given
 *              set of elements
 */
(function() {
  angular.module('lappsFilters').filter('rangeFilter', function() {
    return function(input, total) {
      total = parseInt(total);
      for (var i = 0; i < total; i++)
        input.push(i);
      return input;
    };
  });
}).call(this);