/*
 * This script fetches the swagger-api documentation from the server and creates
 * a local json file that is then referenced from the window object, so it can
 * be used as lappsApi inside the frontend code. expected parameters: local use
 * locally running backend version, instead of the buche-server version.
 */
(function(args) {
  // config
  var FILE_ENCODING = 'utf-8';
  var localAddress = 'http://localhost:8080/lapps/v1/api-docs';
  var deployAddress = 'http://buche.informatik.rwth-aachen.de:9080/lapps-1.0-SNAPSHOT/lapps/v1/api-docs';
  var address = deployAddress;
  var apiFile = 'app/api/lappsApi.js';

  if (args[0] == 'local') {// use local or deploy server?
    address = localAddress;
  }

  var fs = require('fs-extra');
  var fetchSchema = require('fetch-swagger-schema');

  fetchSchema(address, function(error, schema) {
    if (error) return console.error(error);
    // create directory
    path = apiFile;
    path = path.slice(0, path.lastIndexOf('/'));
    fs.mkdirsSync(path);

    // wrap json and append to window object
    schema = '(function() { window.lappsApi = ' + JSON.stringify(schema)
            + ';}).call(this);';

    // create file
    fs.writeFileSync(apiFile, schema, FILE_ENCODING);
  });

})(Array.prototype.slice.call(arguments, 0));