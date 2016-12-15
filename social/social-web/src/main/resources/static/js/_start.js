var main = {};

main.defineDefaultLoader = function () {
    $.ajaxPrefilter((options, _, jqXHR) => {
        main.toggleScroll();
        main.toggleLoader();
        jqXHR.done(() => {
            main.toggleScroll();
            main.toggleLoader();
        }).fail(() => {
            main.toggleScroll();
            main.toggleLoader();
        });
    });
}

main.defineReponsiveNavToggle = function () {
    $('#nav-toggler').click(function () {
        $('#nav-to-toggle').toggleClass('is-active');
        $('#nav-toggler').toggleClass('is-active');
    });
}

main.toggleScroll = function () {
    let windowEvents = jQuery._data(window, "events");
    if (!!windowEvents && !!windowEvents.mousewheel) {
        $(window).unbind("mousewheel");
    } else {
        $(window).bind("mousewheel", function (event) {
            event.preventDefault();
        });
    }
}

main.toggleLoader = function () {
    $('#cog-loader').toggle();
    $('.nav-icon').toggle();
}

$(function () { 
    main.defineReponsiveNavToggle();
    main.defineDefaultLoader();
});