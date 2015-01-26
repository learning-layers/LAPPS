/**
 * @class useruserSearchPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying a list of search
 *              results.
 */
(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'userSearchPageCtrl',
                  [
                      '$scope',
                      'swaggerApi',
                      '$routeParams',
                      'user',
                      '$location',
                      'convert',
                      '$timeout',
                      'md5',
                      '$timeout',
                      function($scope, swaggerApi, $routeParams, user,
                              $location, convert, $timeout, md5, $timeout) {
                        /**
                         * @field
                         * @type object
                         * @memberOf lapps.lappsControllers.userSearchPageCtrl
                         * @description Just gets the Math object into the scope
                         *              for use in bindings
                         */
                        $scope.Math = window.Math;
                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.userSearchPageCtrl
                         * @description Stores the current search query
                         */
                        $scope.searchQuery = '';

                        if ($routeParams.query) {
                          $scope.searchQuery = $routeParams.query;
                        }

                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.userSearchPageCtrl
                         * @description Saves the user input on how to sort the
                         *              search results.
                         */
                        $scope.sortBy = 'name';
                        if ($routeParams.sortBy) {
                          $scope.sortBy = $routeParams.sortBy;
                        }

                        /**
                         * @field
                         * @type boolean
                         * @memberOf lapps.lappsControllers.userSearchPageCtrl
                         * @description Results are filtered by role.
                         */
                        $scope.role = 'developer';
                        if ($routeParams.role) {
                          $scope.role = $routeParams.role;
                        }
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.userSearchPageCtrl
                         * @description Stores the current page of search
                         *              results
                         */
                        $scope.currentPage = 1;
                        if ($routeParams.page) {
                          $scope.currentPage = +$routeParams.page;
                        }
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.userSearchPageCtrl
                         * @description Stores the amount of pages of the
                         *              current search result
                         */
                        $scope.maxPage = 1;
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.userSearchPageCtrl
                         * @description Max amount of pages to display in the
                         *              page selector
                         */
                        $scope.maxDisplayPage = 6;
                        /**
                         * @field
                         * @type app[]
                         * @memberOf lapps.lappsControllers.userSearchPageCtrl
                         * @description Stores the app objects retrieved from
                         *              the backend.
                         */
                        $scope.users = [];
                        /**
                         * @field
                         * @type boolean
                         * @memberOf lapps.lappsControllers.userSearchPageCtrl
                         * @description Changed by a timeout. Used to display a
                         *              'not found' message when search
                         *              unsuccessful.
                         */
                        $scope.probablyNothingFound = false;

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.userSearchPageCtrl
                         * @description Searches using the current query
                         *              settings and loads a list of apps as
                         *              results. Sets currentPage to 1.
                         */
                        $scope.newSearch = function() {
                          document.activeElement.blur();// unfocus elements,
                          // important for the
                          // "not found"
                          // notification to be
                          // static
                          $scope.currentPage = 1;
                          $location.search('query', $scope.searchQuery);
                          $location.search('page', 1);

                          $location.search('role', $scope.role.toLowerCase());
                          $location.search('sortBy', $scope.sortBy
                                  .toLowerCase());
                          $location.search('user', $scope.searchUser);
                          $scope.search();
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.userSearchPageCtrl
                         * @description Searches using the current query
                         *              settings and loads a list of apps as
                         *              results.
                         */

                        $scope.search = function() {

                          // swaggerApi.users.getAllUsers({'accessToken':user.token}).then(function(data2){
                          var apiParams = {
                            search: $scope.searchQuery,
                            page: $scope.currentPage,
                            pageLength: 10,
                            accessToken: user.token
                          };

                          switch ($scope.sortBy) {
                          case 'name':
                            apiParams.sortBy = 'username';
                            break;
                          case 'newest':
                            apiParams.sortBy = 'dateRegistered';
                            apiParams.order = 'desc';
                            break;

                          default:
                            apiParams.sortBy = 'name';
                          }

                          if ($scope.role != 'all') {
                            apiParams.filterBy = 'role';
                            switch ($scope.role) {
                            case 'user':
                              apiParams.filterValue = '1';
                            case 'applicant':
                              apiParams.filterValue = '2';
                              break;
                            case 'developer':
                              apiParams.filterValue = '3';
                              break;
                            case 'admin':
                              apiParams.filterValue = '4';
                              break;

                            default:
                              apiParams.sortBy = '1';
                            }
                          }
                          swaggerApi.users
                                  .getAllUsers(apiParams)
                                  .then(
                                          function(response) {
                                            $scope.users = response.data;
                                            $scope.maxPage = +response
                                                    .headers('numberOfPages');

                                            for (var i = 0; i < $scope.users.length; i++) {
                                              $scope.users[i].roleName = user
                                                      .roleIdToRoleName($scope.users[i].role);
                                              $scope.users[i].memberScince = convert
                                                      .date($scope.users[i].dateRegistered);
                                              $scope.users[i].avatar = 'https://s.gravatar.com/avatar/'
                                                      + md5
                                                              .createHash($scope.users[i].email
                                                                      .trim()
                                                                      .toLowerCase()
                                                                      || $scope.users[i].username)
                                                      + '?s='
                                                      + 60
                                                      + '&d=identicon';
                                            }
                                          });
                          $timeout(
                                  function() {
                                    $scope.probablyNothingFound = $scope.users.length <= 0;
                                  }, 500);
                        }
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.userSearchPageCtrl
                         * @param {number}
                         *          pageNumber Destination page.
                         * @description Changes the current page to a new page.
                         */
                        $scope.changePage = function(pageNumber) {
                          $scope.currentPage = +pageNumber;
                          $location.search('page', pageNumber);
                          $scope.search();
                          $('html, body').animate(
                                  {
                                    scrollTop: ($('#search-results').first()
                                            .offset().top)
                                  }, 100);
                        }

                        $scope.search();
                        if (!user.token) {// token not yet set

                          $timeout(function() {
                            $scope.search();
                          }, 450);
                        } else {
                          $scope.search();
                        }
                      }]);
}).call(this);