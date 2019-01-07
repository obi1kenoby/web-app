$('.item').mouseenter(function () {
    if (this.innerHTML === 'Admin') {
        $(this).css('color', '#e57373');
    } else {
        $(this).css('color', '#81d4fa');
    }
});

$(".item").mouseleave(function () {
    $(this).css('color', '#ffffff');
});

var inputs = $('.input-box').toArray();

function isInputEmpty(elements) {
    var array = new Array();
    for (i in elements) {
        var i = $(elements[i]);
        if (!$(i).val()) {
            array.push(i);
        }
    }
    return array;
}

function close() {
    $('#login-modal').css('display', 'none');
    for (i in inputs) {
        $(inputs[i]).val('');
    }
}

$('.input-box').focus(function () {
    for (i in inputs) {
        $(inputs[i]).css('border-color', '#e0e0e0');
    }
});

$("#login-btn").click(function () {
    $('#login-modal').css('display', 'block');
});

$(document).click(function (e) {
    if (!$(e.target).closest('#login-btn, #login-content').length) {
        close();
    }
});

$('.close, #login-submit').click(function () {
    if ($(this).attr('id') == 'login-submit') {
        var array = $('.input-box').toArray();
        var elements = isInputEmpty(array);
        if (elements.length == 0) {
            close();
        } else {
            for (i in elements) {
                $(elements[i]).css('border-color', '#ef5350');
            }
        }
    } else {
        close();
    }
});

$('#dep-btn, #deps').mouseenter(function () {
    $('#deps').css('display', 'block');
});

$('#dep-btn, #deps').mouseleave(function () {
    $('#deps').css('display', 'none');
});

$('#sub-btn, #subs').mouseenter(function () {
    $('#subs').css('display', 'block');
});

$('#sub-btn, #subs').mouseleave(function () {
    $('#subs').css('display', 'none');
});

$('#search').focus(function () {
    $('#search-box').css({
        '-moz-box-shadow': '0 0 16px 3px #ffffff',
        '-webkit-box-shadow': '0 0 16px 3px #ffffff',
        'box-shadow': '0 0 16px 3px #ffffff'
    });
});

$('#search').focusout(function () {
    $('#search-box').css({
        '-moz-box-shadow': '',
        '-webkit-box-shadow': '',
        'box-shadow': ''
    });
});

let departments = getDepartments();

function getDepartments() {
    let departments = undefined;
    $.ajax({
        url: '/api/department',
        dataType: "json",
        async: false,
        type: "GET",
        success: function (data) {
            departments = data;
        }
    });
    return departments;
}

$( document ).ready(function() {
    let parent =  document.getElementById('deps');
    for (i in departments) {
        let div = document.createElement('div');
        div.innerHTML = departments[i].name;
        parent.appendChild(div);
    }
});