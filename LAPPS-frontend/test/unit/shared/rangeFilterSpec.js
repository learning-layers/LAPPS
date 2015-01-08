'use strict';

describe('Test of rangeFilter', function() {
  beforeEach(module('lappsFilters'));

  var $filter;

  beforeEach(inject(function(_$filter_) {
    $filter = _$filter_;
  }));

  it('should have a given length', function() {
    expect($filter('rangeFilter')(10)).toEqual(10);
  });
});