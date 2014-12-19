/**
 * @class platform
 * @memberOf lapps.lappsServices
 * @description This service handles identification and selection of the current
 *              device platform.
 */
(function() {
  angular
          .module('lappsServices')
          .service(
                  'platform',
                  function() {
                    /**
                     * @field
                     * @type {platform[]}
                     * @memberOf lapps.lappsServices.platform
                     * @description Stores the currently available platforms.
                     */
                    this.platforms = [
                        {
                          id: 0,
                          name: 'iOS',
                          icon: 'fa-apple',
                          agentRegEx: /(iPhone|iPad|iPod)/,
                          isMobile: true
                        },
                        {
                          id: 1,
                          name: 'Android',
                          icon: 'fa-android',
                          agentRegEx: /Android/,
                          isMobile: true
                        },
                        {
                          id: 2,
                          name: 'Windows Phone',
                          icon: 'fa-windows',
                          agentRegEx: /Windows Phone/,
                          isMobile: true
                        },
                        {
                          id: 3,
                          name: 'Web Apps',
                          icon: 'fa-globe',
                          isMobile: false
                        },
                        {
                          id: 4,
                          name: 'Windows',
                          icon: 'fa-windows',
                          agentRegEx: /(Windows NT|Windows 2000|Windows XP|Windows 7|Windows 8|Windows 10)/,
                          isMobile: false
                        }, {
                          id: 5,
                          name: 'Linux',
                          icon: 'fa-linux',
                          agentRegEx: /(Linux|X11)/,
                          isMobile: false
                        }, {
                          id: 6,
                          name: 'Mac OS X',
                          icon: 'fa-apple',
                          agentRegEx: /Mac OS X/,
                          isMobile: false
                        }, {
                          id: 7,
                          name: 'All Platforms',
                          icon: 'fa-circle-o',
                          isMobile: false
                        }];
                    /**
                     * @field
                     * @type {number}
                     * @memberOf lapps.lappsServices.platform
                     * @description Id of the default platform.
                     */
                    this.defaultPlatform = 3;
                    /**
                     * @field
                     * @type {platform}
                     * @memberOf lapps.lappsServices.platform
                     * @description Stores the currently selected platform.
                     */
                    this.currentPlatform = this.platforms[this.defaultPlatform];

                    /**
                     * @function
                     * @memberOf lapps.lappsServices.platform
                     * @type {platform}
                     * @param {number}
                     *          id Platform id.
                     * @description Returns the platform object with the given
                     *              id.
                     */
                    this.getPlatformById = function(id) {
                      for (var i = 0; i < this.platforms.length; i++) {
                        if (this.platforms[i].id == id) { return this.platforms[i]; }
                      }
                      return -1;
                    }

                    /**
                     * @function
                     * @memberOf lapps.lappsServices.platform
                     * @type {platform}
                     * @param {string}
                     *          name Platform name.
                     * @description Returns the platform object with the given
                     *              name.
                     */
                    this.getPlatformByName = function(name) {
                      name = name.toLowerCase();
                      for (var i = 0; i < this.platforms.length; i++) {
                        if (this.platforms[i].name.toLowerCase() == name) { return this.platforms[i]; }
                      }
                      return -1;
                    }
                    /**
                     * @function
                     * @type {platform}
                     * @memberOf lapps.lappsServices.platform
                     * @description Tries to detect the platform used by the
                     *              visitor. It checks the user agent string for
                     *              certain patterns.
                     */
                    this.detectPlatform = function() {
                      var agent = navigator.userAgent;

                      for (var i = 0; i < this.platforms.length; i++) {
                        if (typeof this.platforms[i].agentRegEx !== 'undefined'
                                && this.platforms[i].agentRegEx !== null) {
                          if (this.platforms[i].agentRegEx.test(agent)) { return this.platforms[i]; }
                        }
                      }
                      return this.platforms[this.defaultPlatform];
                    }

                    /**
                     * @function
                     * @memberOf lapps.lappsServices.platform
                     * @param {platform}
                     *          platform new platform.
                     * @description Updates the currently selected platform to a
                     *              new one. Also stores the new platform in
                     *              local storage, so it can be loaded from
                     *              there later.
                     */
                    this.changePlatform = function(platform) {
                      if (typeof platform !== 'number') {
                        this.currentPlatform = platform;
                      } else {
                        this.currentPlatform = this.getPlatformById(platform);
                      }

                      window.localStorage['platform'] = this.currentPlatform.id;
                    }

                    /**
                     * @function
                     * @memberOf lapps.lappsServices.platform
                     * @description Selects an initial platform as the current
                     *              one, when the page is loaded. If previously
                     *              visited, the value is loaded from local
                     *              storage. Otherwise the platform is set based
                     *              on the user agent detection. If this does
                     *              not work the default platform is set.
                     */
                    this.selectInitialPlatform = function() {
                      if (window.localStorage['platform']) {
                        var p = this
                                .getPlatformById(window.localStorage['platform']);
                        // nothing found/error

                        if (p < 0) {
                          this.currentPlatform = this.detectPlatform();
                          window.localStorage['platform'] = this.currentPlatform.id;
                        } else {
                          this.currentPlatform = p;
                        }
                      } else {
                        this.currentPlatform = this.detectPlatform();
                        window.localStorage['platform'] = this.currentPlatform.id;
                      }
                    }
                  });
}).call(this);