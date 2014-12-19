/**
 * @class uploadPage
 * @memberOf lapps.lappsDirectives
 * @description This directive is used for uploadPage
 */
(function() {
  angular
          .module('lappsDirectives')
          .directive('uploadPageDirective', function() {
            return {
              scope: true,
              restrict: 'E',
              templateUrl: 'components/featuredList/featuredListView.html',
              controller: 'uploadPageCtrl'
            };
          })
          // directive for dropdown menu of platforms
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
                          html += '<div class="btn-group"><button class="btn button-label btn-default">Platforms</button><button class="btn btn-default dropdown-toggle" data-toggle="dropdown"><span class="caret"></span></button>';
                          break;
                        default:
                          html += '<div class="dropdown"><a class="dropdown-toggle" role="button" data-toggle="dropdown"  href="javascript:;">Dropdown<b class="caret"></b></a>';
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
                        scope.selectVal = function(item) {
                          scope.doSelect({
                            selectedVal: item.name
                          });
                        };
                        scope.selectVal(scope.bSelectedItem);
                      }
                    };
                  })
          // validation for input. Checks whether required input is empty or not
          .directive('validateInput', function() {
            return {
              restrict: 'A',
              require: '^form',
              link: function(scope, el, attrs, formCtrl) {
                // find the text box element, which has the 'name' attribute
                var inputElement = el[0].querySelector("[name]");
                // convert the native text box element to an angular element
                var inputAngularElement = angular.element(inputElement);
                // get the name on the text box so we know the property to check
                // on the form controller
                var inputName = inputAngularElement.attr('name');
                // only apply the has-error class after the user leaves the text
                // box
                inputAngularElement.bind('blur', function() {
                  el.toggleClass('has-error', formCtrl[inputName].$invalid);
                })
              }
            }
          });
}).call(this);