'use strict';

describe('Test of edit app page', function() {
  beforeEach(module('lappsApp'));

  var scope, $httpBackend, swagger, mockApps;

  beforeEach(inject(function($rootScope, $controller, _$httpBackend_,
          swagger2HttpBackend, _karmaHelper_) {
    scope = $rootScope.$new();
    $httpBackend = _$httpBackend_;
    $controller('editPageCtrl', {
      '$scope': scope,
      '$routeParams': {
        appId: 4845
      }
    });
    swagger = swagger2HttpBackend;
    mockApps = _karmaHelper_.getMockApps();
  }));

  it('should read the appid from the url', function() {
    scope.appId = 4845;
  });

  it('should make a http request and get the correct app', function() {
    var apiRequest = swagger.getRequest('apps.getApp', {
      id: 4845
    });
    $httpBackend.expectGET(apiRequest.url).respond(mockApps[0]);
    expect(scope.app.name).not.toBeDefined();
    $httpBackend.flush();
    expect(scope.app.name).toEqual(mockApps[0].name);
  });
});