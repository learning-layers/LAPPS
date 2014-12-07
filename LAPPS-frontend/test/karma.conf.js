module.exports = function(config) {
  config
          .set({
            basePath: '../',

            files: [
                'app/bower_components/angular/angular.js',
                'app/bower_components/angular-route/angular-route.js',
                'app/bower_components/angular-resource/angular-resource.js',
                'app/bower_components/angular-mocks/angular-mocks.js',
                'app/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
                'app/bower_components/swagger-angular-client/dist/swagger-angular-client.js',
                'app/bower_components/jquery/dist/jquery.js',
                'app/api/lappsApi.js', 'app/core/**/*.js',
                'test/utils/swagger2HttpBackend.js', 'app/components/**/*.js',
                'app/shared/**/*.js', 'test/unit/**/*.js',
                'test/utils/mockUserProvider.js'],

            exclude: ['app/shared/login/userProvider.js'],
            autoWatch: true,

            frameworks: ['jasmine'],

            browsers: ['PhantomJS'],
            singleRun: true,
            plugins: [
            // 'karma-firefox-launcher',
            'karma-chrome-launcher', 'karma-phantomjs-launcher',
                'karma-jasmine'],

            junitReporter: {
              outputFile: 'test_out/unit.xml',
              suite: 'unit'
            }
          });
};