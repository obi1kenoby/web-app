function createTable(department_id, date, subject_id) {
    var table = document.getElementById('table');
    var students = getStudents(department_id);
    var tr = document.createElement('tr');
    var fio = document.createElement('th');
    var subject = getSubject(subject_id);
    fio.innerHTML = 'Student';
    tr.appendChild(fio);
    var dates = getDates(date);
    for (d in dates) {
        var th = document.createElement('th');
        var day = dates[d][2];
        if (day < 10) {
            day = '0' + day;
        }
        th.innerHTML = day;
        tr.appendChild(th);
    }
    table.appendChild(tr);
    for (n in students) {
        var tr = document.createElement('tr');
        var td = document.createElement('td');
        var student = students[n];
        td.innerHTML = student.last_name + " " + student.first_name;
        td.style.textAlign = 'left';
        tr.appendChild(td);
        for (d in dates) {
            var td = document.createElement('td');
            var marks = student.marks;
            var v = isStudentHaveValue(marks, subject, dates[d]);
            if (v != null) {
                td.innerHTML = student.marks[v].value;
            } else {
                td.innerHTML = ' ';
            }
            tr.appendChild(td);
        }
        table.appendChild(tr);
    }
    var container = document.getElementById('table-container');
    container.appendChild(table);
}

function getDates(date) {
    var dates;
    $.ajax({
        url: "/api/date",
        dataType: "json",
        async: false,
        type: "GET",
        data: {date},
        success: function (data) {
            dates = data;
        }
    });
    return dates;
}

function getStudents(department_id) {
    var students;
    $.ajax({
        url: "/api/student/department/" + department_id,
        dataType: "json",
        async: false,
        type: "GET",
        success: function (data) {
            students = data;
        }
    });
    return students;
}

function getSubject(subject_id) {
    var student;
    $.ajax({
        url: "/api/subject/" + subject_id,
        dataType: "json",
        async: false,
        type: "GET",
        success: function (data) {
            student = data;
        }
    });
    return student;
}

function isStudentHaveValue(marks, subject, date) {
    for (var i in marks) {
        var mark = marks[i];
        var m = date[1];
        var d = date[2];
        if (m < 10) {
            m = '0' + m;
        }
        if (d < 10) {
            d = '0' + d;
        }
        var date = date[0] + '-' + m + '-' + d;
        if (date === mark.date && mark.subject.name === subject.name) {
            return i;
        }
    }
    return null;
}