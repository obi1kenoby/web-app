const today = new Date();
const months = ["Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
    "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"];

$(document).ready(function () {
    fillYear();
    fillMonth();
    fillDates(31);
});

$('#bth-month').change(function () {
    let value = parseInt(this.options[this.selectedIndex].value);
    let dates = document.getElementById('bth-date');
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
    let year = document.getElementById('bth-year');
    $(year).empty();
    let currYear = today.getFullYear();
    for (let i = currYear - 37; i < currYear - 7; i++) {
        let option = document.createElement('option');
        option.value = i;
        option.innerHTML = i;
        year.appendChild(option);
    }
}

function fillMonth() {
    let month = document.getElementById('bth-month');
    $(month).empty();
    let val = 1;
    for (let i in months) {
        let option = document.createElement('option');
        option.value = val;
        option.innerHTML = months[i];
        month.appendChild(option);
        val++;
    }
}

function fillDates(n) {
    let date = document.getElementById('bth-date');
    $(date).empty();
    for (let i = 1; i <= n; i++) {
        let option = document.createElement('option');
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