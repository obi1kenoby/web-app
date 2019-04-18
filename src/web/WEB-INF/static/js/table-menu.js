$(document).ready(function() {
    var months = [
        {eng: 'JANUARY', rus: 'ЯНВАРЬ', num: 1},
        {eng: 'FEBRUARY', rus: 'ФЕВРАЛЬ', num: 2},
        {eng: 'MARCH', rus: 'МАРТ', num: 3},
        {eng: 'APRIL', rus: 'АПРЕЛЬ', num: 4},
        {eng: 'MAY', rus: 'МАЙ', num: 5},
        {eng: 'JUNE', rus: 'ИЮНЬ', num: 6},
        {eng: 'JULE', rus: 'ИЮЛЬ', num: 7},
        {eng: 'AUGUST', rus: 'АВГУСТ', num: 8},
        {eng: 'SEPTEMBER', rus: 'СЕНТЯБРЬ', num: 9},
        {eng: 'OCTOBER', rus: 'ОКТЯБРЬ', num: 10},
        {eng: 'NOVEMBER', rus: 'НОЯБРЬ', num: 11},
        {eng: 'DECEMBER', rus: 'ДЕКАБРЬ', num: 12}
    ];

    var select = document.getElementById('month');
    for (var n in months) {
        var o = document.createElement('option');
        o.value = months[n].num;
        o.innerHTML = months[n].eng;
        select.appendChild(o);
    }

    var departments = document.getElementById('department');
    var deps = getDepartments();
    for (var n in deps) {
        var o = document.createElement('option');
        o.value = deps[n].id;
        o.innerHTML = deps[n].name;
        departments.appendChild(o);
    }

    var subjects = document.getElementById('subject');
    var subs = getSubjects();
    for (var n in subs) {
        var o = document.createElement('option');
        o.value = subs[n].id;
        o.innerHTML = subs[n].name;
        subjects.appendChild(o);
    }
});