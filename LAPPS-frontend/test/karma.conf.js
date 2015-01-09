module.exports = function(config) {
  config
          .set({
            basePath: '../',
            preprocessors: {
              'app/**/*.html': ['ng-html2js']
            },
            ngHtml2JsPreprocessor: {
              stripPrefix: 'app/',
              moduleName: 'templates'
            },
            files: [

                'app/bower_components/angular/angular.js',
                'app/bower_components/angular-route/angular-route.js',
                'app/bower_components/angular-resource/angular-resource.js',
                'app/bower_components/angular-mocks/angular-mocks.js',
                'app/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
                'app/bower_components/swagger-angular-client/dist/swagger-angular-client.js',
                'app/bower_components/jquery/dist/jquery.js',
                'app/bower_components/angular-md5/angular-md5.js',
                'app/bower_components/angular-xeditable/dist/js/xeditable.js',
                'app/bower_components/marked/lib/marked.js',
                'app/api/lappsApi.js', 'app/core/**/*.js',
                'app/api/swaggerApi.js', 'test/utils/swagger2HttpBackend.js',
                'app/components/**/*.js', 'app/shared/**/*.js',
                'test/unit/**/*.js', 'test/utils/mockUserProvider.js',
                'test/utils/karmaHelper.js', 'app/**/*.html'],

            exclude: ['app/bower_components/**/*.html',
                'app/shared/login/userProvider.js'],
            autoWatch: true,

            frameworks: ['jasmine'],

            browsers: ['PhantomJS'],
            singleRun: true,
            plugins: [
            // 'karma-firefox-launcher',
            'karma-chrome-launcher', 'karma-phantomjs-launcher',
                'karma-jasmine', 'karma-ng-html2js-preprocessor'],

            junitReporter: {
              outputFile: 'test_out/unit.xml',
              suite: 'unit'
            }
          });
};