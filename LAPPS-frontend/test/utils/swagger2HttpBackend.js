/**
 * Helper method to allow usage of swagger api calls inside the karma tests
 * (without having to explicitly state urls etc.).
 */
(function() {
  angular.module('lappsServices').factory(
          'swagger2HttpBackend',
          [
              '$rootScope',
              'swaggerClient',
              function($rootScope, swaggerClient) {
                return {
                  getRequest: function(apiCall, data, options) {
                    $rootScope.api = $rootScope.api || swaggerClient(lappsApi); // initialize
                    // or
                    // reuse
                    // api
                    // definition

                    var operation = eval('$rootScope.api.' + apiCall
                            + '.operation');
                    var decl = operation.apiObject.apiDeclaration;

                    var _method = operation.method;
                    var _url = window.swaggerAngularClientExt.getRequestUrl(
                            operation, data);
                    var headersObj = window.swaggerAngularClientExt
                            .getRequestHeaders(operation, data, options);
                    var _data = window.swaggerAngularClientExt.getRequestBody(
                            operation, data, headersObj);

                    var _headers = (function(headersObj) {
                      // closure to keep the scope clean
                      return function(headers) {
                        for ( var key in headersObj) {
                          if (headersObj.hasOwnProperty(key)) {
                            if (headers[key + ''] !== headersObj[key] + '') { // check
                              // for
                              // mactching
                              // headers
                              return false;
                            }
                          }
                        }
                        return true;
                      };
                    }(headersObj));

                    return {
                      'method': _method,
                      'url': _url,
                      'data': _data,
                      'headers': _headers
                    };
                  }
                };
              }]);
}).call(this);