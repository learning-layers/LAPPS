/**
 * Helper service
 */
(function() {
  angular
          .module('lappsServices')
          .factory(
                  'karmaHelper',
                  [

                  function() {
                    return {
                      getMockUsers: function() {
                        return [{
                          "oidcId": 0,
                          "email": "dummy0@mail.foo",
                          "username": "Hans Olo",
                          "role": 1,
                          "dateRegistered": "1420556070000",
                          "description": "myDesc0",
                          "website": "www.foo.bar"
                        }, {
                          "oidcId": 1,
                          "email": "dummy0@mail.foo",
                          "username": "Hans Ol1",
                          "role": 2,
                          "dateRegistered": "1420556170000",
                          "description": "myDesc1",
                          "website": "www.foo.bar"
                        }, {
                          "oidcId": 2,
                          "email": "dummy0@mail.foo",
                          "username": "Hans Ol2",
                          "role": 3,
                          "dateRegistered": "1420556270000",
                          "description": "myDesc2",
                          "website": "www.foo.bar"
                        }, {
                          "oidcId": 3,
                          "email": "dummy0@mail.foo",
                          "username": "Hans Ol3",
                          "role": 4,
                          "dateRegistered": "1420556370000",
                          "description": "myDesc4",
                          "website": "www.foo.bar"
                        }, {
                          "oidcId": 4,
                          "email": "dummy0@mail.foo",
                          "username": "Hans Ol4",
                          "role": 1,
                          "dateRegistered": "1420556470000",
                          "description": "myDesc4",
                          "website": "www.foo.bar"
                        }]
                      },
                      getMockApps: function() {
                        return [
                            {
                              "id": 4845,
                              "name": "Aegaeon",
                              "platform": "Web Apps",
                              "minPlatformRequired": "> 1.1.7",
                              "downloadUrl": "http://www.google.com/search?q=Chariklo+",
                              "version": "4.3.7",
                              "size": 60511,
                              "sourceUrl": "http://www.google.com/search?q=Francisco+Application",
                              "supportUrl": "http://www.google.com/search?q=Petit-Prince+Beta",
                              "rating": 0.5,
                              "dateCreated": 1420556971000,
                              "dateModified": 1420556975000,
                              "license": "Copyright 2014",
                              "shortDescription": "Consectetur fugiat irure ut id sint aliqua. Exercitation sed nisi dolore amet, in ullamco sit anim eu.",
                              "longDescription": "##Fugiat anim ##\nPariatur. Veniam, in qui elit, est anim dolore nisi eu in laborum. Mollit proident, ex consectetur non nostrud quis lorem duis velit aute exercitation ullamco sit cupidatat magna id dolor in fugiat sint aliquip adipiscing laboris minim deserunt sed irure et occaecat sunt enim commodo ut consequat. Ut eiusmod cillum officia nulla ut amet, reprehenderit dolor incididunt labore esse ad excepteur aliqua. Do tempor voluptate ea dolore ipsum culpa. Qui consectetur eiusmod mollit sit labore ut consequat. Nulla officia do id ipsum voluptate aliquip proident, est amet, eu sed commodo dolore laborum. Ut non veniam, magna cillum lorem deserunt ex ullamco dolore nostrud cupidatat aute excepteur adipiscing pariatur. Sunt fugiat incididunt anim quis exercitation in esse culpa velit aliqua. Tempor reprehenderit minim et occaecat irure elit, enim laboris dolor.\n##Qui commodo ##\nNostrud ex et dolore ea aliquip eiusmod voluptate laboris cillum anim sint exercitation qui elit, in esse quis id est excepteur culpa minim consectetur ad lorem enim dolor deserunt sed occaecat consequat. Pariatur. Veniam, proident, mollit cupidatat in velit magna adipiscing tempor aliqua. Reprehenderit irure officia do amet, incididunt ut nisi fugiat dolor in ut laborum. Aute ipsum duis dolore sit eu ut nulla ullamco sunt commodo non labore. Culpa tempor excepteur in commodo sit aute ut esse proident, enim sed pariatur. Duis eiusmod aliquip reprehenderit non ut in cupidatat magna lorem do irure amet, dolore et ipsum nulla laboris consectetur est consequat. Adipiscing veniam, dolor dolor sunt incididunt labore ea velit dolore eu cillum ut deserunt quis minim fugiat elit, ad nisi.\n",
                              "creator": {
                                "oidcId": -1556155057,
                                "email": "kayleefrye@test.foobar",
                                "username": "Kaylee Frye",
                                "role": 3,
                                "dateRegistered": 1420556970000,
                                "description": "This is the personal description of Kaylee Frye!",
                                "website": "http://KayleeFrye.something"
                              },
                              "artifacts": [
                                  {
                                    "type": "thumbnail",
                                    "url": "http://i.imgur.com/RSxkEsH.jpg",
                                    "description": "Exercitation qui dolore magna sed do velit."
                                  },
                                  {
                                    "type": "video/youtube",
                                    "url": "//www.youtube.com/embed/Z9crdhewgkI",
                                    "description": "Et quis duis labore excepteur eiusmod in exercitation."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/Rx2YXQr.jpg",
                                    "description": "Nostrud aliquip ut in occaecat reprehenderit fugiat magna eiusmod anim."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/SHgvm2d.jpg",
                                    "description": "Fugiat officia cupidatat dolore est in."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/DuGHh5z.jpg",
                                    "description": "Ut dolore ea anim laborum. Duis qui."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/zBBx7jP.jpg",
                                    "description": "Minim exercitation officia eiusmod esse labore sit do occaecat eu."
                                  }],
                              "tags": [{
                                "id": 10475,
                                "value": "Mimas"
                              }, {
                                "id": 10476,
                                "value": "Larissa"
                              }, {
                                "id": 10477,
                                "value": "Phaethon"
                              }, {
                                "id": 10478,
                                "value": "Teharonhiawako"
                              }, {
                                "id": 10479,
                                "value": "Herculina"
                              }, {
                                "id": 10480,
                                "value": "Jupiter"
                              }, {
                                "id": 10481,
                                "value": "Telesto"
                              }]
                            },
                            {
                              "id": 4837,
                              "name": "Agamemnon",
                              "platform": "iOS",
                              "minPlatformRequired": "> 3.5.8",
                              "downloadUrl": "http://www.google.com/search?q=Hypnos+",
                              "version": "0.1.8",
                              "size": 62449,
                              "sourceUrl": "http://www.google.com/search?q=Duende+",
                              "supportUrl": "http://www.google.com/search?q=Cruithne+",
                              "rating": 2.5,
                              "dateCreated": 1420556970000,
                              "dateModified": 1420556974000,
                              "license": "Copyright 2014",
                              "shortDescription": "Dolore id laboris sint reprehenderit in duis et commodo magna exercitation ea dolor laborum. Occaecat nostrud minim.",
                              "longDescription": "##Enim fugiat ##\nFugiat ex veniam, nisi excepteur cillum lorem ut pariatur. Aliqua. Culpa officia incididunt amet, dolor est dolore in adipiscing nulla eiusmod mollit ullamco duis aliquip enim ipsum elit, magna consequat. Do dolor minim irure sint ea sunt voluptate quis consectetur non eu qui ut labore nostrud occaecat exercitation esse dolore sit et sed id commodo reprehenderit ut tempor ad deserunt anim aute in velit cupidatat laborum. In proident, laboris. Dolor quis esse aute et pariatur. Deserunt non amet, mollit exercitation ut fugiat ut id est officia ex laboris consectetur qui in do anim irure commodo in incididunt sunt proident, ea adipiscing eu enim occaecat culpa nisi magna ut veniam, duis tempor dolore labore voluptate sit sint consequat. Ad dolor aliqua. Elit, excepteur aliquip ipsum nostrud.\n##Excepteur et ##\nVoluptate qui eiusmod dolor officia in dolore veniam, exercitation enim sint aute consectetur nisi lorem aliqua. Nulla ullamco ad ex aliquip sit laborum. Incididunt ut pariatur. Et cupidatat culpa amet, do elit, in in est labore.\n",
                              "creator": {
                                "oidcId": 719300735,
                                "email": "hobanwashburne@test.foobar",
                                "username": "Hoban Washburne",
                                "role": 3,
                                "dateRegistered": 1420556970000,
                                "description": "This is the personal description of Hoban Washburne!",
                                "website": "http://HobanWashburne.something"
                              },
                              "artifacts": [
                                  {
                                    "type": "thumbnail",
                                    "url": "http://i.imgur.com/RSxkEsH.jpg",
                                    "description": "Ex labore magna commodo veniam, nostrud ad."
                                  },
                                  {
                                    "type": "video/youtube",
                                    "url": "//www.youtube.com/embed/ndo3VqErbrM",
                                    "description": "Pariatur. In exercitation cupidatat esse elit, labore sed excepteur eu velit incididunt."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/5KRULPO.jpg",
                                    "description": "Reprehenderit commodo deserunt cupidatat ut in."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/V5UlLVy.jpg",
                                    "description": "Deserunt fugiat voluptate consequat. Sint commodo ad duis."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/heSczUw.jpg",
                                    "description": "In in duis cillum incididunt eu ex labore cupidatat."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/Rx2YXQr.jpg",
                                    "description": "Cillum deserunt dolor do proident, adipiscing anim dolore."
                                  }],
                              "tags": [{
                                "id": 10433,
                                "value": "Trinculo"
                              }, {
                                "id": 10434,
                                "value": "Ijiraq"
                              }]
                            },
                            {
                              "id": 4843,
                              "name": "Amphitrite App",
                              "platform": "Mac OS X",
                              "minPlatformRequired": "> 2.7.3",
                              "downloadUrl": "http://www.google.com/search?q=Linus+App",
                              "version": "4.0.8",
                              "size": 9528,
                              "sourceUrl": "http://www.google.com/search?q=Ymir+",
                              "supportUrl": "http://www.google.com/search?q=Masursky+Application",
                              "rating": 5,
                              "dateCreated": 1420556971000,
                              "dateModified": 1420498800000,
                              "license": "Copyright 2014",
                              "shortDescription": "Magna non officia occaecat ea tempor cillum mollit lorem adipiscing do sed et sint ut ad incididunt duis qui in dolor.",
                              "longDescription": "##Labore incididunt ##\nExcepteur anim non aliqua. Officia voluptate minim fugiat mollit elit, ipsum ullamco dolor in sint duis cillum culpa deserunt et eiusmod magna exercitation ut quis sed sunt eu dolore ex do commodo consequat. Cupidatat laboris est dolor ea in tempor adipiscing qui nisi pariatur. Esse proident, lorem velit consectetur aute enim veniam, in occaecat laborum. Nostrud aliquip ad ut sit nulla labore irure dolore ut amet, id reprehenderit incididunt. Proident, minim voluptate sint ut consectetur occaecat in tempor velit in eiusmod aute duis dolor enim qui incididunt dolore cupidatat do dolore eu non ad est commodo culpa quis sed id excepteur ut pariatur. Et deserunt reprehenderit ea fugiat dolor in adipiscing labore anim aliquip cillum elit, consequat. Sunt ullamco laborum. Ut irure officia ipsum veniam, aliqua. Amet, mollit nostrud sit magna nisi ex nulla lorem laboris esse exercitation. Voluptate ex non ea reprehenderit fugiat laborum. Nulla ut consectetur minim incididunt id.\n##Consequat  Sint ##\nElit, irure labore fugiat magna nostrud excepteur nisi ea enim deserunt ex ut laboris aliquip ullamco ad voluptate sint sit ut velit aute aliqua. Veniam, lorem ipsum pariatur. Sunt in in amet, culpa anim exercitation reprehenderit et consequat. Qui tempor adipiscing eiusmod occaecat proident, esse cupidatat mollit minim sed consectetur eu non id nulla commodo dolore quis ut laborum. Est duis.\n##Culpa velit ##\nId adipiscing occaecat incididunt minim consequat. Ad enim deserunt qui in sit quis eiusmod consectetur lorem excepteur laboris amet, ut tempor mollit culpa in irure dolor ut nulla nostrud dolore cupidatat in esse aute exercitation laborum. Est ipsum sunt ea anim aliquip cillum labore et do dolore duis ut proident, sed pariatur. Voluptate nisi dolor non reprehenderit ex fugiat velit ullamco sint aliqua. Eu commodo veniam, magna officia elit. In pariatur. Excepteur dolor dolor nostrud duis quis est dolore reprehenderit tempor in exercitation culpa ex elit, non lorem veniam, amet, anim minim nulla enim ullamco mollit qui magna eiusmod dolore ea voluptate ut laboris laborum. Eu velit sint consequat. Aliqua. Proident, cupidatat nisi sit esse ad ut aute occaecat incididunt et consectetur id commodo cillum officia ipsum labore deserunt ut fugiat do sed aliquip irure adipiscing sunt in. Ut ex in dolore enim non tempor nulla fugiat amet, in ut nostrud do eu velit pariatur. Adipiscing incididunt anim elit, reprehenderit aliqua. Est ullamco.\n",
                              "creator": {
                                "oidcId": 719300735,
                                "email": "hobanwashburne@test.foobar",
                                "username": "Hoban Washburne",
                                "role": 3,
                                "dateRegistered": 1420556970000,
                                "description": "This is the personal description of Hoban Washburne!",
                                "website": "http://HobanWashburne.something"
                              },
                              "artifacts": [
                                  {
                                    "type": "thumbnail",
                                    "url": "http://i.imgur.com/8wYq8x0.jpg",
                                    "description": "Reprehenderit dolore est eiusmod cillum id."
                                  },
                                  {
                                    "type": "video/youtube",
                                    "url": "//www.youtube.com/embed/iP5eEbqqOqo",
                                    "description": "Ullamco esse velit est ex laboris et elit, labore irure minim mollit."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/foYukns.jpg",
                                    "description": "Laboris minim incididunt excepteur et voluptate eiusmod proident, amet, in dolor cupidatat."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/2FdFykW.jpg",
                                    "description": "Velit dolore excepteur nostrud elit, eiusmod amet, sit consectetur tempor ullamco laboris."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/cJTetYa.jpg",
                                    "description": "Dolor anim culpa proident, dolor minim adipiscing exercitation tempor ut in."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/NDD3WlY.jpg",
                                    "description": "Quis nulla dolor fugiat labore aute duis non et."
                                  }],
                              "tags": [{
                                "id": 10465,
                                "value": "Golevka"
                              }, {
                                "id": 10466,
                                "value": "Euler"
                              }, {
                                "id": 10467,
                                "value": "Typhon"
                              }, {
                                "id": 10468,
                                "value": "Hektor"
                              }, {
                                "id": 10469,
                                "value": "Iris"
                              }, {
                                "id": 10470,
                                "value": "Ijiraq"
                              }]
                            },
                            {
                              "id": 4859,
                              "name": "Annefrank",
                              "platform": "Windows Phone",
                              "minPlatformRequired": "> 5.7.7",
                              "downloadUrl": "http://www.google.com/search?q=Halley's+Comet+",
                              "version": "5.9.3",
                              "size": 83255,
                              "sourceUrl": "http://www.google.com/search?q=Romulus+App",
                              "supportUrl": "http://www.google.com/search?q=Mimas+App",
                              "rating": 3,
                              "dateCreated": 1420556973000,
                              "dateModified": 1420556976000,
                              "license": "Copyright 2014",
                              "shortDescription": "Cillum proident, irure lorem excepteur ut laborum. Cupidatat qui exercitation mollit minim sunt in incididunt quis duis nostrud reprehenderit pariatur. Ullamco labore.",
                              "longDescription": "##Enim nisi ##\nDeserunt ad nostrud aute amet, dolore non id ipsum quis sunt irure est reprehenderit exercitation incididunt officia eu aliquip anim do consectetur qui consequat. Ut cillum minim magna eiusmod aliqua. Pariatur. Sed in in adipiscing ullamco culpa ut commodo elit, nisi dolor laboris voluptate sit nulla excepteur proident, dolore lorem ut labore laborum. Enim cupidatat ea dolor.\n##Qui amet ##\nLaborum. Lorem aliqua. Consectetur ea sit duis dolor incididunt in deserunt mollit pariatur. Dolor enim occaecat id anim esse ut dolore excepteur nisi eu exercitation veniam, commodo magna reprehenderit velit ipsum tempor nostrud dolore adipiscing labore in nulla ut voluptate minim qui cillum est culpa non in fugiat ad ullamco et proident, sed ut sint consequat. Eiusmod irure aliquip cupidatat elit, quis officia laboris sunt amet, ex aute do. Dolor non aliquip dolor et consectetur labore officia sed ullamco cupidatat dolore reprehenderit magna ut pariatur. Culpa anim laboris enim esse occaecat exercitation minim sit ex aute laborum. Velit irure ipsum nostrud in duis nisi sunt adipiscing nulla.\n##Anim consectetur ##\nMollit dolor sit laboris culpa et nulla officia nisi occaecat consectetur sed cillum ea enim proident, sunt eu aliqua. Pariatur. Ut consequat. Magna non ex fugiat deserunt aliquip in labore veniam, duis nostrud reprehenderit ut exercitation qui lorem minim cupidatat elit, ullamco excepteur dolore quis id adipiscing velit amet, laborum. Est ipsum do dolore voluptate eiusmod dolor esse irure ut sint incididunt in tempor aute in ad anim commodo. Id duis laboris consectetur quis aliquip incididunt ad in dolor ullamco veniam, ea dolore ipsum enim commodo dolor sunt consequat. Tempor qui culpa aute reprehenderit nulla lorem est elit, et deserunt proident, in laborum. Do adipiscing cupidatat in non eiusmod pariatur. Exercitation eu cillum sit velit anim nostrud labore mollit officia aliqua. Voluptate dolore ut excepteur sint sed fugiat esse minim occaecat irure magna nisi ut ex amet, ut. Ut culpa quis velit occaecat eu adipiscing voluptate proident, cupidatat qui anim cillum sunt ad fugiat eiusmod nostrud dolor aliqua. Sed mollit in incididunt veniam, duis est reprehenderit dolore aliquip magna deserunt nulla esse amet, id tempor.\n",
                              "creator": {
                                "oidcId": 1304491603,
                                "email": "jaynecobb@test.foobar",
                                "username": "Jayne Cobb",
                                "role": 3,
                                "dateRegistered": 1420556970000,
                                "description": "This is the personal description of Jayne Cobb!",
                                "website": "http://JayneCobb.something"
                              },
                              "artifacts": [
                                  {
                                    "type": "thumbnail",
                                    "url": "http://i.imgur.com/8wYq8x0.jpg",
                                    "description": "Sit duis reprehenderit consectetur irure dolor et."
                                  },
                                  {
                                    "type": "video/youtube",
                                    "url": "//www.youtube.com/embed/1n7yMI5gzJ8",
                                    "description": "Nulla dolore laboris reprehenderit aute quis ut."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/NKawdIF.jpg",
                                    "description": "Non exercitation in minim qui mollit ut excepteur aliqua. Laborum. Dolor dolor."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/7G2JjkR.jpg",
                                    "description": "Nulla eu incididunt do occaecat culpa proident, ut."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/yrxhYri.jpg",
                                    "description": "Adipiscing dolor reprehenderit nisi eu."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/NDD3WlY.jpg",
                                    "description": "Amet, anim elit, id ut quis cupidatat nulla lorem."
                                  }],
                              "tags": [{
                                "id": 10563,
                                "value": "Galatea"
                              }, {
                                "id": 10564,
                                "value": "Erriapus"
                              }, {
                                "id": 10565,
                                "value": "Rhea"
                              }, {
                                "id": 10566,
                                "value": "Fortuna"
                              }]
                            },
                            {
                              "id": 4898,
                              "name": "Aspasia",
                              "platform": "iOS",
                              "minPlatformRequired": "> 5.2.4",
                              "downloadUrl": "http://www.google.com/search?q=Daphne+App",
                              "version": "3.1.9",
                              "size": 58664,
                              "sourceUrl": "http://www.google.com/search?q=Haumea+Tool",
                              "supportUrl": "http://www.google.com/search?q=Ferdinand+",
                              "rating": 1.5,
                              "dateCreated": 1420556977000,
                              "dateModified": 1420556980000,
                              "license": "Copyright 2014",
                              "shortDescription": "Nisi in laboris ipsum est in cillum veniam, reprehenderit eu dolor ut ut mollit aliquip consectetur aute quis ullamco exercitation ut anim officia nostrud pariatur. Cupidatat amet, consequat. Magna tempor.",
                              "longDescription": "##Ipsum cupidatat ##\nDo occaecat sed eu tempor sint id quis officia fugiat est laborum. Esse cillum ullamco labore consectetur commodo ex elit, sunt mollit ad ut proident, nulla sit cupidatat nostrud irure nisi dolore voluptate pariatur. Magna amet, ut reprehenderit qui excepteur lorem deserunt ea in aliqua. Minim adipiscing in dolor in veniam, incididunt laboris ut et dolor non eiusmod culpa aliquip duis velit exercitation dolore ipsum consequat. Enim.\n##Et nisi ##\nSunt ex lorem enim velit proident, cupidatat aute reprehenderit occaecat duis do est eiusmod commodo sint in dolore exercitation adipiscing elit, fugiat consequat. Eu amet, aliqua. Dolor officia consectetur nisi nulla ut ipsum ea in minim cillum.\n##Ullamco consequat ##\nVoluptate et aliquip nostrud eiusmod anim duis in dolore ipsum minim irure consectetur mollit occaecat ad culpa tempor in adipiscing ut reprehenderit magna in id lorem sit cillum amet, officia ex sunt nisi ullamco qui velit laborum. Sed ea deserunt eu nulla esse incididunt dolor dolor consequat. Laboris cupidatat dolore do labore enim non fugiat aute pariatur. Veniam, commodo sint ut exercitation est elit, excepteur aliqua. Ut quis proident. Dolor eiusmod ut enim id ad duis sit quis anim ea labore esse eu veniam, in commodo sunt excepteur aliqua. Pariatur. Irure sint occaecat consectetur magna aliquip et ex non cupidatat elit, tempor in nulla ullamco minim laboris do sed in dolor dolore dolore exercitation proident, aute ut incididunt est qui velit ut consequat. Adipiscing cillum officia amet, mollit nisi voluptate lorem fugiat reprehenderit deserunt culpa ipsum nostrud laborum.. Minim ipsum ut cillum exercitation.\n##Aliquip nulla ##\nSed elit, minim sint ut magna adipiscing ut voluptate labore fugiat deserunt id lorem ullamco excepteur duis tempor reprehenderit eiusmod et incididunt ad officia aute aliqua. Mollit exercitation veniam, proident, est amet, culpa commodo cillum in quis ipsum dolor ea non consequat. Eu in anim occaecat nisi ut dolore nulla ex laboris velit do enim pariatur. Sit aliquip irure nostrud consectetur cupidatat dolore sunt in dolor laborum. Esse qui. Qui ut tempor et id aute anim ea ut non exercitation elit, nisi do cupidatat officia nostrud velit irure nulla dolore ut fugiat lorem sed adipiscing culpa consectetur veniam, reprehenderit dolor deserunt laboris commodo mollit laborum. Aliqua. Quis in ullamco minim sit amet, sint esse ex magna incididunt enim pariatur. Ad in eu proident, ipsum est sunt consequat. Aliquip duis.\n",
                              "creator": {
                                "oidcId": -1315706713,
                                "email": "zoewashburne@test.foobar",
                                "username": "Zoe Washburne",
                                "role": 3,
                                "dateRegistered": 1420556970000,
                                "description": "This is the personal description of Zoe Washburne!",
                                "website": "http://ZoeWashburne.something"
                              },
                              "artifacts": [
                                  {
                                    "type": "thumbnail",
                                    "url": "http://i.imgur.com/UucIiQc.jpg",
                                    "description": "Enim sint in ea ut ut deserunt laboris aute."
                                  },
                                  {
                                    "type": "video/youtube",
                                    "url": "//www.youtube.com/embed/6lC2lbeY_rU",
                                    "description": "Ut cupidatat aliqua. Labore duis."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/foYukns.jpg",
                                    "description": "Laborum. Consequat. Aliquip est in velit minim id quis ipsum enim ea."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/Rx2YXQr.jpg",
                                    "description": "Culpa sint aliqua. Nulla eiusmod in dolor velit aute ut fugiat enim."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/ApteZX1.jpg",
                                    "description": "Minim eu voluptate qui excepteur laborum. Quis aliqua."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/heSczUw.jpg",
                                    "description": "Quis dolore esse nostrud irure sit laboris est cupidatat nulla ut elit."
                                  }],
                              "tags": [{
                                "id": 10807,
                                "value": "Duende"
                              }, {
                                "id": 10808,
                                "value": "Typhon"
                              }, {
                                "id": 10809,
                                "value": "Janus"
                              }, {
                                "id": 10810,
                                "value": "Castalia"
                              }, {
                                "id": 10811,
                                "value": "Rhea"
                              }, {
                                "id": 10812,
                                "value": "Hermione"
                              }, {
                                "id": 10813,
                                "value": "Gaspra"
                              }]
                            },
                            {
                              "id": 4915,
                              "name": "Aten App",
                              "platform": "Windows Phone",
                              "minPlatformRequired": "> 1.1.4",
                              "downloadUrl": "http://www.google.com/search?q=Paaliaq+",
                              "version": "3.7.0",
                              "size": 64951,
                              "sourceUrl": "http://www.google.com/search?q=Dioretsa+",
                              "supportUrl": "http://www.google.com/search?q=Aurora+Tool",
                              "rating": 4,
                              "dateCreated": 1420556979000,
                              "dateModified": 1420556982000,
                              "license": "Copyright 2014",
                              "shortDescription": "In duis esse nisi eiusmod ullamco elit, ad sed proident, ipsum eu reprehenderit do cupidatat magna sunt qui anim velit aute irure dolor minim in sint lorem voluptate ut.",
                              "longDescription": "##Adipiscing laborum ##\nSed in ad lorem anim dolore nulla aliqua. Id eiusmod eu ut minim amet, adipiscing labore officia deserunt cupidatat sunt ex excepteur non laborum. Reprehenderit ullamco enim mollit esse aute cillum sit ut ea laboris dolore consectetur incididunt dolor proident, culpa nisi velit do tempor commodo in irure exercitation ipsum aliquip qui dolor magna duis consequat. Est ut veniam, fugiat pariatur. Et nostrud voluptate sint in elit, quis occaecat. Dolor fugiat ipsum ex.\n##Consectetur est ##\nLabore ut nostrud nulla lorem in elit, ea in eiusmod magna dolor qui cupidatat officia esse eu id ex proident, reprehenderit excepteur quis fugiat anim tempor velit voluptate sed mollit aute aliquip non occaecat commodo et amet, irure in ullamco sit laboris ut deserunt duis nisi do cillum laborum. Ut aliqua. Est enim sunt sint veniam, culpa exercitation incididunt ipsum adipiscing consectetur dolore pariatur. Dolore dolor ad minim consequat.. Esse labore officia consequat. Est deserunt consectetur enim dolor in eiusmod elit, non sunt lorem incididunt reprehenderit aliqua. Proident, magna nulla laboris mollit ad pariatur. Cillum anim duis ullamco velit veniam.\n##Consectetur amet ##\nDolore officia deserunt in nulla fugiat ea elit, minim ex duis ad in reprehenderit proident, anim id sunt tempor culpa esse aute ut occaecat mollit incididunt eu in irure aliquip ipsum nostrud et adipiscing veniam, laboris sint eiusmod aliqua. Consectetur voluptate dolor sit commodo enim do dolor est pariatur.\n",
                              "creator": {
                                "oidcId": 719300735,
                                "email": "hobanwashburne@test.foobar",
                                "username": "Hoban Washburne",
                                "role": 3,
                                "dateRegistered": 1420556970000,
                                "description": "This is the personal description of Hoban Washburne!",
                                "website": "http://HobanWashburne.something"
                              },
                              "artifacts": [
                                  {
                                    "type": "thumbnail",
                                    "url": "http://i.imgur.com/RSxkEsH.jpg",
                                    "description": "Mollit ipsum dolor officia minim."
                                  },
                                  {
                                    "type": "video/youtube",
                                    "url": "//www.youtube.com/embed/1n7yMI5gzJ8",
                                    "description": "Enim pariatur. Nisi voluptate ut esse."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/cJTetYa.jpg",
                                    "description": "Occaecat irure esse duis dolore fugiat deserunt ea reprehenderit."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/2FdFykW.jpg",
                                    "description": "Excepteur officia dolore quis commodo pariatur. Dolor."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/3VYM1Gy.jpg",
                                    "description": "Tempor occaecat magna anim elit, nostrud amet, lorem fugiat ut."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/2FdFykW.jpg",
                                    "description": "Velit tempor esse non dolore nulla nostrud laboris."
                                  }],
                              "tags": [{
                                "id": 10901,
                                "value": "Ganymede"
                              }, {
                                "id": 10902,
                                "value": "Freia"
                              }, {
                                "id": 10903,
                                "value": "Uranus"
                              }]
                            },
                            {
                              "id": 4917,
                              "name": "Aurora",
                              "platform": "Android",
                              "minPlatformRequired": "> 1.7.5",
                              "downloadUrl": "http://www.google.com/search?q=Prokne+",
                              "version": "3.4.6",
                              "size": 79805,
                              "sourceUrl": "http://www.google.com/search?q=Ceres+",
                              "supportUrl": "http://www.google.com/search?q=Orcus+Tool",
                              "rating": 3,
                              "dateCreated": 1420556979000,
                              "dateModified": 1420556982000,
                              "license": "Copyright 2014",
                              "shortDescription": "Nulla fugiat anim in mollit laborum. Ea id laboris consequat. Occaecat magna eu aliqua. Cillum elit, in veniam, enim.",
                              "longDescription": "##Cupidatat labore ##\nNisi esse qui sed minim elit, dolor labore ex dolore incididunt amet, culpa cillum lorem ea enim cupidatat nulla consectetur ut do excepteur est in non ipsum ut fugiat deserunt reprehenderit proident, duis ut veniam, laboris magna adipiscing occaecat eiusmod eu dolor mollit id quis et pariatur. Laborum. Sunt exercitation aute commodo velit tempor sint ullamco in consequat. Dolore aliquip aliqua. Ad in sit voluptate officia anim nostrud irure. Sit aliqua. Cupidatat labore adipiscing id ea laboris eiusmod proident, ex voluptate nostrud velit incididunt ut dolor duis dolor aute veniam, lorem minim exercitation magna in dolore anim tempor excepteur sed irure est nulla ad nisi qui fugiat et in ipsum quis deserunt elit, pariatur. Amet, sint eu aliquip reprehenderit ut occaecat commodo do mollit non enim esse ullamco culpa in officia consectetur consequat. Dolore ut sunt cillum laborum.. In ea sed laboris labore ad cupidatat consequat. Dolore non exercitation commodo qui dolor aliquip culpa nostrud ex cillum id quis excepteur et aute veniam, incididunt minim tempor aliqua. Velit laborum. Consectetur mollit ut magna do lorem sint duis voluptate irure pariatur. Proident, ut eiusmod esse sunt.\n##Eu commodo ##\nAute occaecat qui fugiat labore sed magna consequat. Esse ad lorem eu dolor tempor id adipiscing anim duis amet, laborum. Incididunt in voluptate sunt ex dolor pariatur. Proident, sit mollit do aliqua. Nostrud veniam, exercitation enim cupidatat consectetur non dolore ut ea sint culpa nulla ullamco in in minim nisi commodo laboris dolore est reprehenderit irure et aliquip officia elit, eiusmod quis deserunt ut ipsum cillum velit ut excepteur. Tempor amet, nostrud dolor adipiscing sed duis laborum. Laboris cupidatat qui id exercitation est ut ipsum nulla et elit, sit incididunt sunt dolore fugiat eiusmod reprehenderit lorem do dolore officia.\n##Amet, sed ##\nDeserunt qui adipiscing quis dolore ut dolor cupidatat consectetur cillum voluptate tempor ut id ut officia in minim est proident, mollit laboris pariatur. Esse magna ullamco sed ad dolore consequat. Incididunt anim duis non sint veniam, eu aliqua. Amet, labore aliquip ex occaecat in in elit, aute sit enim ipsum nisi lorem exercitation ea culpa irure reprehenderit et nulla sunt velit laborum. Do dolor nostrud excepteur commodo fugiat eiusmod. Cillum amet, pariatur. Commodo laborum. Est mollit excepteur ut aute culpa cupidatat et fugiat occaecat sint consequat. Lorem sit sed sunt magna quis nostrud eiusmod incididunt in dolore irure enim ad labore ea proident, duis ullamco ut dolor anim ex esse consectetur nulla velit id dolor officia adipiscing elit, deserunt qui in voluptate ut ipsum veniam, do aliquip minim reprehenderit non exercitation aliqua. Dolore in nisi tempor eu laboris. Adipiscing ullamco sint labore ut dolore in magna nulla irure in quis ad.\n##Ea incididunt ##\nOccaecat in anim voluptate laboris consequat. Sit adipiscing ea non ullamco tempor duis ut sint deserunt qui ut culpa irure amet, pariatur. Ex cillum aliquip consectetur et ipsum laborum. Velit sed dolore labore dolor eiusmod in dolore mollit do quis magna sunt aute nisi reprehenderit id exercitation nostrud excepteur esse cupidatat minim commodo ut incididunt est elit, in nulla fugiat officia aliqua. Lorem dolor eu ad veniam, enim proident. Laborum. Dolor ut veniam, magna in ut dolore commodo est velit cillum irure qui consequat. Cupidatat ullamco in elit, eiusmod enim in ea.\n##Nisi laboris ##\nVoluptate ex pariatur. Dolor dolore aliqua. Sit et ad officia laborum. Incididunt cupidatat ipsum ut excepteur consequat. Occaecat laboris fugiat mollit qui enim consectetur in do tempor exercitation culpa dolore adipiscing velit esse id reprehenderit est in aliquip eu ut labore sint ullamco veniam, non dolor quis irure aute nulla commodo magna ea duis elit, eiusmod ut sunt nostrud nisi anim deserunt minim proident, cillum sed in amet.\n",
                              "creator": {
                                "oidcId": -1556155057,
                                "email": "kayleefrye@test.foobar",
                                "username": "Kaylee Frye",
                                "role": 3,
                                "dateRegistered": 1420556970000,
                                "description": "This is the personal description of Kaylee Frye!",
                                "website": "http://KayleeFrye.something"
                              },
                              "artifacts": [
                                  {
                                    "type": "thumbnail",
                                    "url": "http://i.imgur.com/Im5G6eQ.jpg",
                                    "description": "Velit mollit ut et amet, id ex."
                                  },
                                  {
                                    "type": "video/youtube",
                                    "url": "//www.youtube.com/embed/1n7yMI5gzJ8",
                                    "description": "Esse fugiat sit in proident, ullamco ea reprehenderit cupidatat aliquip magna."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/tqUfedw.jpg",
                                    "description": "Esse eu ullamco reprehenderit dolor veniam, dolore laborum. Irure pariatur. Sed."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/DuGHh5z.jpg",
                                    "description": "Exercitation ut qui in et eiusmod duis ut excepteur dolor reprehenderit sint."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/SHgvm2d.jpg",
                                    "description": "Eu nostrud et in officia sit."
                                  },
                                  {
                                    "type": "image/png",
                                    "url": "http://i.imgur.com/oIBvEKj.jpg",
                                    "description": "Labore in ut excepteur duis in est."
                                  }],
                              "tags": [{
                                "id": 10908,
                                "value": "Oberon"
                              }, {
                                "id": 10909,
                                "value": "Elatus"
                              }, {
                                "id": 10910,
                                "value": "Varuna"
                              }, {
                                "id": 10911,
                                "value": "Interamnia"
                              }, {
                                "id": 10912,
                                "value": "Interamnia"
                              }, {
                                "id": 10913,
                                "value": "Ceto"
                              }, {
                                "id": 10914,
                                "value": "Aten"
                              }]
                            }]
                      }
                    };
                  }]);
}).call(this);