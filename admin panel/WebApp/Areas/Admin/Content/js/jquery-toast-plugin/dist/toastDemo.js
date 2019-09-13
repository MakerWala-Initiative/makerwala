(function ($) {
    showSuccessToast = function (text) {
        'use strict';
        resetToastPosition();
        $.toast({
            heading: 'Success',
            text: text,
            showHideTransition: 'slide',
            icon: 'success',
            loaderBg: '#f96868',
            position: 'bottom-right'
        })
    };
    showInfoToast = function (text) {
        'use strict';
        resetToastPosition();
        $.toast({
            heading: 'info',
            text: text,
            showHideTransition: 'slide',
            icon: 'info',
            loaderBg: '#f96868',
            position: 'bottom-right'
        })
    };
    //showInfoToast = function(){
    //    'use strict';
    //    resetToastPosition();
    //    $.toast({
    //        heading: 'Info',
    //        text: 'And these were just the basic demos! Scroll down to check further details on how to customize the output.',
    //        showHideTransition: 'slide',
    //        icon: 'info',
    //        loaderBg: '#46c35f',
    //        position: 'top-right'
    //    })
    //};
    showWarningToast = function (text) {
        'use strict';
        resetToastPosition();
        $.toast({
            heading: 'Warning',
            text: text,
            showHideTransition: 'slide',
            icon: 'warning',
            loaderBg: '#57c7d4',
            position: 'bottom-right'
        })
    };
    showDangerToast = function(){
        'use strict';
        resetToastPosition();
        $.toast({
            heading: 'Danger',
            text: 'And these were just the basic demos! Scroll down to check further details on how to customize the output.',
            showHideTransition: 'slide',
            icon: 'error',
            loaderBg: '#f2a654',
            position: 'top-right'
        })
    };
    showToastPosition = function(position) {
        'use strict';
        resetToastPosition();
        $.toast({
            heading: 'Positioning',
            text: 'Specify the custom position object or use one of the predefined ones',
            position: String(position),
            icon: 'info',
            stack: false,
            loaderBg: '#f96868'
        })
    }
    showToastInCustomPosition = function() {
        'use strict';
        resetToastPosition();
        $.toast({
            heading: 'Custom positioning',
            text: 'Specify the custom position object or use one of the predefined ones',
            icon: 'info',
            position: {
                left: 120,
                top: 120
            },
            stack: false,
            loaderBg: '#f96868'
        })
    }
    resetToastPosition = function() {
        $('.jq-toast-wrap').removeClass('bottom-left bottom-right top-left top-right mid-center'); // to remove previous position class
        $(".jq-toast-wrap").css({"top": "", "left": "", "bottom":"", "right": ""}); //to remove previous position style
    }

    /*Project Toast*/

    showPostProjectToast = function (hading,Body) {
        'use strict';
        resetToastPosition();
        $.toast({
            heading: hading,
            text: Body,
            showHideTransition: 'slide',
            icon: 'success',
            loaderBg: '#53c183',
            hideAfter: 5000,
            position: 'bottom-left',
           // bgColor : '#23B65D'
        })
    }

    showOrderProjectToast = function (hading, Body) {
        'use strict';
        resetToastPosition();
        $.toast({
            heading: hading,
            text: Body,
            showHideTransition: 'slide',
            icon: 'info',
            loaderBg: '#53c183',
            hideAfter: 5000,
            position: 'bottom-left',
            // bgColor : '#23B65D'
        })
    }

})(jQuery);
