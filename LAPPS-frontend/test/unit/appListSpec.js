'use strict';

/* jasmine specs for controllers go here */
describe('Logic for app list', function() {
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

    it('should create "app" model with 3 dummy apps', function() {
      expect(scope.apps.length).toBe(0);
      $httpBackend.flush();
      expect(scope.apps.length).toBe(3);
    });
  });
});