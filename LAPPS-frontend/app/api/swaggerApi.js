/**
 * @class swaggerApi
 * @memberOf lapps.lappsServices
 * @description This service offers access to the backend api using
 *              swagger-angular-client
 */
(function() {
  angular.module('lappsServices').service('swaggerApi',
          ['swaggerClient', function(swaggerClient) {
            return swaggerClient(lappsApi);
          }]);
}).call(this);