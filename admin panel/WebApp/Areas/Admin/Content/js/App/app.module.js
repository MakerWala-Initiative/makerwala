//(function () {
//    'use strict';

//    angular.module('app', []);

//})();
//var app = angular.module('app', []);

app.filter('htmlToPlaintext', function () {
    return function (text) {
        return text ? String(text).replace(/<[^>]+>/gm, '') : '';
    };
}
);