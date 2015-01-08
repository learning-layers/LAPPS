'use strict';

describe(
        'Test of tagsDirective',
        function() {
          beforeEach(module('templates'));
          beforeEach(module('lappsDirectives'));
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
                  'should show two links for both tags a,b',
                  function() {
                    compileDirective("<tags value='[{\"value\":\"a\"},{\"value\":\"b\"}]'></tags>");
                    expect(elm.html()).toContain('href="#/search/a"');
                    expect(elm.html()).toContain('href="#/search/b"');
                  });

          it(
                  'should hide the links, if nolink="true"',
                  function() {
                    compileDirective("<tags value='[{\"value\":\"a\"},{\"value\":\"b\"}]' nolink=\"true\" ></tags>");
                    expect(elm.html()).toContain(
                            'href="#/search/a" class="ng-binding ng-hide"');
                    expect(elm.html()).toContain(
                            'href="#/search/b" class="ng-binding ng-hide"');
                  });

          it(
                  'should show only tag a, since  min="1"',
                  function() {
                    compileDirective("<tags value='[{\"value\":\"a\"},{\"value\":\"b\"}]' min=\"1\" ></tags>");
                    expect(elm.html()).toContain('href="#/search/a"');
                    expect(elm.html()).toNotContain('href="#/search/b"');
                  });
        });