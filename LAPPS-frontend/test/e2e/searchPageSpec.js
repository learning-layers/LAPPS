'use strict';

describe('Browse Search Page', function() {
  it('should open search page', function() {
    browser.get('app/index.html');
    browser.sleep(500);
    element(by.css('.search-bar.hidden-xs')).all(by.model('searchQuery'))
            .sendKeys('a');
    browser.actions().sendKeys(protractor.Key.ENTER).perform();
    browser.getLocationAbsUrl().then(function(url) {
      expect(url.split('#')[1]).toContain('/search');
    });
  });
  it('should have some results', function() {
    expect(element.all(by.css('.app-list-item')).count()).toBeGreaterThan(1);
  });

  it('should react if sorting changes',
          function() {
            var items = element.all(by.css('.app-list-item'));
            var name1 = items.get(0).all(by.css('.title h3')).getAttribute(
                    'innerHTML');
            element(by.cssContainingText('option', 'rating')).click()
            var items2 = element.all(by.css('.app-list-item'));
            var name2 = items.get(0).all(by.css('.title h3')).getAttribute(
                    'innerHTML');
            expect(name1).not.toBe(name2);

          });

  it('should go an app page', function() {
    element(by.css('.app-list-item')).click();

    browser.getLocationAbsUrl().then(function(url) {
      expect(url.split('#')[1]).toContain('/apps/');
    });
  });
});