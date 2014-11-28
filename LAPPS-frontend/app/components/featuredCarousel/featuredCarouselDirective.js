/**
 * @class featuredCarousel
 * @memberOf lapps.lappsDirectives
 * @description This directive is a template for the carousel displayed on the
 *              welcome page.
 */
(function() {
  angular.module('lappsDirectives').directive('featuredCarousel', function() {
    return {
      scope: true,
      restrict: 'E',
      templateUrl: 'components/featuredCarousel/featuredCarouselView.html',
      controller: 'featuredCarouselCtr'
    };
  });
}).call(this);