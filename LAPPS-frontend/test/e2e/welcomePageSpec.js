'use strict';

describe('Browse Welcome Page', function() {

  it('should redirect index.html to index.html#/apps', function() {
    browser.get('app/index.html');
    browser.sleep(1000);
    browser.getLocationAbsUrl().then(function(url) {
      expect(url.split('#')[1]).toBe('/apps?tab=new');
      // expect($('#help-text').isDisplayed()).toBeTruthy();
    });
  });

  it('should NOT show a platform notification message on 2nd visit',
          function() {
            browser.get('app/index.html');
            browser.sleep(1000);
            expect($('#help-text').isDisplayed()).toBeFalsy();
          });

  it('should flip through different apps in carousel', function() {
    browser.sleep(1000);
    var name = element(by.css('.active .carousel-caption.ng-scope h1'))
            .getAttribute('innerHTML');

    element(by.css('.right.carousel-control')).click();
    browser.sleep(1000);
    element(by.css('.right.carousel-control')).click();

    var name2 = element(by.css('.active .carousel-caption.ng-scope h1'))
            .getAttribute('innerHTML');
    expect(name).not.toBe(name2);
  });

  it('should change the url when changing tabs (rating)', function() {
    $$('.lead.nav.nav-justified.nav-pills li').click();
    browser.getLocationAbsUrl().then(function(url) {
      expect(url).toContain('tab=top');
    });
  });

  it('should have some app tiles on display', function() {
    expect(element.all(by.css('.app-list-item')).count()).toBeGreaterThan(1);
  });

  it('should go to search page when searching and back when clicking on logo',
          function() {
            element(by.css('.search-bar.hidden-xs')).all(
                    by.model('searchQuery')).sendKeys('a');
            browser.actions().sendKeys(protractor.Key.ENTER).perform();
            browser.sleep(1000);
            browser.getLocationAbsUrl().then(function(url) {
              expect(url.split('#')[1]).toContain('/search');
              element(by.css('.navbar-brand')).click();
              browser.getLocationAbsUrl().then(function(url) {
                expect(url.split('#')[1]).toContain('/apps');
              });
            });
          });

  it('should go an app page', function() {
    element(by.css('.app-list-item')).click();

    browser.getLocationAbsUrl().then(function(url) {
      expect(url.split('#')[1]).toContain('/apps/');
    });
  });
});