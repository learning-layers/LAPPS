/**
 * @class onCarouselChange
 * @memberOf lapps.lappsDirectives
 * @description This directive is used in the carousel on the welcome page, to
 *              keep track of the current slide.
 */
(function() {
  angular.module('lappsDirectives').directive('onCarouselChange',
          ['$parse', function($parse) {
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
          }]);
}).call(this);