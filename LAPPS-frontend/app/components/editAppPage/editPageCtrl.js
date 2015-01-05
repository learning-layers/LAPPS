/**
 * @class userPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying the profile of
 *              users.
 */
(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'editPageCtrl',
                  [
                      '$scope',
                      '$filter',
                      '$routeParams',
                      'user',
                      '$http',
                      'swaggerApi',
                      'md5',
                      function($scope, $filter, $routeParams, user, $http,
                              swaggerApi, md5) {

                        $scope.appTags = '';

                        $scope.app = {};

                        $scope.fetchAppData = function() {

                          $scope.app = {
                            id: 1, // have to get latest id?
                            name: 'Advertisement App',
                            platform: 'iOS',
                            supportedPlatformVersion: '5 and later',
                            downloadUrl: 'www.apple.com',
                            version: '1.2',
                            sizeKB: 20,
                            sourceUrl: 'www.github.com',
                            supportUrl: 'www.advertisement.com',
                            developer: {
                              name: 'John',
                              oidcId: 0
                            },
                            rating: 4,
                            tags: ['advertisement', 'ios', 'help'],
                            dateCreated: new Date(2013, 4, 15),
                            dateModified: new Date(2014, 11, 09),
                            licence: 'Best Ads LLC',
                            shortDescription: 'Shows current most popular apps',
                            longDescription: 'Advertising is a form of marketing communication used to persuade an audience to take or continue some action, usually with respect to a commercial offering, or political or ideological support. In Latin, ad vertere means "to turn toward".[1] The purpose of advertising may also be to reassure employees or shareholders that a company is viable or successful. Advertising messages are usually paid for by sponsors and viewed via various old media; including mass media such as newspaper, magazines, television advertisement, radio advertisement, outdoor advertising or direct mail; or new media such as blogs, websites or text messages.'

                                    + ' Commercial advertisers often seek to generate increased consumption of their products or services through "branding", which involves associating a product name or image with certain qualities in the minds of consumers. Non-commercial advertisers who spend money to advertise items other than a consumer product or service include political parties, interest groups, religious organizations and governmental agencies. Nonprofit organizations may rely on free modes of persuasion, such as a public service announcement (PSA).',
                            thumbnail: 'https://s.gravatar.com/avatar/',
                            artifacts: [{
                              url: $scope.videoUrl,
                              type: 'video'
                            }, {
                              url: $scope.videoUrl,
                              type: 'video'

                            }]

                          };

                          if ($scope.app.tags) {
                            $scope.appTags = $scope.app.tags.join();
                          }

                        }

                        this.platforms = [
                            {
                              id: 0,
                              name: 'iOS',
                              icon: 'fa-apple',
                              agentRegEx: /(iPhone|iPad|iPod)/
                            },
                            {
                              id: 1,
                              name: 'Android',
                              icon: 'fa-android',
                              agentRegEx: /Android/
                            },
                            {
                              id: 2,
                              name: 'Windows Phone',
                              icon: 'fa-windows',
                              agentRegEx: /Windows Phone/
                            },
                            {
                              id: 3,
                              name: 'Web Apps',
                              icon: 'fa-globe',
                            },
                            {
                              id: 4,
                              name: 'Windows',
                              icon: 'fa-windows',
                              agentRegEx: /(Windows NT|Windows 2000|Windows XP|Windows 7|Windows 8|Windows 10)/
                            }, {
                              id: 5,
                              name: 'Linux',
                              icon: 'fa-linux',
                              agentRegEx: /(Linux|X11)/
                            }, {
                              id: 6,
                              name: 'Mac OS X',
                              icon: 'fa-apple',
                              agentRegEx: /Mac OS X/
                            }, {
                              id: 7,
                              name: 'All Platforms',
                              icon: 'fa-circle-o',
                            }];

                        $scope.showPlatform = function() {

                          var selected = $filter('filter')($scope.platforms, {
                            name: $scope.app.platform
                          });
                          return ($scope.app.platform && selected.length)
                                  ? selected[0].name : 'Not set';

                        };

                        $scope.validateInput = function(data) {

                          if (!data) { return 'Input required'; }

                        }

                        $scope.fetchAppData();

                      }]);
}).call(this);
