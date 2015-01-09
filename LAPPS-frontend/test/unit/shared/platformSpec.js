'use strict';

describe(
        'Test of platformService',
        function() {
          beforeEach(module('lappsServices'));

          var platformService;

          beforeEach(function() {
            inject(function(platform) {
              platformService = platform;
            })
          });

          it('should get a correct and a null value for getPlatformById',
                  function() {
                    expect(platformService.getPlatformById(1).name).toBe(
                            'Android');
                    expect(platformService.getPlatformById(9001)).toBe(null);
                  });

          it(
                  'should get a correct and a null value for getPlatformByName',
                  function() {
                    expect(platformService.getPlatformByName('iOS').id).toBe(0);
                    expect(platformService.getPlatformByName('Banana')).toBe(
                            null);
                  });

          it(
                  'should detect the current platform as iOS',
                  function() {
                    // hack to change user agent
                    var __originalNavigator = navigator;
                    navigator = new Object();
                    navigator.__proto__ = __originalNavigator;
                    navigator
                            .__defineGetter__(
                                    'userAgent',
                                    function() {
                                      return 'Mozilla/5.0 (iPad; CPU OS 7_1_2 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Version/7.0 Mobile/11D257 Safari/9537.53';
                                    });

                    expect(platformService.detectPlatform().name).toBe('iOS');
                  });

          it(
                  'should detect the current platform as Linux',
                  function() {
                    var __originalNavigator = navigator;
                    navigator = new Object();
                    navigator.__proto__ = __originalNavigator;
                    navigator
                            .__defineGetter__(
                                    'userAgent',
                                    function() {
                                      return 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/28.0.1500.52 Chrome/28.0.1500.52 Safari/537.36';
                                    });

                    expect(platformService.detectPlatform().name).toBe('Linux');
                  });

          it('should change current and saved platform to id 2', function() {
            platformService.changePlatform(2);
            expect(localStorage['platform']).toEqual('2');

          });

          it('should load platform with id based on localStorage', function() {
            localStorage['platform'] = '4';
            platformService.selectInitialPlatform();
            expect(platformService.currentPlatform.id).toBe(4);
          });
        });