'use strict';

describe('Test of featuredList', function() {
  beforeEach(module('lappsApp'));

  var scope, $httpBackend, swagger, mockApps, platform;

  beforeEach(inject(function($rootScope, $controller, _$httpBackend_,
          swagger2HttpBackend, _karmaHelper_, _platform_) {
    scope = $rootScope.$new();
    $httpBackend = _$httpBackend_;
    $controller('featuredListCtrl', {
      '$scope': scope

    });
    swagger = swagger2HttpBackend;
    mockApps = _karmaHelper_.getMockApps();
    platform = _platform_;
  }));

  it('should set tab only as new and top', function() {
    scope.setTab('top');
    expect(scope.currentTab).toBe('top');
    scope.setTab('new');
    expect(scope.currentTab).toBe('new');
    scope.setTab('banana');
    expect(scope.currentTab).toBe('new');
  });
  it('should make a request to show some apps', function() {
    var apiRequest = swagger.getRequest('apps.getAllApps', {

      page: 1,
      pageLength: 10,
      order: 'desc',
      sortBy: 'dateCreated',
      filterBy: 'platform',
      filterValue: platform.currentPlatform.name
    });
    $httpBackend.expectGET(apiRequest.url).respond([mockApps[0], mockApps[1]]);
    expect(scope.apps.length).toBe(0);
    $httpBackend.flush();
    expect(scope.apps.length).toBe(2);
    expect(scope.apps[0].thumbnail).toContain('.');
    expect(scope.apps[0].platformObj).toBeDefined();
  });

});