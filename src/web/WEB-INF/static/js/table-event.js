$(document).ready(function () {

    var tdId;

    /* FILL DEPARTMENTS AND SUBJECTS IN MENU (START) */

    function fillDepartments() {
        var depHeaderSelector = document.getElementById('my-departments');
        var childNodes = depHeaderSelector.childNodes;
        var depSelector = document.getElementById('depSelector');
        for (var i in childNodes) {
            if (childNodes[i].tagName === 'A') {
                var depOption = document.createElement('option');
                depOption.text = childNodes[i].text;
                depOption.setAttribute('dep-index', childNodes[i].getAttribute('dep-index'));
                depSelector.appendChild(depOption);
            }
        }
    }

    function fillSubjects() {
        var subHeaderSelector = document.getElementById('my-subjects');
        var childNodes = subHeaderSelector.childNodes;
        var subSelector = document.getElementById('subSelector');
        for (var i in childNodes) {
            if (childNodes[i].tagName === 'A') {
                var subOption = document.createElement('option');
                subOption.text = childNodes[i].text;
                subOption.setAttribute('sub-index', childNodes[i].getAttribute('sub-index'));
                subSelector.appendChild(subOption);
            }
        }
    }

    fillDepartments();
    fillSubjects();

    /* FILL DEPARTMENTS AND SUBJECTS IN MENU (END) */

    /* CLICK FUNCTION FOR VALUE (TD) */

    var values = document.getElementsByClassName('value');

    for (var i = 0; i < values.length; i++) {
        values[i].onclick = function () {
            editMark(this);
        }
    }

    function editMark(element) {
        var array = element.id.split(".");
        var mark_id = Number(array[array.length - 1]); // defined mark id;
        markId = mark_id;
        $("#valueModal").modal();
        var nameCell = element.parentElement.firstChild.firstChild;
        var markUser = document.getElementById('mark-user');
        markUser.innerHTML = nameCell.innerHTML;
        var subjects = document.getElementById('subSelector');
        var markSubject = document.getElementById('mark-subject');
        markSubject.innerHTML = subjects.value;
        var date = element.id.split('.')[1];
        var year = parseInt(date.split('-')[0]);
        var month = date.split('-')[1];
        var day = date.split('-')[2];
        var d;
        var m;
        if (day < 10) {
            d = '0' + day;
        } else {
            d = day;
        }
        if (month < 10) {
            m = '0' + month;
        } else {
            m = month;
        }
        var markDate = document.getElementById('mark-date');
        markDate.innerHTML = d + '.' + m + '.' + year;
        var saveButton = document.getElementById('save-btn');
        var markValue = document.getElementById('mark-value');
        if (mark_id !== 0) {
            saveButton.innerHTML = 'Изменить';
            markValue.value = element.innerHTML;
        } else {
            saveButton.innerHTML = 'Сохранить';
            markValue.value = '5';
        }
        tdId = element.id;
    }

    /* SAVE BUTTON EVENT */

    var saveButton = document.getElementById('save-btn');
    saveButton.onclick = function () {
        var markValue = document.getElementById('mark-value').value;
        var markDate = document.getElementById('mark-date').innerHTML;
        var subSelector = document.getElementById('subSelector');
        var markSubject = subSelector.options[subSelector.selectedIndex];
        var markSubjectId = parseInt(markSubject.getAttribute('sub-index'));
        var markInfo = document.getElementById('mark-user').innerHTML;
        var markStudent = markInfo.split(' ')[0] + '-' + markInfo.split(' ')[1];
        $.ajax({
            url: '/marks/' + markId + '/' + markValue + '/' + markSubjectId + '/' + markStudent + '/' + markDate + '/save',
            dataType: "json",
            async: false,
            type: "POST",
            success: function (data) {
                document.getElementById(tdId).textContent = markValue;
                document.getElementById(tdId).id = tdId.split('.')[0] + '.' + tdId.split('.')[1] + '.' + data.id.toString();
                $('#valueModal').modal('hide');
            }
        });
    }

    /* DELETE BUTTON EVENT */

    var deleteButton = document.getElementById('delete-btn');
    deleteButton.onclick = function () {
        $.ajax({
            url: "/marks/" + markId + "/delete",
            dataType: "json",
            async: false,
            type: "GET",
            success: function () {
                document.getElementById(tdId).textContent = "";
                document.getElementById(tdId).id = tdId.split('.')[0] + '.' + tdId.split('.')[1] + '.' + 0;
                $('#valueModal').modal('hide');
            }
        });
    }

    /* PREVIOUS MONTH BUTTON EVENT */

    var previousMonth = document.getElementById('prev-month');
    previousMonth.onclick = function () {
        currentDate.setMonth(currentDate.getMonth() - 1);
        var dates = prevMonth(currentDate);
        var marks = getMarks(currentDate);
        for (var i in months) {
            if (months[i].num === currentDate.getMonth()) {
                $('#month').val(months[i].num);
            }
        }
        $('#year-input').val(currentDate.getFullYear());
        tableFill(dates, marks);
    }

    /* NEXT MONTH BUTTON EVENT */

    var nextMonth = document.getElementById('next-month');
    nextMonth.onclick = function () {
        currentDate.setMonth(currentDate.getMonth() + 1);
        var dates = nxMonth(currentDate);
        var marks = getMarks(currentDate);
        for (var i in months) {
            if (months[i].num === currentDate.getMonth()) {
                $('#month').val(months[i].num);
            }
        }
        $('#year-input').val(currentDate.getFullYear());
        tableFill(dates, marks);
    }

    /* APPLY BUTTON EVENT */

    var applyBtn = document.getElementById('applyBtn');
    applyBtn.onclick = function () {
        var sub = document.getElementById('subSelector');
        var subject = sub.options[sub.selectedIndex].innerHTML;
        var dep = document.getElementById('depSelector');
        var department = dep.options[dep.selectedIndex];
        $.ajax({
            url: '/' + subject + '/' + parseInt(department.getAttribute('dep-index')) + '/subject',
            dataType: "json",
            async: false,
            type: "GET",
            success: function (data) {
                if (data === false) {
                    alert("This department does not include subject " + subject);
                } else {
                    window.location.href = '/' + parseInt(department.getAttribute('dep-index')) + '/' + subject + '/marks';
                    currentDate = new Date('#year-input').val(), (parseInt($('#month').find(":selected").attr("value")) + 1);
                }
            }
        });
    };

    /* GET DATES FROM PREVIOUS MONTH */

    function prevMonth(date) {
        var year = date.getFullYear();
        var mon = date.getMonth() + 2;
        if (mon === 1) {
            mon = 12;
            year--;
        }
        else {
            mon--;
        }
        if (mon < 10) {
            mon = '0' + mon;
        }
        curDate = year + '-' + mon;
        var dates;
        $.ajax({
            url: "/dates/" + year + '-' + mon,
            dataType: "json",
            async: false,
            type: "GET",
            success: function (data) {
                dates = data;
            }
        });
        return dates;
    }

    /* GET DATES FROM NEXT MONTH */

    function nxMonth(date) {
        var year = date.getFullYear();
        var mon = date.getMonth();
        if (mon === 12) {
            mon = 1;
            year++;
        }
        else {
            mon++;
        }
        if (mon < 10) {
            mon = '0' + mon;
        }
        curDate = year + '-' + mon;
        var dates;
        $.ajax({
            url: "/dates/" + year + '-' + mon,
            dataType: "json",
            async: false,
            type: "GET",
            success: function (data) {
                dates = data;
            }
        });
        return dates;
    }

    /* GET MARKS FROM CURRENT MONTH */

    function getMarks(date) {
        var dep = window.location.pathname.split('/')[1];
        var marks;
        $.ajax({
            url: "/" + dep + "/" + subject + "/" + date.getFullYear() + '-' + (date.getMonth() + 1) + "/marks",
            dataType: "json",
            async: false,
            type: "GET",
            success: function (data) {
                marks = data;
            }
        });
        return marks;
    }

    /* FILL TABLE */

    function tableFill(dates, marks) {
        var values = document.getElementsByClassName('value');
        for (var n in values) {
            values[n].innerHTML = '';
        }
        var trs = document.getElementsByTagName('TR');
        var cells;
        for (var n in trs) {
            if (cells === undefined) {
                var tr = trs[n];
                cells = tr.cells.length - 1;
            }
            if (cells > dates.length) {
                for (var i = cells; i > dates.length; i--) {
                    trs[n].deleteCell(i);
                }
            }
            if (cells < dates.length) {
                for (var i = cells; i < dates.length; i++) {
                    trs[n].insertCell(i);
                }
            }
            if (n == 0) {
                for (var i = 0; i < dates.length; i++) {
                    var cell = trs[n].cells[i + 1];
                    var d = dates[i].dayOfMonth;
                    if (d < 10) {
                        var string = '0' + d;
                    } else {
                        var string = d.toString();
                    }
                    cell.className = 'day';
                    cell.innerHTML = string;
                }
            } else {
                for (var i = 0; i < dates.length; i++) {
                    var date = dates[i].year + '-' + dates[i].monthValue + '-' + dates[i].dayOfMonth;
                    trs[n].cells[i + 1].id = trs[n].cells[0].id + '.' + date + '.0';
                    trs[n].cells[i + 1].className = 'value';
                }
                var student = trs[n].cells[0].id;
                for (var j in marks) {
                    if (student == marks[j].student.id) {
                        for (var i = 1; i < trs[n].cells.length; i++) {
                            var cell = trs[n].cells[i];
                            var date = cell.id.split('.')[1];
                            if (marks[j].date.dayOfMonth == date.split('-')[2]) {
                                cell.innerHTML = marks[j].value;
                                var id = student + '.' + date + '.' + marks[j].id;
                                cell.id = id;
                            }
                        }
                    }
                }
            }
        }
    }
});