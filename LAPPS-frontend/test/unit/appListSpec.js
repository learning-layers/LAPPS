'use strict';

/* jasmine specs for controllers go here */
/*
 * describe('Logic for app list', function() { beforeEach(module('lappsApp'));
 * describe('featuredListCtrl', function() { var scope, ctrl, $httpBackend;
 * 
 * beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
 * $httpBackend = _$httpBackend_;
 * $httpBackend.expectGET('assets/dummy/appList.json').respond([{ 'id': '1',
 * 'name': 'HelpApp', 'description': 'Automatically asks experts in the
 * community for help.' }, { 'id': '2', 'name': 'ExpertApp', 'description':
 * 'Registers the user as an expert in the community.' }, { 'id': '3', 'name':
 * 'ChessApp', 'description': 'Teaches the rules of chess.' }]);
 * 
 * scope = $rootScope.$new(); ctrl = $controller('featuredListCtrl', { $scope:
 * scope }); })); afterEach(function() {
 * $httpBackend.verifyNoOutstandingExpectation();
 * $httpBackend.verifyNoOutstandingRequest(); }); it('should create "app" model
 * with 3 dummy apps', function() { expect(scope.apps.length).toBe(0);
 * $httpBackend.flush(); expect(scope.apps.length).toBe(3); }); }); });
 */

// TODO: create tests in different files, this is only an example how to use
// swagger with tests
describe(
        'Logic for app search',
        function() {
          beforeEach(module('lappsApp'));
          describe(
                  'searchPageCtrl',
                  function() {
                    var scope, ctrl, $httpBackend;

                    beforeEach(inject(function(_$httpBackend_, $rootScope,
                            $controller, swagger2HttpBackend, platform) {
                      $httpBackend = _$httpBackend_;
                      var apiRequest = swagger2HttpBackend.getRequest(
                              'apps.getAllApps', {
                                search: '',
                                page: 1,
                                sortBy: 'name',
                                filterBy: 'platform',
                                filterValue: platform.currentPlatform.name
                              });
                      $httpBackend
                              .expectGET(apiRequest.url)
                              .respond(
                                      [
                                          {
                                            "id": 4348,
                                            "name": "Aegle",
                                            "platform": "Mac OS X",
                                            "minPlatformRequired": "> 0.1.3",
                                            "downloadUrl": "http://www.google.com/search?q=Apollo+Beta",
                                            "version": "3.6.2",
                                            "size": 49908,
                                            "sourceUrl": "http://www.google.com/search?q=Larissa+",
                                            "supportUrl": "http://www.google.com/search?q=Amalthea+Tool",
                                            "rating": 5,
                                            "dateCreated": 1418924885000,
                                            "dateModified": 1418924887000,
                                            "license": "Copyright 2014",
                                            "shortDescription": "Anim do duis officia pariatur. Deserunt reprehenderit adipiscing voluptate aute proident, ut est et sint esse in nostrud dolor sunt id aliqua.",
                                            "longDescription": "##Laborum  Magna ##\nUllamco sit cupidatat officia laboris dolor fugiat sunt ex tempor incididunt lorem aute et in ut amet, nisi dolor in anim est velit id ut mollit adipiscing ea deserunt pariatur. Ad culpa occaecat.\n##Adipiscing nostrud ##\nQui labore laborum. Enim sit in ullamco id pariatur. Magna et velit non deserunt amet, consequat. Veniam, aliqua. In aute anim officia voluptate tempor laboris fugiat irure nostrud nulla lorem dolore in elit, culpa nisi ad ut incididunt mollit ea esse commodo excepteur duis quis sed eiusmod minim ut ut occaecat reprehenderit dolore eu ex consectetur exercitation cupidatat sint do est cillum dolor sunt proident, adipiscing dolor ipsum aliquip. Excepteur incididunt fugiat deserunt proident, aliqua. Cillum quis laboris eiusmod dolor minim ut dolore exercitation dolor ullamco nisi sed consequat. Voluptate magna elit, sit eu ipsum ut reprehenderit labore officia qui amet, in sint do velit culpa mollit veniam, et nulla esse occaecat aute est adipiscing ea enim dolore consectetur ex ut in laborum. Id lorem in pariatur. Non ad sunt cupidatat aliquip nostrud duis tempor commodo anim irure. Laborum. Tempor esse eu elit, et sit in velit ea dolor irure exercitation nisi reprehenderit incididunt minim laboris commodo in anim ad in ex dolor ullamco pariatur. Dolore enim sed consequat. Aliquip aute adipiscing est non ut cillum ipsum veniam, deserunt.\n##Consequat  Voluptate ##\nSed lorem in amet, sunt magna deserunt officia minim fugiat elit, nisi qui id reprehenderit dolor exercitation voluptate dolore nulla aliquip culpa mollit nostrud velit cillum anim incididunt dolor dolore veniam, consequat. Eu proident, ut aliqua. Tempor labore excepteur adipiscing in eiusmod laborum. Sit ipsum ad enim ut commodo irure ut pariatur. Do cupidatat occaecat sint ea ullamco aute et quis ex consectetur duis in laboris non esse est. Ad esse est aliquip sed pariatur. Dolore ut in consectetur cupidatat ea enim culpa in ut minim sit aliqua. Mollit et commodo eu tempor excepteur nulla do id amet, aute.\n##Exercitation esse ##\nOfficia fugiat velit est amet, culpa consectetur quis non ut ut tempor ullamco sit ut id sed incididunt minim dolore lorem irure aliqua. Do cupidatat aliquip aute dolor occaecat labore duis nulla nostrud laborum. In esse eiusmod ex ad laboris pariatur. Veniam, proident, voluptate adipiscing et in anim nisi excepteur sint dolore cillum deserunt enim elit, ea in commodo sunt magna consequat. Exercitation eu mollit reprehenderit dolor ipsum qui. Officia ipsum consequat. Nulla voluptate enim dolore laboris tempor nisi dolor aliqua. Elit, incididunt ad dolor lorem ullamco amet, ea ut id sunt ut irure esse in duis sint sit do non est labore fugiat occaecat.\n",
                                            "creator": {
                                              "oidcId": 719300735,
                                              "email": "hobanwashburne@test.foobar",
                                              "username": "Hoban Washburne",
                                              "role": 3,
                                              "dateRegistered": 1418924879000,
                                              "description": "This is the personal description of Hoban Washburne!",
                                              "website": "http://HobanWashburne.something"
                                            },
                                            "artifacts": [
                                                {
                                                  "type": "thumbnail",
                                                  "url": "http://i.imgur.com/UB9dHqk.jpg",
                                                  "description": "Nostrud in non cillum ullamco laboris culpa."
                                                },
                                                {
                                                  "type": "image/png",
                                                  "url": "http://i.imgur.com/0jT9sb1.jpg",
                                                  "description": "Consectetur nostrud dolore laborum. Labore cillum dolore duis laboris do ipsum."
                                                },
                                                {
                                                  "type": "image/png",
                                                  "url": "http://i.imgur.com/cJTetYa.jpg",
                                                  "description": "Sint dolore incididunt id dolor qui adipiscing eu mollit nisi enim amet."
                                                },
                                                {
                                                  "type": "image/png",
                                                  "url": "http://i.imgur.com/zBBx7jP.jpg",
                                                  "description": "Ipsum dolor sint nostrud ut consequat. Reprehenderit veniam, officia nulla tempor ex."
                                                },
                                                {
                                                  "type": "image/png",
                                                  "url": "http://i.imgur.com/V5UlLVy.jpg",
                                                  "description": "Sunt id in ipsum commodo consequat. Dolor eiusmod non dolore deserunt."
                                                }],
                                            "tags": [{
                                              "id": 9581,
                                              "value": "Bertha"
                                            }, {
                                              "id": 9582,
                                              "value": "Puck"
                                            }]
                                          },
                                          {
                                            "id": 4360,
                                            "name": "Aegle",
                                            "platform": "Linux",
                                            "minPlatformRequired": "> 0.4.6",
                                            "downloadUrl": "http://www.google.com/search?q=Kiviuq+",
                                            "version": "2.3.3",
                                            "size": 26292,
                                            "sourceUrl": "http://www.google.com/search?q=Winchester+",
                                            "supportUrl": "http://www.google.com/search?q=Apollo+Beta",
                                            "rating": 3.5,
                                            "dateCreated": 1418924886000,
                                            "dateModified": 1418924888000,
                                            "license": "Copyright 2014",
                                            "shortDescription": "Labore in occaecat id esse nulla do aliqua. Ex reprehenderit ut ipsum eu.",
                                            "longDescription": "##Nostrud dolore ##\nEsse culpa ut sunt consequat. Duis ea sed minim proident, reprehenderit commodo occaecat tempor labore velit incididunt pariatur. Enim in quis excepteur fugiat laborum. Amet, sint exercitation est mollit ipsum ullamco elit, in magna cillum nisi anim non dolore et voluptate laboris consectetur dolor id officia aliqua. Dolor aute lorem aliquip dolore sit nulla nostrud ut qui veniam, adipiscing deserunt ad in irure cupidatat eu ut ex do eiusmod. Reprehenderit nostrud ut elit, minim.\n##Ea in ##\nNon nisi laboris consequat. Sunt pariatur. Veniam, ipsum magna in anim incididunt minim ut culpa aliquip et velit dolore nulla elit, nostrud sint exercitation laborum. Fugiat ut tempor id reprehenderit in sed quis ad ut esse labore mollit amet, eu lorem est ullamco cillum sit proident, dolore cupidatat enim duis voluptate consectetur do aute deserunt aliqua. Eiusmod dolor in officia adipiscing commodo occaecat dolor irure ea qui excepteur ex. Ex deserunt irure tempor eu minim ut non laborum. Reprehenderit in mollit cupidatat fugiat commodo consequat. Cillum nostrud ut occaecat sit esse qui sunt in excepteur nulla veniam, duis voluptate labore sed aliquip velit dolore dolor anim dolor incididunt officia pariatur. Laboris in nisi est culpa elit, quis magna consectetur ad exercitation enim eiusmod et ullamco aliqua. Ea ipsum proident, dolore do amet, adipiscing lorem sint aute id ut. In aute culpa laboris sunt amet, veniam, duis ut esse mollit nulla commodo ipsum et fugiat ut irure elit, in ut labore non voluptate.\n",
                                            "creator": {
                                              "oidcId": -1556155057,
                                              "email": "kayleefrye@test.foobar",
                                              "username": "Kaylee Frye",
                                              "role": 3,
                                              "dateRegistered": 1418924880000,
                                              "description": "This is the personal description of Kaylee Frye!",
                                              "website": "http://KayleeFrye.something"
                                            },
                                            "artifacts": [
                                                {
                                                  "type": "thumbnail",
                                                  "url": "http://i.imgur.com/HIxueyv.jpg",
                                                  "description": "Cillum dolore anim id magna incididunt mollit minim nostrud."
                                                },
                                                {
                                                  "type": "image/png",
                                                  "url": "http://i.imgur.com/GiUSHvX.jpg",
                                                  "description": "Cillum dolor dolor ullamco enim."
                                                },
                                                {
                                                  "type": "image/png",
                                                  "url": "http://i.imgur.com/oIBvEKj.jpg",
                                                  "description": "Exercitation laborum. Nisi proident, commodo culpa amet, ullamco."
                                                },
                                                {
                                                  "type": "image/png",
                                                  "url": "http://i.imgur.com/Rx2YXQr.jpg",
                                                  "description": "Magna occaecat eiusmod qui culpa."
                                                },
                                                {
                                                  "type": "image/png",
                                                  "url": "http://i.imgur.com/0jT9sb1.jpg",
                                                  "description": "Tempor minim amet, lorem cupidatat magna fugiat ipsum excepteur consequat."
                                                }],
                                            "tags": [{
                                              "id": 9651,
                                              "value": "Vesta"
                                            }, {
                                              "id": 9652,
                                              "value": "Methone"
                                            }, {
                                              "id": 9653,
                                              "value": "Iapetus"
                                            }, {
                                              "id": 9654,
                                              "value": "Pholus"
                                            }, {
                                              "id": 9655,
                                              "value": "Europa"
                                            }, {
                                              "id": 9656,
                                              "value": "Salacia"
                                            }, {
                                              "id": 9657,
                                              "value": "Herschel"
                                            }, {
                                              "id": 9658,
                                              "value": "Quaoar"
                                            }, {
                                              "id": 9659,
                                              "value": "Namaka"
                                            }]
                                          }]);

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