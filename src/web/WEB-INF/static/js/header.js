const department_api = '/api/department';
const subject_api = "/api/subject";

$(document).ready(function () {
    $.ajax({
        url: department_api,
        dataType: "json",
        async: false,
        type: "GET",
        success: function (data) {
            let div = document.getElementById('my-departments');
            for (let s in data) {
                let a = document.createElement('a');
                a.className = 'dropdown-item';
                a.setAttribute('dep-index',data[s].id )
                a.innerHTML = data[s].name;
                a.href = '#';
                div.appendChild(a);
            }
        }
    });
});

$(document).ready(function () {
    $.ajax({
        url: subject_api,
        dataType: "json",
        async: false,
        type: "GET",
        success: function (data) {
            let div = document.getElementById('my-subjects');
            for (let s in data) {
                let a = document.createElement('a');
                a.className = 'dropdown-item';
                a.innerHTML = data[s].name;
                a.setAttribute('sub-index', data[s].id);
                div.appendChild(a);
            }
        }
    });
});

