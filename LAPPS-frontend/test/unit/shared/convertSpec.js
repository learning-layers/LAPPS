'use strict';

describe('Test of platformService', function() {
  beforeEach(module('lappsServices'));

  var convertService;

  beforeEach(function() {
    inject(function(convert) {
      convertService = convert;
    })
  });

  it('should convert the dates correctly', function() {
    expect(convertService.date(1420670339000)).toBe('7-January-2015');
    expect(convertService.date(1431993900000)).toBe('19-May-2015');
  });

  it('should convert the size from KB to MB', function() {
    expect(convertService.size(35)).toBe('35 KB');
    expect(convertService.size(5003685)).toBe('4886.4 MB');
  });
  it('should shorten the url', function() {
    expect(convertService.url('www.google.com/bla')).toBe('google.com');
  });

});