﻿/**
 * @class searchPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying a list of search
 *              results.
 */
(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'searchPageCtrl',
                  [
                      '$scope',
                      'swaggerApi',
                      '$routeParams',
                      'user',
                      'platform',
                      '$location',
                      'convert',
                      function($scope, swaggerApi, $routeParams, user,
                              platform, $location, convert) {
                        /**
                         * @field
                         * @type object
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description Just gets the Math object into the scope
                         *              for use in bindings
                         */
                        $scope.Math = window.Math;
                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description Stores the current search query
                         */
                        $scope.searchQuery = '';

                        if ($routeParams.query) {
                          $scope.searchQuery = $routeParams.query;
                        }
                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @descriptiion If searched for a user, the user name
                         *               is stored here.
                         */
                        $scope.searchUser = '';

                        if ($routeParams.user) {
                          $scope.searchUser = $routeParams.user;
                        }
                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description Saves the user input on how to sort the
                         *              search results.
                         */
                        $scope.sortBy = 'name';
                        if ($routeParams.sortBy) {
                          $scope.sortBy = $routeParams.sortBy;
                        }
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.searchPageCtrl
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
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description Stores the amount of pages of the
                         *              current search result
                         */
                        $scope.maxPage = 1;
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description Max amount of pages to display in the
                         *              page selector
                         */
                        $scope.maxDisplayPage = 6;
                        /**
                         * @field
                         * @type app[]
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description Stores the app objects retrieved from
                         *              the backend.
                         */
                        $scope.apps = [];
                        /**
                         * @field
                         * @type boolean
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description The state of the advanced search area.
                         */
                        $scope.collapsed = true;

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @param {object}
                         *          usr user
                         * @description Shows only app results of user ust
                         */
                        $scope.listAppsByUser = function(usr) {
                          $scope.searchUser = usr;

                          $scope.search();
                        }
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description Searches using the current query
                         *              settings and loads a list of apps as
                         *              results. Sets currentPage to 1.
                         */
                        $scope.newSearch = function() {
                          $scope.currentPage = 1;
                          $location.path('/search/' + $scope.searchQuery);
                          $location.search('page', 1);
                          $location.search('sortBy', $scope.sortBy
                                  .toLowerCase());
                          $location.search('user', $scope.searchUser);
                          $scope.search();
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description Searches using the current query
                         *              settings and loads a list of apps as
                         *              results.
                         */

                        $scope.search = function() {
                          // swaggerApi.users.getAllUsers({'accessToken':user.token}).then(function(data2){
                          var apiParams = {
                            search: $scope.searchQuery,
                            page: $scope.currentPage,
                            pageLength: 10
                          };

                          switch ($scope.sortBy) {
                          case 'name':
                            apiParams.sortBy = 'name';
                            break;
                          case 'platform':
                            apiParams.sortBy = 'platform';
                            break;
                          case 'rating':
                            apiParams.sortBy = 'rating';
                            apiParams.order = 'desc';
                            break;
                          case 'newest':
                            apiParams.sortBy = 'dateCreated';
                            apiParams.order = 'desc';
                            break;
                          case 'random':
                            apiParams.sortBy = 'platform';
                            break;
                          case 'last updated':
                            apiParams.sortBy = 'dateModified';
                            apiParams.order = 'desc';
                            break;
                          default:
                            apiParams.sortBy = 'name';

                          }
                          if (platform.currentPlatform.isAllPlatforms !== true) {
                            apiParams.filterBy = 'platform';
                            apiParams.filterValue = platform.currentPlatform.name;
                          }

                          if ($scope.searchUser.trim() != '') {
                            apiParams.filterBy = apiParams.filterBy
                                    ? apiParams.filterBy + ';creator'
                                    : 'creator';
                            apiParams.filterValue = apiParams.filterValue
                                    ? apiParams.filterValue + ';'
                                            + $scope.searchUser
                                    : $scope.searchUser;
                          }
                          swaggerApi.apps
                                  .getAllApps(apiParams)
                                  .then(
                                          function(response) {
                                            $scope.apps = response.data;
                                            $scope.maxPage = +response
                                                    .headers('numberOfPages');

                                            for (var i = 0; i < $scope.apps.length; i++) {
                                              var thumbnail = '';
                                              for (var j = 0; j < $scope.apps[i].artifacts.length; j++) {
                                                if ($scope.apps[i].artifacts[j].type
                                                        .indexOf('thumb') >= 0) {
                                                  thumbnail = $scope.apps[i].artifacts[j].url;
                                                  break;
                                                }
                                              }
                                              $scope.apps[i].thumbnail = thumbnail;

                                              $scope.apps[i].platformObj = platform
                                                      .getPlatformByName($scope.apps[i].platform);

                                              $scope.apps[i].dateCreated = convert
                                                      .date($scope.apps[i].dateCreated);
                                              $scope.apps[i].dateModified = convert
                                                      .date($scope.apps[i].dateModified);
                                            }
                                          });
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description Toggles
                         *              {@link lapps.lappsControllers.searchPageCtrl.$scope.collapsed}.
                         */
                        $scope.expandCollapseSearch = function() {
                          if ($scope.collapsed) {
                            $scope.collapsed = false;
                          } else {
                            $scope.collapsed = true;
                          }
                        }

                        $scope.changePage = function(pageNumber) {

                          $scope.currentPage = +pageNumber;
                          $location.search('page', pageNumber);
                          $scope.search();
                        }
                        $scope.search();
                      }]);
}).call(this);