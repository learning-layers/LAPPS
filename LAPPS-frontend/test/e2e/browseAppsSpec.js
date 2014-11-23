'use strict';

describe('Browse Apps', function() {
  it('should redirect index.html to index.html#/apps', function() {
    browser.get('app/index.html');
    browser.getLocationAbsUrl().then(function(url) {
      expect(url.split('#')[1]).toBe('/apps');
    });
  });

  describe('Phone list view', function() {
    beforeEach(function() {
      browser.get('app/index.html#/apps');
    });

    it('should open a detailed view of the first app', function() {
      element(by.css('.app-list-item a')).click();
      expect(element(by.css('h1')).getText()).toBe('HelpApp');
    });
  });
});