'use strict';

describe(
        'Test of appDetail',
        function() {
          beforeEach(module('lappsApp'));

          var scope, $httpBackend, swagger, mockApps, platform;

          beforeEach(inject(function($rootScope, $controller, _$httpBackend_,
                  swagger2HttpBackend, _karmaHelper_, _platform_) {
            scope = $rootScope.$new();
            $httpBackend = _$httpBackend_;
            $controller('appDetailCtrl', {
              '$scope': scope,
              '$routeParams': {
                appId: 0
              }
            });
            swagger = swagger2HttpBackend;
            mockApps = _karmaHelper_.getMockApps();
            platform = _platform_;
          }));

          it('should toggle the collapsed state', function() {
            expect(scope.collapsed).toBe(true);
            scope.expandCollapseDescription();
            expect(scope.collapsed).toBe(false);
            scope.expandCollapseDescription();
            expect(scope.collapsed).toBe(true);
          });

          it('should make a request to show some apps', function() {
            var apiRequest = swagger.getRequest('apps.getApp', {
              id: 0
            });
            $httpBackend.expectGET(apiRequest.url).respond(mockApps[0]);

            var apiRequest2 = swagger.getRequest('apps.getAllPlatformsForApp',
                    {
                      appId: 0
                    });
            $httpBackend.expectGET(apiRequest2.url).respond([]);

            expect(scope.app.thumbnail).not.toBeDefined();
            $httpBackend.flush();
            expect(scope.alternativePlatforms[0].platform).toBe('-');

            expect(scope.app.thumbnail).toContain('.');
            expect(scope.app.images.length).toBeGreaterThan(0);
            expect(scope.app.videos.length).toBeGreaterThan(0);
            expect(scope.app.platformObj).toBeDefined();
            expect(scope.app.sourceUrlShort).toBeDefined();
            expect(scope.app.supportUrlShort).toBeDefined();
            expect(scope.app.size).toBeDefined();
            expect(scope.app.dateCreated).toBeDefined();
            expect(scope.app.dateModified).toBeDefined();
          });
          it(
                  'should request alternative platforms of an app and sort the results alphabetically',
                  function() {
                    var apiRequest = swagger.getRequest('apps.getApp', {
                      id: 0
                    });
                    $httpBackend.expectGET(apiRequest.url).respond(mockApps[0]);

                    var apiRequest2 = swagger.getRequest(
                            'apps.getAllPlatformsForApp', {
                              appId: 0
                            });
                    $httpBackend.expectGET(apiRequest2.url).respond(
                            [mockApps[0], mockApps[1]]);

                    $httpBackend.flush();
                    expect(scope.alternativePlatforms.length).toBe(2);
                    expect(scope.alternativePlatforms[0].platform).toBe('iOS');
                    expect(scope.alternativePlatforms[1].platform).toBe(
                            'Web Apps');
                  });
        });