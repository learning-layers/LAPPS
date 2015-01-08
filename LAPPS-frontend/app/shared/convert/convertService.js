/**
 * @class convert
 * @memberOf lapps.lappsServices
 * @description This service handles various conversions
 */
(function() {
  angular.module('lappsServices').service(
          'convert',
          function() {
            /**
             * @function
             * @type string
             * @memberOf lapps.lappsServices
             * @param {number}
             *          utc timestamp.
             * @description Converts timestamp in date string day-month-year.
             */
            this.date = function(utc) {
              var d = new Date(utc);
              var m_names = new Array("January", "February", "March", "April",
                      "May", "June", "July", "August", "September", "October",
                      "November", "December");

              var curr_date = d.getDate();
              var curr_month = d.getMonth();
              var curr_year = d.getFullYear();
              return (curr_date + "-" + m_names[curr_month] + "-" + curr_year);
            };
            /**
             * @function
             * @type string
             * @memberOf lapps.lappsServices
             * @param {number|string}
             *          size size in KB to convert to MB.
             * @description Converts size in KB to MB with 1 digit after the
             *              comma. If size is less than 1024 the value is not
             *              changed. The string MB or KB is appended to the
             *              result (12.5 MB).
             */
            this.size = function(size) {
              size = parseInt(size, 10);
              if (size < 1024) {
                return size + " KB";
              } else {
                var div = Math.floor(size / 1024);
                var rem = size % 1024;
                return div + "." + Math.round(rem / 100) + " MB";
              }
            };
            /**
             * @function
             * @type string
             * @memberOf lapps.lappsServices
             * @param {string}
             *          url
             * @description Shortens a given urlby only displaying the TLD.
             */
            this.url = function(url) {
              if (typeof url === 'undefined' || url === null) { return ''; }

              var urlCopy = url.substring(0);

              var wwwPos = urlCopy.indexOf('www.');
              if (wwwPos >= 0) {
                urlCopy = urlCopy.substring(wwwPos + 4);
              }
              var slashPos = urlCopy.indexOf('/');
              if (slashPos >= 0) {
                urlCopy = urlCopy.substring(0, slashPos);
              }
              return urlCopy;
            }

          });
}).call(this);