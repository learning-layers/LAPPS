'use strict';

describe(
        'Test of headerCtrl',
        function() {
          beforeEach(module('templates'));
          beforeEach(module('lappsApp'));

          var scope, userProvider, loc, time;

          beforeEach(inject(function($rootScope, $controller, $location, user,
                  $timeout) {
            scope = $rootScope.$new();

            $controller('headerCtrl', {
              '$scope': scope
            });
            loc = $location;
            userProvider = user;
            time = $timeout;
          }));

          it(
                  'should change login/logout status by using the mock userProvider',
                  function() {
                    scope.login();
                    expect(scope.isloggedIn).toBe(true);

                    scope.logout();
                    expect(scope.isloggedIn).toBe(false);
                  });

          it('should go to search page, when searching something', function() {
            scope.searchQuery = 'banana';
            scope.search();
            expect(loc.path()).toContain('search/banana');
          });

          it('should change the current platform', function() {
            scope.changePlatform(0);
            expect(scope.currentPlatform.id).toBe(0);
            scope.changePlatform(1);
            expect(scope.currentPlatform.id).toBe(1);
          });

          it('should show platform helper message on first visit only',
                  function() {
                    spyOn($.fn, 'position').andCallFake(function() {
                      return {
                        left: 0,
                        top: 0,
                        right: 0,
                        bottom: 0
                      }
                    });
                    window.localStorage['firstVisit'] = false;
                    scope.helperTextVisible = false;
                    scope.displayPlatformHelper();

                    expect(scope.helperTextVisible).toBe(true);
                    scope.helperTextVisible = false;
                    scope.displayPlatformHelper();
                    expect(scope.helperTextVisible).toBe(false);
                  });
        });