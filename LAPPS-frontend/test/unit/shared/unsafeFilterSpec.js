'use strict';

describe('Test of unsafe', function() {
  beforeEach(module('lappsFilters'));

  var $filter;

  beforeEach(inject(function(_$filter_) {
    $filter = _$filter_;
  }));

  it('should make given html a trusted value', function() {
    expect($filter('unsafe')('<a href="#"></a>').$$unwrapTrustedValue())
            .toEqual('<a href="#"></a>');
  });
});