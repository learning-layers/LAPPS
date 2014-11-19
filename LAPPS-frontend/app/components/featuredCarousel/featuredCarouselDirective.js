/**
 * @class lappsHeader
 * @memberOf lapps.lappsDirectives
 * @description This directive is a template for the header.
 */
(function() {
  angular.module('lappsDirectives').directive('featuredCarousel', function() {
    return {
      restrict: 'E',
      templateUrl: 'components/featuredCarousel/featuredCarouselView.html',
      controller: 'featuredCarouselCtr'
    };
  });

  angular.module('lappsDirectives').directive('onCarouselChange',
          function($parse) {
            return {
              require: 'carousel',
              link: function(scope, element, attrs, carouselCtrl) {
                var fn = $parse(attrs.onCarouselChange);
                var origSelect = carouselCtrl.select;
                carouselCtrl.select = function(nextSlide, direction) {
                  if (nextSlide !== this.currentSlide) {
                    fn(scope, {
                      nextSlide: nextSlide,
                      direction: direction,
                    });
                  }
                  return origSelect.apply(this, arguments);
                };
              }
            };
          });
}).call(this);