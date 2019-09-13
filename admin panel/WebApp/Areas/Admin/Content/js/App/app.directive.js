//angular.module('app', [])

app.directive('ngConfirmClick', [
        function () {
            return {
                link: function (scope, element, attr) {
                    var msg = attr.ngConfirmClick || "Are you sure?";
                    var clickAction = attr.confirmedClick;
                    element.bind('click', function (event) {
                        if (window.confirm(msg)) {
                            scope.$eval(clickAction)
                        }
                    });
                }
            };
        }])
app.directive('onErrorSrc', function () {
    return {
        link: function (scope, element, attrs) {
            element.bind('error', function () {
                if (attrs.src != attrs.onErrorSrc) {
                    attrs.$set('src', attrs.onErrorSrc);
                }
            });
        }
    }
});

app.factory('httpLoadingInterceptor', ['$q', '$rootScope', function ($q, $rootScope) {
    var reqIteration = 0;
    return {
        request: function (config) {
            // Firing event only if current request was the first
            if (reqIteration === 0) {
                $rootScope.$broadcast('globalLoadingStart');
            }
            // Increasing request iteration
            reqIteration++;
            return config || $q.when(config);
        },
        requestError: function (config) {
            reqIteration--;
            if (!reqIteration) {
                $rootScope.$broadcast('globalLoadingEnd');
            }
            return config || $q.when(config);
        },
        response: function (config) {
            // Decreasing request iteration
            reqIteration--;
            // Firing event only if current response was came to the last request
            if (!reqIteration) {
                $rootScope.$broadcast('globalLoadingEnd');
            }
            return config || $q.when(config);
        },
        responseError: function (rejection) {
            reqIteration--;
            if (!reqIteration) {
                $rootScope.$broadcast('globalLoadingEnd');
            }
            return config || $q.when(config);
        }
    };
}])

// Injecting our custom loader interceptor
app.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.interceptors.push('httpLoadingInterceptor');
}])

app.directive('ionLoader', function () {
    return {
        restrict: 'E',
        replace: true,
        template: '<div id="preloader"><div class="loader" id="loader-1"></div></div>',
        link: function (scope, element) {
            angular.element(element).addClass('ion-hide');
            scope.$on('globalLoadingStart', function () {
                console.log("Loading started...");
                element.show();
                angular.element(element).toggleClass('ion-show ion-hide');
            });
            scope.$on('globalLoadingEnd', function () {
                console.log("Loading ended...");
                angular.element(element).toggleClass('ion-hide ion-show');
                element.hide();
            });
        }
    }
})

app.directive('validNumber', function () {
    return {
        require: '?ngModel',
        link: function (scope, element, attrs, ngModelCtrl) {
            if (!ngModelCtrl) {
                return;
            }

            ngModelCtrl.$parsers.push(function (val) {
                if (angular.isUndefined(val)) {
                    var val = '';
                }

                var clean = val.replace(/[^-0-9\.]/g, '');
                var negativeCheck = clean.split('-');
                var decimalCheck = clean.split('.');
                if (!angular.isUndefined(negativeCheck[1])) {
                    negativeCheck[1] = negativeCheck[1].slice(0, negativeCheck[1].length);
                    clean = negativeCheck[0] + '-' + negativeCheck[1];
                    if (negativeCheck[0].length > 0) {
                        clean = negativeCheck[0];
                    }

                }

                if (!angular.isUndefined(decimalCheck[1])) {
                    decimalCheck[1] = decimalCheck[1].slice(0, 2);
                    clean = decimalCheck[0] + '.' + decimalCheck[1];
                }

                if (val !== clean) {
                    ngModelCtrl.$setViewValue(clean);
                    ngModelCtrl.$render();
                }
                return clean;
            });

            element.bind('keypress', function (event) {
                if (event.keyCode === 32) {
                    event.preventDefault();
                }
            });
        }
    };
});
app.directive('numbersOnly', function () {
    return {
        require: 'ngModel',
        link: function (scope, element, attr, ngModelCtrl) {
            function fromUser(text) {
                if (text) {
                    var transformedInput = text.replace(/[^0-9]/g, '');

                    if (transformedInput !== text) {
                        ngModelCtrl.$setViewValue(transformedInput);
                        ngModelCtrl.$render();
                    }
                    return transformedInput;
                }
                return undefined;
            }
            ngModelCtrl.$parsers.push(fromUser);
        }
    };
});

app.directive('copyToClipboard', function () {
    return {
        restrict: 'A',
        link: function (scope, elem, attrs) {
            elem.click(function () {
                if (attrs.copyToClipboard) {
                    var $temp_input = $("<input>");
                    $("body").append($temp_input);
                    $temp_input.val(attrs.copyToClipboard).select();
                    document.execCommand("copy");
                    $temp_input.remove();
                    showSuccessToast("Project Url Copied");
                }
            });
        }
    };
});
app.directive("monthPicker", function () {
    function link(scope, element, attrs, controller) {
        element.datepicker({
            onSelect: function (dt) {
                scope.$apply(function () {
                    controller.$setViewValue(dt);
                });
            },
            minViewMode: 1,
            autoclose: true,
            dateFormat: 'dd-M-yy'
        });
    }
    return { require: 'ngModel', link: link };
});

app.directive('textArea', function ($compile) {
    return {
        require: "?ngModel",
        scope: {
            label: '@',
            options: '=?options'
        },
        template: "{{label}}<summernote config='options'></summernote>{{options|json}}",
        link: function (scope, element, attrs, ngModel) {
            //all of the code you had in here doesn't even get used in this case
            scope.options.height = 300;
        }
    };
});
app.directive("limitToMax", function () {//https://codepen.io/Jaydo/pen/yOMZJd
    return {
        link: function (scope, element, attributes) {
            element.on("keydown keyup", function (e) {
                if (Number(element.val()) > Number(attributes.max) &&
                      e.keyCode != 46 // delete
                      &&
                      e.keyCode != 8 // backspace
                    ) {
                    e.preventDefault();
                    element.val(attributes.max);
                }
            });
        }
    };

});
app.directive("preventTypingGreater", function () {//https://codepen.io/Jaydo/pen/yOMZJd
    return {
        link: function (scope, element, attributes) {
            var oldVal = null;
            element.on("keydown keyup", function (e) {
                if (Number(element.val()) > Number(attributes.max) &&
                      e.keyCode != 46 // delete
                      &&
                      e.keyCode != 8 // backspace
                    ) {
                    e.preventDefault();
                    element.val(oldVal);
                } else {
                    oldVal = Number(element.val());
                }
            });
        }
    };
});



app.directive('ngPatternRestrict', ['$log', function (a) {
    'use strict';
    return {
        restrict: 'A',
        require: '?ngModel',
        compile: function () {
            return function (a, b, c, d) {
                function e() {
                    return b[0].selectionStart
                }

                function f() {
                    var a = document.selection.createRange();
                    return a.moveStart('character', -b.val().length), a.text.length
                }

                function g() {
                    var a, b, c, d = window.getSelection(),
                        e = (d + '').length,
                        f = !1;
                    do a = (d + '').length, d.modify('extend', 'backward', 'character'), 0 === (d + '').length && (f = !0); while (a !== (d + '').length);
                    for (b = f ? a : a - e, d.collapseToStart(), c = b; c-- > 0;) d.modify('move', 'forward', 'character');
                    for (; e-- > 0;) d.modify('extend', 'forward', 'character');
                    return b
                }

                function h(a) {
                    b[0].setSelectionRange(a, a)
                }

                function i(a) {
                    var c = b[0].createTextRange();
                    c.collapse(!0), c.moveEnd('character', a), c.moveStart('character', a), c.select()
                }

                function j(a) {
                    var b, c = window.getSelection();
                    do b = (c + '').length, c.modify('extend', 'backward', 'line'); while (b !== (c + '').length);
                    for (c.collapseToStart() ; a--;) c.modify('move', 'forward', 'character')
                }

                function k(a) {
                    if (!/Opera/i.test(navigator.userAgent)) return 0;
                    a.focus(), document.execCommand('selectAll');
                    var b = window.getSelection().focusNode;
                    return (b || {}).selectionStart || 0
                }

                function l() {
                    b.val(w), angular.isUndefined(x) || z(x)
                }

                function m(a) {
                    w = a, x = y()
                }

                function n(c) {
                    if (!B) {
                        var e = b.val(),
                            f = b.prop('validity');
                        '' === e && 'text' !== b.attr('type') && f && f.badInput ? (c.preventDefault(), l()) : '' === e && 0 !== k(b[0]) ? (c.preventDefault(), l()) : A.test(e) ? m(e) : (c.preventDefault(), l()), d && a.$apply(function () {
                            d.$setViewValue(w)
                        })
                    }
                }

                function o() {
                    D || b.bind('input keyup click', n)
                }

                function p() {
                    D && (b.unbind('input', n), b.unbind('keyup', n), b.unbind('click', n), D = !1)
                }

                function q() {
                    var a = c.ngPatternRestrict ? c.ngPatternRestrict : c.pattern;
                    B = /\^?\.\*\$?/.test(a);
                    try {
                        A = RegExp(a)
                    } catch (b) {
                        throw 'Invalid RegEx string parsed for ngPatternRestrict: ' + a
                    }
                }

                function r(a, b) {
                    try {
                        return a() || !b
                    } catch (c) {
                        return !1
                    }
                }

                function s() {
                    var a = b[0];
                    y = r(function () {
                        return a.selectionStart
                    }) ? e : r(function () {
                        return document.selection
                    }, !0) ? f : g
                }

                function t() {
                    var a = b[0];
                    z = 'function' == typeof a.setSelectionRange ? h : 'function' == typeof a.createTextRange ? i : j
                }

                function u() {
                    C || (q(), w = b.val(), w || (w = ''), o(), s(), t(), C = !0)
                }

                function v() {
                    p()
                }
                var w, x, y, z, A = /.*/,
                    B = !0,
                    C = !1,
                    D = !1;
                c.$observe('ngPatternRestrict', q), c.$observe('pattern', q), a.$on('$destroy', v), u()
            }
        }
    }
}]);



app.directive('datepicker', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        compile: function () {
            return {
                pre: function (scope, element, attrs, ngModelCtrl) {
                    var format, dateObj;
                    format = (!attrs.dpFormat) ? 'd/m/yyyy' : attrs.dpFormat;
                    if (!attrs.initDate && !attrs.dpFormat) {
                        // If there is no initDate attribute than we will get todays date as the default
                        dateObj = new Date();
                        scope[attrs.ngModel] = dateObj.getDate() + '/' + (dateObj.getMonth() + 1) + '/' + dateObj.getFullYear();
                    } else if (!attrs.initDate) {
                        // Otherwise set as the init date
                        scope[attrs.ngModel] = attrs.initDate;
                    } else {
                        // I could put some complex logic that changes the order of the date string I
                        // create from the dateObj based on the format, but I'll leave that for now
                        // Or I could switch case and limit the types of formats...
                    }
                    // Initialize the date-picker
                    $(element).datepicker({
                        format: format,
                    }).on('changeDate', function (ev) {
                        // To me this looks cleaner than adding $apply(); after everything.
                        scope.$apply(function () {
                            ngModelCtrl.$setViewValue(ev.format(format));
                        });
                    });
                }
            }
        }
    }
});



//http://jsfiddle.net/vojtajina/U7Bz9/
//angular.module('scroll', []).directive('whenScrolled', function () {
//    return function (scope, elm, attr) {
//        var raw = elm[0];

//        elm.bind('scroll', function () {
//            if (raw.scrollTop + raw.offsetHeight >= raw.scrollHeight) {
//                scope.$apply(attr.whenScrolled);
//            }
//        });
//    };
//});

app.filter('limitWordsTo', function () {
    return function (stringData, numberOfWords) {
        //Get array of words (determined by spaces between words)
        var arrayOfWords = stringData.split(" ");

        //Get loop limit
        var loopLimit = numberOfWords > arrayOfWords.length ? arrayOfWords.length : numberOfWords;

        //Create variables to hold limited word string and array iterator
        var limitedString = '', i;
        //Create limited string bounded by limit passed in
        for (i = 0; i < loopLimit; i++) {
            if (i === 0) {
                limitedString = arrayOfWords[i];
            } else {
                limitedString = limitedString + ' ' + arrayOfWords[i];
            }
        }
        return limitedString;
    };
}); //End filter

app.filter("mydate", function () {
    var re = /\/Date\(([0-9]*)\)\//;
    return function (x) {
        if (x == null) {
            return
        }
        var m = x.match(re);
        if (m) return new Date(parseInt(m[1]));
        else return null;
    };
});
app.filter('unique', function () {
    return function (arr, field) {
        return _.uniq(arr, function (a) { return a[field]; });
    };
});
app.filter('jsonDate', ['$filter', function ($filter) {
    return function (input, format) {
        return (input)
               ? $filter('date')(parseInt(input.substr(6)), format)
               : '';
    }
}]);









app.factory('storageService', ['$rootScope', function ($rootScope) {
    return {
        get: function (key) {
            return localStorage.getItem(key);
        },
        set: function (key, data) {
            localStorage.setItem(key, data);
        }, remove: function (key) {
            return localStorage.removeItem(key);
        }, clear: function (key) {
            return localStorage.clear();
        }
    };
}]);


function ValidateRequired(attribute) {
    var retVal = false;
    $('.wrapper :input:visible[' + attribute + ']').each(function () {

        this.value = (this.value == "?" ? "" : this.value);

        if (!this.validity.valid) {
            $(this).focus();
            // break
            if (!$(this).attr("ngMessage") == false) {
                showInfoToast($(this).attr("ngMessage"))
                // toaster_message('error', 'Warning', $(this).attr("ngMessage"));
            }
            retVal = false;
            return false;
        }
        else {
            retVal = true;
        }
    });
    return retVal;
}



function toDataUrl(src, callback, outputFormat) {
    outputFormat = "image/png";
    var img = new Image();
    img.crossOrigin = 'Anonymous';
    img.onload = function () {
        var canvas = document.createElement('CANVAS');
        var ctx = canvas.getContext('2d');
        var dataURL;
        canvas.height = this.naturalHeight;
        canvas.width = this.naturalWidth;
        ctx.drawImage(this, 0, 0);
        dataURL = canvas.toDataURL(outputFormat);
        callback(dataURL);
        canvas = null;
    };
    img.src = src;
}

function ValidateRequired(attribute) {
    var retVal = false;
    $('.wrapper :input:visible[' + attribute + ']').each(function () {
        if ($(this).is("[email]")) {
            var re = /^\w+([-+.'][^\s]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
            var emailFormat = re.test($(this).val());
            if (!emailFormat) {
                $(this).focus();
                if (!$(this).attr("ngMessage") == false) {
                    showInfoToast($(this).attr("ngMessage"))
                    retVal = false;
                    return false;
                }
            }
        }
        this.value = (this.value == "?" ? "" : this.value);
        if (!this.validity.valid) {
            $(this).focus();
            // break
            if (!$(this).attr("ngMessage") == false) {
                showInfoToast($(this).attr("ngMessage"))
                // toaster_message('error', 'Warning', $(this).attr("ngMessage"));
            }
            retVal = false;
            return false;
        }
        else {
            retVal = true;
        }
    });
    return retVal;
}