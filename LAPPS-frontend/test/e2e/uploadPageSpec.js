'use strict';

describe('upload app page', function() {

  var license = element(by.model('newapp.license'));
  var name = element(by.model('newapp.name'));
  var tags = element(by.model('tags.value'));
  var size = element(by.model('newapp.size'));

  beforeEach(function() {
    browser.get('app/#/upload');
    browser.sleep(1000);
  });

  var hasClass = function(element, cls) {
    return element.getAttribute('class').then(function(classes) {
      return classes.split(' ').indexOf(cls) !== -1;
    });
  };

  it('length validation should work', function() {
    license.sendKeys('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa');
    expect(hasClass(license, 'ng-valid')).toBe(true);
  });

  it('required validation should work', function() {
    element(by.id('createApp')).click();
    expect(hasClass(name, 'ng-invalid')).toBe(true);
  });

  it('validation for size should work', function() {
    size.sendKeys('aaa');
    expect(hasClass(size, 'ng-invalid')).toBe(true);
  });

  it('should generate new field', function() {
    element.all(by.css('[ng-click="addAnotherField($event)"]')).get(0).click();
    expect(
            element.all(by.css('[ng-click="addAnotherField($event)"]')).get(1)
                    .isDisplayed()).toBeTruthy();
  });

});