/**
 * @class userPage
 * @memberof lapps.lappsDirectives
 * @description This directive is used for managing dropdown menu in of
 *              platforms
 */
(function() {
  angular
          .module('lappsDirectives')
          .directive(
                  'customDropdown',
                  function($compile) {
                    return {
                      restrict: 'E',
                      scope: {
                        items: '=dropdownData',
                        doSelect: '&selectVal',
                        selectedItem: '=preselectedItem'
                      },
                      link: function(scope, element, attrs) {
                        var html = '';
                        switch (attrs.menuType) {
                        case "button":
                          html += '<div class="btn-group"><button class="btn button-label btn-default">Platforms</button><button class="btn btn-default dropdown-toggle" name ="platform" data-toggle="dropdown" required><span class="caret"></span></button>';
                          break;
                        default:
                          html += '<div class="dropdown"><a class="dropdown-toggle" role="button" data-toggle="dropdown"  href="javascript:;">Dropdown<b class="caret" required></b></a>';
                          break;
                        }
                        html += '<ul class="dropdown-menu"><li ng-repeat="item in items"><a tabindex="-1" data-ng-click="selectVal(item)" ng-model = "newapp.platform">{{item.name}}</a></li></ul></div>';
                        element.append($compile(html)(scope));
                        for (var i = 0; i < scope.items.length; i++) {
                          if (scope.items[i].id === scope.selectedItem) {
                            scope.bSelectedItem = scope.items[i];
                            break;
                          }
                        }
                        scope.selectVal = function(clickedItem) {
                          switch (attrs.menuType) {
                          case "button":
                            $('button.button-label', element).html(
                                    clickedItem.name);
                            break;
                          default:
                            $('a.dropdown-toggle', element)
                                    .html(
                                            '<b class="caret"></b> '
                                                    + clickedItem.name);
                            break;
                          }
                          scope.doSelect({
                            selectedVal: clickedItem.id
                          });
                        };
                        scope.selectVal(scope.bSelectedItem);
                      }
                    };
                  })
}).call(this);
