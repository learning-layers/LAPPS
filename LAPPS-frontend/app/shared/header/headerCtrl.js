/**
 * @class headerCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for the header (i.e. check if
 *              user should see links, like admin-area, search etc)
 */
(function() {
  angular.module('lappsControllers').controller(
          'headerCtrl',
          [
              '$scope',
              '$location',
              'user',
              'platform',
              '$interval',
              "$route",
              function($scope, $location, user, platform, $interval, $route) {
                var SIGN_IN = 'Sign in';
                var SIGN_OUT = 'Sign out';
                /**
                 * @field
                 * @type string
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @description Text displayed on the sign in button.
                 */
                $scope.loginText = SIGN_IN;
                /**
                 * @field
                 * @type string
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @description Current content of the search box.
                 */
                $scope.searchQuery = '';

                /**
                 * @field
                 * @type boolean
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @description True if user is signed in. Used to display sign
                 *              in button accordingly to the current state.
                 */
                $scope.isloggedIn = false;

                /**
                 * @function
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @description Signs the user manually in (when clicking the
                 *              sign in button).
                 */
                $scope.login = function() {
                  user.signIn();
                }
                /**
                 * @function
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @description Signs the user out (when clicking the sign out
                 *              button).
                 */
                $scope.logout = function() {
                  user.signOut();
                  $scope.loginCallback(false);

                }
                /**
                 * @function
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @description Redirects to the search page with the given
                 *              search parameters.
                 */
                $scope.search = function() {
                  $location.path('/search/' + $scope.searchQuery);
                }
                /**
                 * @function
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @param {boolean}
                 *          success True, if the login was successful.
                 * @description Called after a oidc login attempt.
                 */
                $scope.loginCallback = function(success) {
                  /*
                   * user.signedIn = true; user.data = { sub: 12,
                   * preferred_username: 'JohnDoe', email: 'john@mail1.foobar' };
                   * user.role = 'Developer'; $scope.isloggedIn = true;
                   * $scope.userData = user.data; return;
                   */

                  $scope.isloggedIn = success;

                  $interval($scope.adjustSearchBar, 100, 1);
                  if (!success) {
                  } else {
                    $scope.userData = user.data;
                  }
                  // console.log(user);
                }
                /**
                 * @function
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @description Test method: upgrades current user to an admin.
                 */
                $scope.activateGodMode = function() {
                  user.role = 4;
                  user.token = 'test_token';
                  alert("You are now a mighty Admin!")
                }
                /**
                 * @function
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @type boolean
                 * @description True if the visitor is a developer.
                 */
                $scope.isDeveloper = function() {
                  return user.isDeveloper();
                }
                /**
                 * @function
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @type boolean
                 * @description True if the visitor is an admin. Note: Actual
                 *              security checks are performed on the backend
                 *              side. Here it is only used to show/hide
                 *              elements.
                 */
                $scope.isAdmin = function() {
                  return user.isAdmin();
                }
                /**
                 * @field
                 * @type boolean
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @description Controls of the help text underneath the navbar
                 *              is displayed.
                 */
                $scope.helperTextVisible = false;

                /**
                 * @field
                 * @type {platform[]}
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @description Stores the currently available platforms.
                 */
                $scope.platforms = platform.platforms;
                /**
                 * @field
                 * @type {platform}
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @description Stores the currently selected platform.
                 */
                $scope.currentPlatform = {};
                platform.selectInitialPlatform();
                $scope.currentPlatform = platform.currentPlatform;

                /**
                 * @function
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @description Used to change the selected platform filter
                 *              (i.e. Linux/Windows etc)
                 */
                $scope.changePlatform = function(id) {
                  $scope.currentPlatform = platform.getPlatformById(id);
                  platform.changePlatform($scope.currentPlatform);
                  // needed to wait after rendering of the element is finished
                  // (we need the new width of the element)
                  // due to queue 0 is enough waiting time
                  $interval($scope.adjustPositions, 0, 1);
                  $route.reload();
                }
                /**
                 * @function
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @description Checks if the user visits the page for the first
                 *              time. If yes, displays a helping notification on
                 *              how to change the platform filter.
                 */
                $scope.displayPlatformHelper = function() {
                  if (!window.localStorage['firstVisit']
                          || window.localStorage['firstVisit'] == 'false') {
                    $scope.helperTextVisible = true;
                    window.localStorage['firstVisit'] = true;
                    $interval(function() {
                      $scope.helperTextVisible = false
                    }, 15000, 1);
                  }
                }
                /**
                 * @function
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @description Makes sure the arrow-pointer of the help-popup
                 *              points to the platform selection button.
                 */
                $scope.adjustPlatformHelperArrow = function() {
                  var elem = $('#platform-selector');
                  if (elem) {
                    var center = elem.offset().left + elem.width() / 2;
                    if (center > 0) {// not collapsed
                      $('#help-text > .arrow-up').css('left', center + 'px');
                    } else {
                      elem = $('.navbar-header > .navbar-toggle');
                      center = elem.offset().left + elem.width() / 2;
                      $('#help-text > .arrow-up').css('left', center + 'px');
                    }
                  }
                }
                /**
                 * @function
                 * @memberOf lapps.lappsControllers.headerCtrl
                 * @description Resizes search bar to fit dynamically. Seems
                 *              like not all current browsers (like Safari for
                 *              iPhone) support flex.
                 */
                $scope.adjustSearchBar = function() {
                  var elem = $('.navbar-nav > li:last-child');
                  if (elem) {
                    var right = elem.position().left + elem.width();
                    if (right > 0) {// not collapsed
                      $('.search-bar.hidden-xs').css('left', right + 'px');
                    }
                    elem = $('.navbar-right');

                    var left = elem.width();

                    if (left > 0) {// not collapsed
                      $('.search-bar.hidden-xs').css('right', left + 'px');
                    }
                  }
                }

                user.init($scope.loginCallback);

                // keep helper pointer directly under the platform dropdown
                $(window).on('resize', function() {
                  $scope.adjustPlatformHelperArrow();
                  $scope.adjustSearchBar();
                });

                $scope.adjustPositions = function() {
                  $scope.adjustSearchBar();
                  $scope.adjustPlatformHelperArrow();
                }

                // when angularjs is ready working on the DOM
                $scope.$on('$viewContentLoaded', function() {
                  if ($location.path().indexOf('/search') < 0) {
                    $scope.searchQuery = '';
                  }
                  $scope.adjustPositions();
                  $scope.displayPlatformHelper();
                });
              }]);
}).call(this);