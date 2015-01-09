'use strict';

describe('Test of userPage', function() {
  beforeEach(module('lappsApp'));

  var scope, $httpBackend, swagger, mockUsers;

  beforeEach(inject(function($rootScope, $controller, _$httpBackend_,
          swagger2HttpBackend, _karmaHelper_) {
    scope = $rootScope.$new();
    $httpBackend = _$httpBackend_;
    $controller('userPageCtrl', {
      '$scope': scope,
      '$routeParams': {
        userId: 0
      }
    });
    swagger = swagger2HttpBackend;
    mockUsers = _karmaHelper_.getMockUsers();
  }));

  it('should read the userId from the url', function() {
    scope.userId = 0;
  });

  it('should make a http request and get the correct username', function() {
    var apiRequest = swagger.getRequest('users.getUser', {
      oidcId: 0
    });
    $httpBackend.expectGET(apiRequest.url).respond(mockUsers[0]);
    expect(scope.user.username).not.toBeDefined();
    $httpBackend.flush();
    expect(scope.user.username).toEqual(mockUsers[0].username);
  });
});