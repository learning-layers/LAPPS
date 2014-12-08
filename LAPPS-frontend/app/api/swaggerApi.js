(function() {
  angular.module('lappsServices').service('swaggerApi',
          ['swaggerClient', function(swaggerClient) {
            return swaggerClient(lappsApi);
          }]);
}).call(this);