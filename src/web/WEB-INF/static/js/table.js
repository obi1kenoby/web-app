var dates;
var students;
var marks;
var currentDate;


if (currentDate == undefined){
    currentDate = new Date();
}

var subject = decodeURIComponent(window.location.pathname.split('/')[2]);
var dep = window.location.pathname.split('/')[1];

var currentYear = currentDate.getFullYear();
var currentMonth;
if (currentDate.getMonth() + 1 < 10) {
    currentMonth = '0' + (currentDate.getMonth() + 1).toString();
} else {
    currentMonth = (currentDate.getMonth() + 1).toString();
}

$.ajax({
    url: "/dates/" + currentYear + '-' + currentMonth,
    dataType: "json",
    async: false,
    type: "GET",
    success: function (data) {
        dates = data;
    }
});

$.ajax({
    url: "/" + dep + "/students",
    dataType: "json",
    async: false,
    type: "GET",
    success: function (data) {
        students = data;
    }
});

$.ajax({
    url: "/" + dep + "/" + subject + "/" + currentDate.getFullYear() + '-' + (currentDate.getMonth() + 1) + "/marks",
    dataType: "json",
    async: false,
    type: "GET",
    success: function (data) {
        marks = data;
    }
});

/* CHECKING IF STUDENT HAS MARK IN CURRENT DATE */

function isStudentHaveValue(dates, marks, student) {
    var object = {};
    for (let i in marks) {
        for (let j in dates) {
            if (dates[j].year === marks[i].date.year && dates[j].monthValue === marks[i].date.monthValue &&
                dates[j].dayOfMonth === marks[i].date.dayOfMonth && marks[i].student.id === student.id && marks[i].subject.name === subject) {
                object.id = marks[i].id;
                object.date = marks[i].date;
                object.value = marks[i].value;
                return object;
            }
        }
    }
    object.id = 0;
    object.date = null;
    object.value = 0;
    return object;
}

/* START MAIN CONTAINER */

var tableContainer = document.createElement('div');
tableContainer.id = 'main';
tableContainer.classList.add('container');
document.body.appendChild(tableContainer);

var row = document.createElement('div');
row.classList.add('row', 'align-items-center');
tableContainer.appendChild(row);

/* END MAIN CONTAINER */

/* START LEFT TOGGLE */

var leftContainer = document.createElement('div');
leftContainer.classList.add('col', 'text-center');
row.appendChild(leftContainer);

var leftImg = document.createElement('img');
leftImg.id = 'prev-month';
leftImg.setAttribute("src", "/image/left.png");
leftImg.setAttribute("width", "30");
leftImg.setAttribute("height", "30");
leftImg.setAttribute("alt", "left");
leftContainer.appendChild(leftImg);

/* END LEFT TOGGLE CONTENT */

/* START TABLE CONTENT */

var tableContent = document.createElement('div');
tableContent.classList.add('col-10');
row.appendChild(tableContent);

var table = document.createElement('TABLE');
table.classList.add('table', 'table-bordered', 'table-striped', 'table-sm');
tableContent.appendChild(table);

var headTableRow = document.createElement('TR');
headTableRow.classList.add('table-success');
table.appendChild(headTableRow);

var studentsCell = document.createElement('TD');
studentsCell.innerHTML = 'Ф.И.О';
headTableRow.appendChild(studentsCell);

for(var i in dates){
    var date = document.createElement('TD');
    date.classList.add('day');
    var day = dates[i].dayOfMonth;
    if (day < 10) {
        date.innerHTML = '0' + day;
    } else {
        date.innerHTML = day;
    }
    headTableRow.appendChild(date);
}

for (var i in students) {
    var studTableRow = document.createElement('TR');
    var student = document.createElement('TD');
    student.classList.add('std');
    student.id = students[i].id;
    var a = document.createElement('a');
    a.innerHTML = students[i].last_name + ' ' + students[i].first_name;
    a.href = '/students/' + students[i].id;
    student.appendChild(a);
    studTableRow.appendChild(student);
    for (var j in dates) {
        var tableCell = document.createElement('TD');
        var object = isStudentHaveValue(dates, marks, students[i]);
        var fDate = dates[j].year + '-' + dates[j].monthValue + '-' + dates[j].dayOfMonth;
        if (object.date !== null) {
            if (object.date.dayOfMonth === dates[j].dayOfMonth) {
                tableCell.innerHTML = object.value;
                tableCell.id = students[i].id + '.' + fDate + '.' + object.id;
            } else {
                tableCell.innerHTML = '';
                tableCell.id = students[i].id + '.' + fDate + '.' + 0;
            }
        } else {
            tableCell.innerHTML = '';
            tableCell.id = students[i].id + '.' + fDate + '.' + 0;
        }
        tableCell.className = 'value';
        studTableRow.appendChild(tableCell);
    }
    table.appendChild(studTableRow);
}

/* END TABLE CONTENT */

/* START RIGHT TOGGLE CONTENT */

var rightContainer = document.createElement('div');
rightContainer.classList.add('col', 'text-center');
row.appendChild(rightContainer);

var rightImg = document.createElement('img');
rightImg.id = 'next-month';
rightImg.setAttribute("src", "/image/right.png");
rightImg.setAttribute("width", "30");
rightImg.setAttribute("height", "30");
rightImg.setAttribute("alt", "left");
rightContainer.appendChild(rightImg);

/* END RIGHT TOGGLE CONTENT */

/* START MENU CONTENT */

var menuContainer = document.createElement('div');
menuContainer.id = 'menuContainer';
menuContainer.classList.add('container');
document.body.appendChild(menuContainer);

var menuRow = document.createElement('div');
menuRow.classList.add('row');
menuContainer.appendChild(menuRow);

var departmentContent = document.createElement('div');
departmentContent.classList.add('col-4', 'text-center');
menuRow.appendChild(departmentContent);

var depSelector = document.createElement('select');
depSelector.classList.add('form-control');
depSelector.id = 'depSelector';
departmentContent.appendChild(depSelector);

var subjectContent = document.createElement('div');
subjectContent.classList.add('col', 'text-center');
menuRow.appendChild(subjectContent);

var subSelector = document.createElement('select');
subSelector.classList.add('form-control');
subSelector.id = 'subSelector';
subjectContent.appendChild(subSelector);

var monthContent = document.createElement('div');
subjectContent.classList.add('col', 'text-center');
menuRow.appendChild(monthContent);

var monthSelector = document.createElement('select');
monthSelector.id = 'month';
monthSelector.classList.add('form-control');

monthContent.appendChild(monthSelector);

var yearContent = document.createElement('div');
yearContent.classList.add('col', 'text-center');
menuRow.appendChild(yearContent);

var yearInput = document.createElement('input');
yearInput.id = 'year-input';
yearInput.classList.add('form-control');
yearInput.setAttribute("type", "text");
yearInput.setAttribute("placeholder", currentDate.getFullYear());
yearContent.appendChild(yearInput);

var buttonContent = document.createElement('div');
buttonContent.classList.add('col', 'text-center');
menuRow.appendChild(buttonContent);

var applyButton = document.createElement('button');
applyButton.id = 'applyBtn';
applyButton.classList.add('btn', 'btn-success');
applyButton.innerHTML = 'Выбрать';
buttonContent.appendChild(applyButton);

/* END MENU CONTENT */

const months = [
    {eng: 'JANUARY', rus: 'ЯНВАРЬ', num: 0},
    {eng: 'FEBRUARY', rus: 'ФЕВРАЛЬ', num: 1},
    {eng: 'MARCH', rus: 'МАРТ', num: 2},
    {eng: 'APRIL', rus: 'АПРЕЛЬ', num: 3},
    {eng: 'MAY', rus: 'МАЙ', num: 4},
    {eng: 'JUNE', rus: 'ИЮНЬ', num: 5},
    {eng: 'JULE', rus: 'ИЮЛЬ', num: 6},
    {eng: 'AUGUST', rus: 'АВГУСТ', num: 7},
    {eng: 'SEPTEMBER', rus: 'СЕНТЯБРЬ', num: 8},
    {eng: 'OCTOBER', rus: 'ОКТЯБРЬ', num: 9},
    {eng: 'NOVEMBER', rus: 'НОЯБРЬ', num: 10},
    {eng: 'DECEMBER', rus: 'ДЕКАБРЬ', num: 11}
];

for (var i in months) {
    var monthOption = document.createElement('option');
    monthOption.value = months[i].num;
    monthOption.innerHTML = months[i].rus;
    if (currentDate.getMonth() === months[i].num) {
        monthOption.selected = "true";
    }
    monthSelector.appendChild(monthOption);
}