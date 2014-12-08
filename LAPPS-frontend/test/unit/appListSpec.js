'use strict';

/* jasmine specs for controllers go here */
/*describe('Logic for app list', function() {
  beforeEach(module('lappsApp'));
  describe('featuredListCtrl', function() {
    var scope, ctrl, $httpBackend;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
      $httpBackend = _$httpBackend_;
      $httpBackend.expectGET('assets/dummy/appList.json').respond([{
        'id': '1',
        'name': 'HelpApp',
        'description': 'Automatically asks experts in the community for help.'
      }, {
        'id': '2',
        'name': 'ExpertApp',
        'description': 'Registers the user as an expert in the community.'
      }, {
        'id': '3',
        'name': 'ChessApp',
        'description': 'Teaches the rules of chess.'
      }]);

      scope = $rootScope.$new();
      ctrl = $controller('featuredListCtrl', {
        $scope: scope
      });

    }));
    afterEach(function() {
      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();
    });
    it('should create "app" model with 3 dummy apps', function() {
      expect(scope.apps.length).toBe(0);
      $httpBackend.flush();
      expect(scope.apps.length).toBe(3);
    });
  });
});*/

// TODO: create tests in different files, this is only an example how to use
// swagger with tests
describe('Logic for app search', function() {
  beforeEach(module('lappsApp'));
  describe('searchPageCtrl', function() {
    var scope, ctrl, $httpBackend;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller,
            swagger2HttpBackend) {

      $httpBackend = _$httpBackend_;
      var apiRequest = swagger2HttpBackend.getRequest('apps.getAllApps', {
        search: ''
      });
      $httpBackend.expectGET(apiRequest.url).respond([1, 2]);

      apiRequest = swagger2HttpBackend.getRequest('apps.getApp', {
        id: '1'
      });
      $httpBackend.expectGET(apiRequest.url).respond({
        'id': '1',
        'name': 'ChessApp',
        'rating': '1'
      });
      apiRequest = swagger2HttpBackend.getRequest('apps.getApp', {
        id: '2'
      });
      $httpBackend.expectGET(apiRequest.url).respond({
        'id': '2',
        'name': 'FoodApp',
        'rating': '1'
      });

      scope = $rootScope.$new();
      ctrl = $controller('searchPageCtrl', {
        $scope: scope
      });

    }));

    it('should result in 2 apps found', function() {
      expect(scope.apps.length).toBe(0);
      $httpBackend.flush();
      expect(scope.apps.length).toBe(2);
    });
  });
});