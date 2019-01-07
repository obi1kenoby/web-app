$(".add").mouseenter(function(){
    $(this).attr('src', '/image/green-add.png');
});

$(".add").mouseleave(function(){
    $(this).attr('src', '/image/add.png');
});

$(".edit").mouseenter(function(){
    $(this).attr('src', '/image/edit-blue.png');
});

$(".edit").mouseleave(function(){
    $(this).attr('src', '/image/edit.png');
});

$(".remove").mouseenter(function(){
    $(this).attr('src', '/image/red-remove.png');
});

$(".remove").mouseleave(function(){
    $(this).attr('src', '/image/remove.png');
});

$(".remove").click(function () {
    let items = document.getElementById('items');
    let head = document.getElementById('remove-modal-label');
    let entities;
    let type = this.classList[1];
    if ($(this).hasClass("student")){
        head.innerHTML = "Delete Students";
        entities = getStudents();
    } else {
        if ($(this).hasClass("department")) {
            head.innerHTML = "Delete Department";
            entities = getDepartments();
        } else {
            head.innerHTML = "Delete Subjects";
            entities = getSubjects();
        }
    }
    for(let i in entities){
        let li = document.createElement('li');
        li.classList.add('item');
        li.classList.add('list-group-item');
        if (type === "student") {
            li.innerHTML = entities[i].last_name + " " + entities[i].first_name;
        } else {
            li.innerHTML = entities[i].name;
        }
        items.appendChild(li);
    }
    $("#remove-modal").modal('show');
});

/**
 * Admin page "ADD" click.
 */
$(".add").click(function () {
    if (!$(this).hasClass('student')) {
        let children = document.getElementById('children');
        let header = document.getElementById('add-modal-label');
        let childEntity = document.getElementById('child-entities');
        let childElements;
        if ($(this).hasClass('department')) {
            header.innerHTML = "Add Department";
            childEntity.innerHTML = "Chose Subjects for new Department";
            childElements = getSubjects();
        } else {
            if ($(this).hasClass('subject')) {
                header.innerHTML = "Add Subject";
                childEntity.innerHTML = "Chose Departments for new Subject";
                childElements = getDepartments();
            }
        }
        for (let i in childElements) {
            let li = document.createElement('li');
            li.classList.add('item');
            li.classList.add('list-group-item');
            li.innerHTML = childElements[i].name;
            children.appendChild(li);
        }
        $("#add-modal").modal('show');
    } else {
        let select = document.getElementById('stud-dep');
        let departments = $('#my-departments > .dropdown-item');
        for (let i in departments) {
            if (departments[i].tagName === 'A'){
                let option = document.createElement('option');
                option.value = departments[i].getAttribute('dep-index');
                option.text = departments[i].innerHTML;
                select.appendChild(option);
            }
        }
        $("#student-modal").modal('show');
    }
});

$('.edit').click(function () {
    let items = document.getElementById('edit-items');
    let type = this.classList[1];
    let header = document.getElementById('edit-modal-label');
    let entities;
    if ($(this).hasClass('student')){
        entities = getStudents();
        header.innerHTML = "Edit Student";
    } else {
        if ($(this).hasClass('subject')){
            entities = getSubjects();
            header.innerHTML = "Edit Subject";
        } else {
            entities = getDepartments();
            header.innerHTML = "Edit Department";
        }
    }
    for(var i in entities) {
        let item = document.createElement('li');
        item.classList.add('list-group-item');
        if (type === "student") {
            item.innerHTML = entities[i].last_name + " " + entities[i].first_name;
        } else {
            item.innerHTML = entities[i].name;
        }
        items.appendChild(item);
    }
    $('#edit-modal').modal('show');
});

$(document).on('click', '#edit-items > li', function () {
    let header = document.getElementById('edit-modal-label');
    let type =  header.innerHTML.toLowerCase().split(" ")[1];
    $('#edit-modal').modal('hide');
    if (type === "student"){
        $('#student-modal').modal('show');
    } else {
        if (type === "subject"){
            $('#add-modal').modal('show');
        } else {
            $('#add-modal').modal('show');
        }
    }
    edit(type, this.innerHTML);
});

function edit(type, content) {
    if (type === "student") {
        let firstName = content.split(" ")[1];
        let lastName = content.split(" ")[0];
        console.log(firstName + " " + lastName);
        let student = getStudent(firstName, lastName);
        $("#f-name").val(student.first_name);
        $("#l-name").val(student.last_name);
        $("#pass").val(student.password);
        $("#c-pass").val(student.password);
        $("#mail").val(student.email);
        $("#bth-year").val(student.birthday.year);
        $("#bth-month").val(student.birthday.monthValue);
        $("#bth-date").val(student.birthday.dayOfMonth);
    } else {
        if (type === "subject") {

        } else {

        }
    }
}

function getDepartments() {
    let departments;
    $.ajax({
        url: department_api,
        dataType: "json",
        async: false,
        type: "GET",
        success: function (data) {
            departments = data;
        }
    });
    return departments;
}

function getSubjects() {
    let subjects;
    $.ajax({
        url: subject_api,
        dataType: "json",
        async: false,
        type: "GET",
        success: function (data) {
            subjects = data;
        }
    });
    return subjects;
}

function getStudents() {
    let students;
    $.ajax({
        url: "/students",
        dataType: "json",
        async: false,
        type: "GET",
        success: function (data) {
            students = data;
        }
    });
    return students;
}

function getStudent(firstName, lastName) {
    let student;
    $.ajax({
        url: "/student/" + firstName + "/" + lastName,
        dataType: "json",
        async: false,
        type: "GET",
        success: function (data) {
            student = data;
        }
    });
    return student;
}