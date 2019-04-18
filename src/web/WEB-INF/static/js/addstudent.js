var today = new Date();
var months = ["Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
    "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"];

$(document).ready(function () {
    fillYear();
    fillMonth();
    fillDates(31);
});

$('#bth-month').change(function () {
    var value = parseInt(this.options[this.selectedIndex].value);
    var dates = document.getElementById('bth-date');
    $(dates).empty();
    if (value < 3) {
        if (value === 1) {
            fillDates(31);
        } else {
            fillDates(28);
        }
    } else {
        if (value < 8) {
            if (value % 2 === 0) {
                fillDates(30);
            } else {
                fillDates(31);
            }
        } else {
            if (value % 2 === 0) {
                fillDates(31);
            } else {
                fillDates(30);
            }
        }
    }
});

function fillYear() {
    var year = document.getElementById('bth-year');
    $(year).empty();
    var currYear = today.getFullYear();
    for (var i = currYear - 37; i < currYear - 7; i++) {
        var option = document.createElement('option');
        option.value = i;
        option.innerHTML = i;
        year.appendChild(option);
    }
}

function fillMonth() {
    var month = document.getElementById('bth-month');
    $(month).empty();
    var val = 1;
    for (var i in months) {
        var option = document.createElement('option');
        option.value = val;
        option.innerHTML = months[i];
        month.appendChild(option);
        val++;
    }
}

function fillDates(n) {
    var date = document.getElementById('bth-date');
    $(date).empty();
    for (var i = 1; i <= n; i++) {
        var option = document.createElement('option');
        option.value = i;
        option.innerHTML = i;
        date.appendChild(option);
    }
}

$('#student-modal').on('hidden.bs.modal', function () {
    $('#stud-dep').empty();
    $('#f-name').val('');
    $('#l-name').val('');
    $('#pass').val('');
    $('#c-pass').val('');
    $('#mail').val('');
    $('#photo').val('');
    fillYear();
    fillMonth();
    fillDates(31);
});