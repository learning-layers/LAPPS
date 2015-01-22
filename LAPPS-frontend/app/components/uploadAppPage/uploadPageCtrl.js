/**
 * @class uploadPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for uploading apps
 */
(function() {
  angular.module('lappsControllers').controller(
          'uploadPageCtrl',
          [
              '$scope',
              '$http',
              'swaggerApi',
              'user',
              'platform',
              function($scope, $http, swaggerApi, user, platform) {

                // dummy userId
                $scope.userId = 390532463;

                /**
                 * @field
                 * @type object
                 * @memberOf lapps.lappsControllers.uploadPageCtrl
                 * @description new app object
                 */

                $scope.newapp = {
                  id: 0,
                  name: '',
                  platform: '',
                  minPlatformRequired: '',
                  downloadUrl: '',
                  version: '',
                  size: 0,
                  sourceUrl: '',
                  supportUrl: '',
                  rating: 0,
                  dateCreated: '',
                  dateModified: '',
                  license: '',
                  shortDescription: '',
                  longDescription: '',
                  creator: {
                    oidcId: 0,
                    email: '',
                    username: '',
                    role: 0,
                    dateRegistered: '',
                    description: '',
                    website: ''
                  },
                  artifacts: [],
                  tags: []
                }

                /**
                 * @field
                 * @type object
                 * @memberOf lapps.lappsControllers.uploadPageCtrl
                 * @description temporary tags object
                 */

                $scope.tags = {};

                var counter = 0;

                var currentIndex = 0;

                /**
                 * @field
                 * @type string
                 * @memberOf lapps.lappsControllers.uploadPageCtrl
                 * @description modified date of app
                 */
                $scope.dateModified = '';

                /**
                 * @field
                 * @type array of objects
                 * @memberOf lapps.lappsControllers.uploadPageCtrl
                 * @description fields to be generated for image in images
                 */

                $scope.newFields = [{
                  id: 0,
                  buttonName: 'add',
                  buttonTitle: 'Add'
                }];

                /**
                 * @field
                 * @type object
                 * @memberOf lapps.lappsControllers.uploadPageCtrl
                 * @description object for video
                 */
                $scope.video = {};

                /**
                 * @field
                 * @type object
                 * @memberOf lapps.lappsControllers.uploadPageCtrl
                 * @description object for thumbnail
                 */
                $scope.thumbnail = {};

                /**
                 * @field
                 * @type array
                 * @memberOf lapps.lappsControllers.uploadPageCtrl
                 * @description array of images for carousel
                 */

                $scope.images = [];

                /**
                 * @field
                 * @type array
                 * @memberOf lapps.lappsControllers.uploadPageCtrl
                 * @description gets list of platforms from platform object
                 */
                this.platforms = platform.platforms;

                /**
                 * @function
                 * @memberOf lapps.lappsControllers.uploadPageCtrl
                 * @description if form is valid creates new app object inputs
                 *              in form and sends the object through swaggerApi
                 */
                $scope.createNewApp = function() {

                  $scope.$broadcast('show-errors-check-validity');
                  // check if form is valid

                  if ($scope.uploadForm.$valid) {
                    $scope.newapp.creator.oidcId = $scope.userId;

                    // split tags and delete white spaces

                    if ($scope.tags.value) {
                      var tempTags = [];
                      tempTags = $scope.tags.value
                              .match(/(?=\S)[^,]+?(?=\s*(,|$))/g);

                      for (var i = 0; i < tempTags.length; i++) {
                        $scope.newapp.tags.push({
                          value: tempTags[i]
                        });
                      }
                    }

                    // create new artifacts from each image url

                    for (var i = 0; i < $scope.images.length; i++) {
                      if ($scope.images[i].url) {
                        $scope.newapp.artifacts.push({
                          url: $scope.images[i].url,
                          description: $scope.images[i].description,
                          type: 'image/png'
                        })
                      }
                    }
                    // create another artifact for video
                    if ($scope.video.url) {
                      $scope.newapp.artifacts.push({
                        url: $scope.video.url,
                        description: $scope.video.description,
                        type: 'video/youtube'
                      })
                    }
                    // create another artifact for thumbnail
                    $scope.newapp.artifacts.push({
                      url: $scope.thumbnail.url,
                      description: '',
                      type: 'thumbnail'
                    })
                    if ($scope.dateModified) {
                      $scope.newapp.dateModified = Date.parse(
                              $scope.dateModified).toString();
                    }

                    $scope.newapp.size = parseInt($scope.newapp.size);

                    // send app object through swaggerApi
                    swaggerApi.apps.createApp({
                      accessToken: 'test_token',
                      body: $scope.newapp
                    }).then(function() {
                      alert('Success!');
                      // reset values of newapp
                      $scope.reset();
                    });
                  }
                };

                /**
                 * @function
                 * @memberOf lapps.lappsControllers.uploadPageCtrl
                 * @param {object}
                 *          event
                 * @description for current event checks the name of the
                 *              currentTarget and if it is 'add' then adds
                 *              object to newFields array, else calls function
                 *              to find object at index then delete the object
                 */

                $scope.addAnotherField = function($event) {
                  if ($event.currentTarget.name == 'add') {
                    counter++;
                    $scope.newFields.push({
                      id: counter,
                      buttonName: 'remove' + counter,
                      buttonTitle: 'Remove'
                    });
                  } else {
                    $scope.findElementInArray($event.currentTarget.id);
                    $scope.newFields.splice(currentIndex, 1);
                  }
                  $event.preventDefault();
                };

                /**
                 * @function
                 * @memberOf lapps.lappsControllers.uploadPageCtrl
                 * @param {object}
                 *          index of array
                 * @description finds element in array for given index
                 */

                $scope.findElementInArray = function(index) {
                  for (var i = 0; i < $scope.newFields.length; i++) {
                    if ($scope.newFields[i].id == index) {
                      currentIndex = $scope.newFields
                              .indexOf($scope.newFields[i]);
                    }
                  }
                };

                /**
                 * @function
                 * @memberOf lapps.lappsControllers.uploadPageCtrl
                 * @description resets valus of newapp object
                 */
                $scope.reset = function() {

                  $scope.$broadcast('show-errors-reset');

                  $scope.newapp = {
                    id: 0,
                    name: '',
                    platform: '',
                    minPlatformRequired: '',
                    downloadUrl: '',
                    version: '',
                    size: 0,
                    sourceUrl: '',
                    supportUrl: '',
                    rating: 0,
                    dateCreated: '',
                    dateModified: '',
                    license: '',
                    shortDescription: '',
                    longDescription: '',
                    creator: {
                      oidcId: 0,
                      email: '',
                      username: '',
                      role: 0,
                      dateRegistered: '',
                      description: '',
                      website: ''
                    },
                    artifacts: [],
                    tags: []
                  }

                  $scope.tags.value = ' ';
                  $scope.video = {};
                  $scope.thumbnail = {};
                  $scope.images = [];
                  $scope.dateModified = '';

                }

                /*
                 * $scope.test = function() {
                 * 
                 * $scope.newapp = { id: 0, name: 'Test App', platform:
                 * 'Windows', minPlatformRequired: 'XP', downloadUrl:
                 * "http://google.com", version: '1.1', size: 3000, sourceUrl:
                 * 'http://google.com', supportUrl: 'http://google.com', rating:
                 * 3.5, dateCreated: '1421617299101', dateModified:
                 * '1421617299101', license: 'Copyright 2014', shortDescription:
                 * 'Consequat. Tempor ut cupidatat quis anim amet, irure ad ea
                 * cillum do exercitation culpa in et eiusmod magna duis
                 * consectetur dolore aute.', longDescription: "##Consectetur
                 * dolore ## Ut velit sed in in nulla ex dolor non cillum dolore
                 * est ut tempor consectetur id anim veniam, enim eiusmod do
                 * labore minim irure cupidatat lorem officia ad aute amet,
                 * adipiscing reprehenderit qui ipsum pariatur. Excepteur sit
                 * nisi sint eu proident, nostrud ea aliqua. Quis exercitation
                 * occaecat laborum. Voluptate deserunt fugiat commodo esse
                 * dolore dolor ullamco ut mollit culpa consequat. Sunt et duis
                 * aliquip in elit, laboris magna incididunt. Ut laboris mollit
                 * ullamco nulla sit dolor sed excepteur et voluptate fugiat
                 * aliqua. Aliquip adipiscing id non culpa est deserunt dolore
                 * officia in eu minim veniam, dolor do enim. ##Dolor esse ## Ut
                 * cupidatat mollit irure dolor tempor dolore consequat. Ipsum
                 * aute est id ut voluptate sunt minim incididunt velit
                 * consectetur ullamco ad laborum. Nulla in et eiusmod ea sint
                 * aliquip labore enim eu lorem aliqua. Do nostrud cillum duis
                 * proident, adipiscing anim magna ut quis sit elit, officia
                 * occaecat non reprehenderit culpa veniam, esse pariatur. Sed
                 * exercitation deserunt amet, fugiat in qui laboris dolor nisi
                 * excepteur dolore in commodo ex. Dolor laboris dolore do
                 * dolore minim labore eu reprehenderit id cupidatat officia
                 * deserunt ipsum occaecat dolor sint velit incididunt pariatur.
                 * Mollit nostrud esse veniam, consequat. Enim quis in in aute
                 * duis fugiat ad exercitation sit ullamco nisi sunt.", creator: {
                 * oidcId: 390532463, email: '', username: '', role: 0,
                 * dateRegistered: '', description: '', website: '' },
                 * artifacts: [], tags: [] }
                 * 
                 * $scope.tags.value = 'windowsapp,windows'; }
                 * 
                 * $scope.test();
                 */
              }]);
}).call(this);