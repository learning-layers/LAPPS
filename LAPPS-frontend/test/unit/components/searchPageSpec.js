'use strict';

describe(
        'Test of searchPage',
        function() {
          beforeEach(module('lappsApp'));

          var scope, $httpBackend, swagger, mockApps, platform;

          beforeEach(inject(function($rootScope, $controller, _$httpBackend_,
                  swagger2HttpBackend, _karmaHelper_, _platform_) {
            scope = $rootScope.$new();
            $httpBackend = _$httpBackend_;
            $controller('searchPageCtrl', {
              '$scope': scope,
              '$routeParams': {
                query: 'a',
                user: 'b',
                sortBy: 'name',
                page: 2
              }
            });
            swagger = swagger2HttpBackend;
            mockApps = _karmaHelper_.getMockApps();
            platform = _platform_;
          }));

          it('should query, user sortBy and page from url', function() {
            expect(scope.searchQuery).toBe('a');
            expect(scope.searchUser).toBe('b');
            expect(scope.sortBy).toBe('name');
            expect(scope.currentPage).toBe(2);
          });

          it(
                  'should make a http request and receive 2 apps, then switch to page 3 and perform search',
                  function() {
                    var apiRequest = swagger.getRequest('apps.getAllApps', {
                      search: 'a',
                      page: 2,
                      pageLength: 10,
                      sortBy: 'name',
                      filterBy: 'platform;creator',
                      filterValue: platform.currentPlatform.name + ';b'
                    });
                    $httpBackend.expectGET(apiRequest.url).respond(
                            [mockApps[0], mockApps[1]]);
                    expect(scope.apps.length).toBe(0);
                    $httpBackend.flush();
                    expect(scope.apps.length).toBe(2);
                    expect(scope.apps[0].thumbnail).toContain('.');
                    expect(scope.apps[0].dateCreated).toBeDefined();
                    expect(scope.apps[0].dateModified).toBeDefined();

                    scope.changePage(3);
                    apiRequest = swagger.getRequest('apps.getAllApps', {
                      search: 'a',
                      page: 3,
                      pageLength: 10,
                      sortBy: 'name',
                      filterBy: 'platform;creator',
                      filterValue: platform.currentPlatform.name + ';b'
                    });
                    $httpBackend.expectGET(apiRequest.url).respond(
                            [mockApps[0], mockApps[1], mockApps[2]]);
                    $httpBackend.flush();
                    expect(scope.apps.length).toBe(3);
                  });

        });