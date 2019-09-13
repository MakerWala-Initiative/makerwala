//'use strict';

///* Controllers */
//http://demo.dotnetawesome.com/datatables-server-side-paging-sorting-filtering-angularjs
//var app = angular.module('app').controller('MainCtrl',
//    ['$scope', '$http', '$q', '$interval',
//        function ($scope, $http, $q, $interval) {

//        }]);


'use strict';

var modules = [
     'ngAnimate',
    //'ngRoute',
    //'ngAria',
    //'ngCookies',
    //'ngMessages',
    //'ngResource',
    //'ngSanitize',
    'ngTouch',
    //'ngStorage',
    //'ui.router',
    'ui.bootstrap',
    'datatables',
    'ui.filters',
    'cp.ngConfirm',
    'cgBusy',
    'angularjs-dropdown-multiselect'
   // 'ui.directives'
    //'ngFileUpload',
    //'ngTagsInput',
    //'summernote',
    //'ngSanitize'
    //'angularCroppie'
    //'ui.utils',
    ////'ui.load',
    //'ui.jq',
    //'oc.lazyLoad',
    //'pascalprecht.translate',
    //'watchers',
    //'ui-notification',
    //'ui.select',
    //'Constants',
    //'as.sortable',
    //'froala',
    //'daterangepicker'
    //'naif.base64',

];

var app = angular.module('app', modules)
app.directive('errSrc', function () {
    return {
        link: function (scope, element, attrs) {
            var defaultSrc = attrs.src;
            element.bind('error', function () {
                if (attrs.errSrc) {
                    element.attr('src', attrs.errSrc);
                }
                else if (attrs.src) {
                    element.attr('src', defaultSrc);
                }
            });
        }
    }
});