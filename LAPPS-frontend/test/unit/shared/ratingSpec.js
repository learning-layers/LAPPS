'use strict';

describe(
        'Test of ratingDirective',
        function() {
          beforeEach(module('templates'));
          beforeEach(module('lappsApp'));
          var elm, scope, $httpBackend;

          beforeEach(inject(function($rootScope, $compile, _$httpBackend_) {
            $httpBackend = _$httpBackend_;
            scope = $rootScope.$new();
          }));
          function compileDirective(tpl) {
            inject(function($compile) {
              elm = $compile(tpl)(scope);
              scope.$digest();
            });
          }

          it(
                  'should show 5 full and 0 empty stars',
                  function() {
                    compileDirective('<star-rating value="4.5"></star-rating>');
                    var countStarFull = (elm.html().match(/glyphicon-star\s/g) || []).length;
                    var countStarEmpty = (elm.html().match(
                            /glyphicon-star-empty\s/g) || []).length;
                    expect(countStarFull).toBe(5);
                    expect(countStarEmpty).toBe(0);
                  });

          it(
                  'should show 2 full and 3 empty stars',
                  function() {
                    compileDirective('<star-rating value="2"></star-rating>');
                    var countStarFull = (elm.html().match(/glyphicon-star\s/g) || []).length;
                    var countStarEmpty = (elm.html().match(
                            /glyphicon-star-empty\s/g) || []).length;
                    expect(countStarFull).toBe(2);
                    expect(countStarEmpty).toBe(3);
                  });

          it(
                  'should show 5 full and 0 empty stars',
                  function() {
                    compileDirective('<star-rating value="10"></star-rating>');
                    var countStarFull = (elm.html().match(/glyphicon-star\s/g) || []).length;
                    var countStarEmpty = (elm.html().match(
                            /glyphicon-star-empty\s/g) || []).length;
                    expect(countStarFull).toBe(5);
                    expect(countStarEmpty).toBe(0);
                  });

        });