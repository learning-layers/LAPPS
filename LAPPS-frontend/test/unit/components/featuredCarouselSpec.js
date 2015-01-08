'use strict';

describe('Test of featuredCarousel', function() {
  beforeEach(module('lappsApp'));

  var scope, $httpBackend, swagger, mockApps, platform;

  beforeEach(inject(function($rootScope, $controller, _$httpBackend_,
          swagger2HttpBackend, _karmaHelper_, _platform_) {
    scope = $rootScope.$new();
    $httpBackend = _$httpBackend_;
    $controller('featuredCarouselCtr', {
      '$scope': scope
    });
    swagger = swagger2HttpBackend;
    mockApps = _karmaHelper_.getMockApps();
    platform = _platform_;
  }));

  it('should make a request to show some apps', function() {
    var apiRequest = swagger.getRequest('apps.getAllApps', {
      page: 1,
      pageLength: 5,
      sortBy: 'random',
      filterBy: 'minrating;platform',
      filterValue: '3;' + platform.currentPlatform.name
    });
    $httpBackend.expectGET(apiRequest.url).respond([mockApps[0], mockApps[1]]);
    expect(scope.apps.length).toBe(0);
    $httpBackend.flush();
    expect(scope.apps.length).toBe(2);
    expect(scope.apps[0].thumbnail).toContain('.');
    expect(scope.apps[0].image).toContain('.');
    expect(scope.apps[0].platformObj).toBeDefined();
  });
});