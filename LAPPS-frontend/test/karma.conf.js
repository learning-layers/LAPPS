module.exports = function(config) {
  config.set({
    basePath: '../',

    files: ['app/bower_components/angular/angular.js',
        'app/bower_components/angular-route/angular-route.js',
        'app/bower_components/angular-resource/angular-resource.js',
        'app/bower_components/angular-mocks/angular-mocks.js',
        'app/bower_components/jquery/dist/jquery.js', 'app/core/**/*.js',
        'app/components/**/*.js', 'app/shared/**/*.js', 'test/unit/**/*.js'],

    autoWatch: true,

    frameworks: ['jasmine'],

    browsers: ['PhantomJS'],

    plugins: [
    // 'karma-firefox-launcher',
    'karma-jasmine'],

    junitReporter: {
      outputFile: 'test_out/unit.xml',
      suite: 'unit'
    }
  });
};