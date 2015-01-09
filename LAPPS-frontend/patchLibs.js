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
  var changed = false;
  if (match != null) {
    var offset = match.index + 21;
    libSource = libSource
            .insert(
                    offset,
                    'window.swaggerAngularClientExt = {};window.swaggerAngularClientExt.getRequestHeaders = getRequestHeaders;window.swaggerAngularClientExt.getRequestUrl = getRequestUrl;window.swaggerAngularClientExt.getRequestBody = getRequestBody;window.swaggerAngularClientExt.applyAuthData = applyAuthData;window.swaggerAngularClientExt.errorTypes = errorTypes;');
    changed = true;
  }
  // make it minification compatible. the .min. given with the swagger angular
  // client package is buggy
  var regex2 = /\.factory\('swaggerClient',\s*f/g;
  var regex3 = /\);\s*},{"..\/bower_components\/swagger-client-generator\/dist\/swagger-client-generator\.js/g;
  match = regex2.exec(libSource);
  if (match != null) {
    var offset = match.index + 25;
    libSource = libSource.insert(offset, "['$log', '$http', '$q', ");
    match = regex3.exec(libSource);
    if (match != null) {
      var offset = match.index;
      libSource = libSource.insert(offset, "]");
    }
    changed = true;
  }

  // make the the method return not only the data, but the whole response,
  // including headers
  var regex4 = /response\.data/g;
  match = regex4.exec(libSource);
  if (match != null) {

    libSource = libSource.replace("response.data", "response");

    changed = true;
  }
  if (changed) {
    fs.writeFileSync(libFile, libSource, FILE_ENCODING);
  }

})(Array.prototype.slice.call(arguments, 0));