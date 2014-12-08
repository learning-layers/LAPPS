/*
 * Deploy script for LAPPS-frontend run with: npm run deploy
 * 
 * What it does: - Scan index.html for js and css - concat js by defined groups -
 * modify index.html to use merged js file names - minify all js, css and html
 * files - store all required files in a deploy directory - create a gzip
 * version for every resource
 */
(function(args) {

  /*
   * config
   */
  var deployDirectory = 'deploy/'; // where to put all files
  var devDirectory = 'app/'; // directory containing all files during
  // development

  // define how to group js files
  var jsGroups = [{
    baseDirs: ['api/lappsApi'],
    groupFile: 'js/api.js'
  }, {
    baseDirs: ['core', 'components', 'shared', 'api/swaggerApi'],
    groupFile: 'js/lapps.js'
  }, {
    baseDirs: ['bower_components/angular', 'bower_components/swagger'],
    groupFile: 'js/libsAngular.js'/*,
    ignore: ['.min.']*/
  }, {
    baseDirs: ['bower_components/bootstrap', 'bower_components/jquery',],
    groupFile: 'js/libs.js'/*,
    ignore: ['.min.']*/
  }];
  // define which other files should be copied
  var copyPaths = ['**/*.html', 'assets/img/**/*', 'assets/dummy/**/*',
      'bower_components/bootstrap/dist/fonts/*'];

  var FILE_ENCODING = 'utf-8';
  var EOL = '\n';
  var zipAll = true;

  /*
   * requires
   */
  var fs = require('fs-extra');
  var CleanCSS = require('clean-css');
  var uglifyJS = require('uglify-js');
  var zlib = require('zlib');
  var glob = require('glob');
  var stream = require('stream');
  var colors = require('colors');

  /*
   * steps
   */

  // read index.html
  var htmlSource = fs.readFileSync(devDirectory + 'index.html', FILE_ENCODING);
  // get all scripts linked in the index.html
  var scriptsUsed = getScriptsUsed(htmlSource);
  // get all css linked in the index.html
  var stylesUsed = getStylesUsed(htmlSource);

  // clear old deployment
  fs.removeSync(deployDirectory);
  // copy files based on given patterns
  copyFiles(copyPaths, function() {
    // delete old script references in index.html
    htmlSource = deleteScriptsInHtml(htmlSource, scriptsUsed);
    // concat and minify scripts found in index.html
    scriptsUsed = buildScripts(jsGroups, scriptsUsed);
    // minify css found in index.html
    buildStyles(stylesUsed);
    // insert new script references in index.html
    htmlSource = insertScriptsInHtml(htmlSource, scriptsUsed);
    // save modified index.html
    saveIndexHtml(htmlSource, 'index.html');
    console.log("done.".green);
  });

  String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
  };

  function minifyJS(jsCode, fileName) {
    var result = uglifyJS.minify(jsCode, {
      fromString: true,
      outSourceMap: fileName + '.map'
    });
    return result;
  }

  function minifyCSS(css) {
    return new CleanCSS({
      keepSpecialComments: 0
    }).minify(css);
  }

  function minifyHtml(html) {
    html = html.replace(/[\r\n]/gm, "").replace(/\>\s+\</gm, "><").trim();
    return html;
  }

  function copyFiles(patterns, callback) {
    console.log('copying files from'.green);
    for (var k = 0; k < patterns.length; k++) {
      (function(k) {
        console.log(' ' + devDirectory + patterns[k]);
        // find all files matching a pattern
        glob(devDirectory + patterns[k], {}, function(err, files) {
          if (err) throw err;
          for (var i = 0; i < files.length; i++) {
            var targetPath = deployDirectory + removeRootFromPath(files[i]);
            createDir(targetPath);
            if (!files[i].endsWith('index.html')) { // do not copy index.html
              if (files[i].endsWith('.html')) {

                var fileContent = fs.readFileSync(files[i], FILE_ENCODING);
                fs.writeFileSync(targetPath, minifyHtml(fileContent),
                        FILE_ENCODING);
                gzipFile(fileContent, targetPath);
              } else if (files[i].endsWith('.js')) {
                var fileContent = fs.readFileSync(files[i], FILE_ENCODING);
                compressScript(fileContent, targetPath);
              } else // some file we do not know how to minify
              {
                fs.copySync(files[i], targetPath);
                gzipFilePure(files[i], targetPath);
              }
            }
          }
          if (k == patterns.length - 1) // proceed with other steps when all
          // files have been copied
          callback();
        });
      })(k);
    }
  }

  function buildStyles(styles) {
    var out = styles.map(function(filePath) {
      return fs.readFileSync(devDirectory + filePath, FILE_ENCODING);
    });
    console.log('building styles:'.green);
    for (var i = 0; i < out.length; i++) {
      var minCSS = minifyCSS(out[i]);
      var fileName = deployDirectory + styles[i];
      createDir(fileName);
      fs.writeFileSync(fileName, minCSS, FILE_ENCODING);
      gzipFile(minCSS, fileName);
      console.log(' ' + styles[i] + ' built.');
    }
  }
  function gzipFilePure(src, dest) {
    if (!zipAll) return 0;
    var gzip = zlib.createGzip();
    fs.createReadStream(src).pipe(gzip)
            .pipe(fs.createWriteStream(dest + '.gz'));
  }
  function gzipFile(file, path) {
    if (!zipAll) return 0;
    var s = new stream.Readable();
    s._read = function noop() {
    }; // redundant? see update below
    s.push(file);
    s.push(null);
    var gzip = zlib.createGzip();
    s.pipe(gzip).pipe(fs.createWriteStream(path + '.gz'));
  }
  // create directory, if not already existing
  function createDir(path) {
    path = path.slice(0, path.lastIndexOf('/'));
    fs.mkdirsSync(path);
  }
  function concat(opts) {

    var fileList = opts.src;
    var distPath = opts.dest;
    var out = fileList.map(function(filePath) {
      return fs.readFileSync(devDirectory + filePath, FILE_ENCODING);
    });

    var fileName = deployDirectory + distPath;
    createDir(fileName);
    compressScript(out.join(EOL), fileName);

    console.log(' ' + distPath + ' built.');
  }
  function compressScript(fileContent, targetPath) {
    createDir(targetPath);
    var minifiedJS = minifyJS(fileContent, getFileNameFromPath(targetPath));
    fs.writeFileSync(targetPath, minifiedJS.code, FILE_ENCODING);
    fs.writeFileSync(targetPath + '.map', minifiedJS.map, FILE_ENCODING);
    gzipFile(minifiedJS.code, targetPath);
  }
  function removeRootFromPath(path) {
    return path.substring(path.indexOf('/') + 1);
  }
  function getFileNameFromPath(path) {
    return path.substring(path.lastIndexOf('/') + 1);
  }
  function buildScripts(groups, scripts) {

    console.log('building scripts:'.green);
    for (var i = 0; i < groups.length; i++) {
      var baseDirs = groups[i].baseDirs;
      var groupFile = groups[i].groupFile;
      var ignore = groups[i].ignore || [];
      var fileList = [];

      for (var j = 0; j < scripts.length; j++) {
        var ignoreThisScript = false;
        for (var k = 0; k < ignore.length; k++) {
          if (scripts[j].file.indexOf(ignore[k]) > 0) { // contains ignore
            // pattern
            ignoreThisScript = true;
          }
        }
        if (!ignoreThisScript) {
          for (var k = 0; k < baseDirs.length; k++) {
            if (scripts[j].file.indexOf(baseDirs[k]) == 0) {// starts with

              scripts[j].reference = '<script src="' + groupFile
                      + '"></script>';
              fileList.push(scripts[j].file);
              scripts[j].file = groupFile;
              scripts[j].isGrouped = true;
            }
          }
        }
      }
      concat({
        src: fileList,
        dest: groupFile
      });
    }
    // remove merged script duplicates
    var tempCopy = scripts;
    scripts = [];
    for (var i = 0; i < tempCopy.length; i++) {
      var alreadyExisting = false;
      for (var j = 0; j < scripts.length; j++) {
        if (tempCopy[i].reference == scripts[j].reference) {
          alreadyExisting = true;
          break;
        }

      }
      if (!alreadyExisting) {
        scripts.push(tempCopy[i]);
        if (!tempCopy[i].isGrouped) {// minify non-grouped js
          var fileContent = fs.readFileSync(devDirectory + tempCopy[i].file,
                  FILE_ENCODING);
          var targetPath = deployDirectory + tempCopy[i].file;
          compressScript(fileContent, targetPath);
        }
      }
    }
    return scripts;
  }
  function saveIndexHtml(html, distPath) {
    console.log('building index.html:'.green);
    var fileName = deployDirectory + distPath;
    createDir(fileName);
    fs.writeFileSync(fileName, minifyHtml(html), FILE_ENCODING);
    gzipFile(html, fileName);

    console.log(' ' + distPath + ' built.');
  }
  function insertScriptsInHtml(html, scripts) {

    var startPoint = html.lastIndexOf('.css">');// insert scripts below title-tag
    if (startPoint < 0) {
      console.log('No title tag found in index.html!');
      return null;
    }
    startPoint += ('.css">').length;
    var scriptBlock = "\n";
    for (var i = 0; i < scripts.length; i++) {
      scriptBlock += "  " + scripts[i].reference + "\n";

    }
    html = html.substring(0, startPoint) + scriptBlock
            + html.substring(startPoint);
    return html;
  }
  function deleteScriptsInHtml(html, scripts) {

    for (var i = 0; i < scripts.length; i++) {
      html = html.replace(scripts[i].reference, "");
    }

    return html.replace(/^\s*[\r\n]/gm, "");
  }
  function getStylesUsed(html) {
    var usedList = [];
    var regex = /\<link(\s)*rel(\s)*=(\s)*('|")stylesheet.*/g;
    var match = regex.exec(html);
    while (match != null) {
      var style = null;
      var temp = match[0].substring(match[0].indexOf('href'));
      if (/"/g.test(temp)) {
        style = temp.match(/"(.*?)"/g)[0];
      } else if (/'/g.test(temp)) {
        style = temp.match(/'(.*?)'/g)[0];
      } else {
        style = null;
      }
      if (style) {
        style = style.slice(1, style.length - 1);
        usedList.push(style);
      }

      match = regex.exec(html);
    }
    return usedList;
  }
  function getScriptsUsed(html) {
    var usedList = [];
    var regex = /\<script(\s)*src.*/g;
    var match = regex.exec(html);

    while (match != null) {
      var script = {};
      script.reference = match[0]; // save html code

      // get the raw file path
      if (/"/g.test(match[0])) {
        script.file = match[0].match(/"(.*?)"/g)[0];
      } else if (/'/g.test(match[0])) {
        script.file = match[0].match(/'(.*?)'/g)[0];
      } else {
        script.file = null;
      }
      if (script.file) {
        script.file = script.file.slice(1, script.file.length - 1);
        usedList.push(script);
      }

      match = regex.exec(html);
    }
    return usedList;
  }

})(Array.prototype.slice.call(arguments, 0));