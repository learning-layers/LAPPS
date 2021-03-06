﻿'use strict';

describe(
        'Test of appDetail',
        function() {
          beforeEach(module('lappsApp'));

          var scope, $httpBackend, swagger, mockApps, mockComments, platform;

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
            mockComments = _karmaHelper_.getMockComments();
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

            var apiRequest_comments = swagger.getRequest('apps.getAllComments',
                    {
                      appId: 0,
                      page: 1,
                      pageLength: 10,
                      order: 'desc'
                    });
            $httpBackend.expectGET(apiRequest_comments.url).respond(
                    mockComments);

            var apiRequest_comments2 = swagger.getRequest(
                    'apps.getAllComments', {
                      appId: 0,
                      page: 1,
                      pageLength: 10,
                      filterBy: 'creator',
                      filterValue: '42'
                    });
            $httpBackend.expectGET(apiRequest_comments2.url).respond(
                    mockComments[0]);

            expect(scope.app.thumbnail).not.toBeDefined();
            $httpBackend.flush();
            expect(mockComments.length).toBe(2);
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
                    var apiRequest_comments = swagger.getRequest(
                            'apps.getAllComments', {
                              appId: 0,
                              page: 1,
                              pageLength: 10,
                              order: 'desc'
                            });
                    $httpBackend.expectGET(apiRequest_comments.url).respond(
                            mockComments);
                    var apiRequest_comments2 = swagger.getRequest(
                            'apps.getAllComments', {
                              appId: 0,
                              page: 1,
                              pageLength: 10,
                              filterBy: 'creator',
                              filterValue: '42'
                            });
                    $httpBackend.expectGET(apiRequest_comments2.url).respond(
                            mockComments[0]);

                    $httpBackend.flush();
                    expect(scope.alternativePlatforms.length).toBe(2);
                    expect(scope.alternativePlatforms[0].platform).toBe('iOS');
                    expect(scope.alternativePlatforms[1].platform).toBe(
                            'Web Apps');
                  });
        });