exports.config = {
  allScriptsTimeout: 34000,

  specs: ['e2e/*.js'],

  capabilities: {
    'browserName': 'chrome',
    'chromeOptions': {
      args: ['--disable-web-security']
    }
  },

  baseUrl: 'http://localhost:8000/',

  framework: 'jasmine',
  onPrepare: function() {
    browser.driver.manage().window().maximize();
  },
  jasmineNodeOpts: {
    defaultTimeoutInterval: 30000
  }
};
// "preprotractor": "npm run update-webdriver",
