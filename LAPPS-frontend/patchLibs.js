/*
 * This script fixes some issues with the libraries used
 * 
 */
(function(args) {
  // config
  var FILE_ENCODING = 'utf-8';
  var libFile = 'app/bower_components/swagger-angular-client/dist/swagger-angular-client.js';

  String.prototype.insert = function(index, string) {
    if (index > 0)
      return this.substring(0, index) + string
              + this.substring(index, this.length);
    else
      return string + this;
  };

  var fs = require('fs-extra');

  // need to access methods like getRequestUrl from swagger angular client
  // module in unit tests, but require (karma-browsify) returns empty object...
  var libSource = fs.readFileSync(libFile, FILE_ENCODING);
  var regex = /\('swagger-validate'\);\s*var/g;
  var match = regex.exec(libSource);
  if (match != null) {
    var offset = match.index + 21;
    libSource = libSource
            .insert(
                    offset,
                    'window.swaggerAngularClientExt = {};window.swaggerAngularClientExt.getRequestHeaders = getRequestHeaders;window.swaggerAngularClientExt.getRequestUrl = getRequestUrl;window.swaggerAngularClientExt.getRequestBody = getRequestBody;window.swaggerAngularClientExt.applyAuthData = applyAuthData;window.swaggerAngularClientExt.errorTypes = errorTypes;');
    fs.writeFileSync(libFile, libSource, FILE_ENCODING);
  }

})(Array.prototype.slice.call(arguments, 0));