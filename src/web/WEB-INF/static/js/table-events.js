$(document).ready(function () {

    var t = document.getElementById('table');
    var y = $('#year').val();
    var m = $($('#month')).find(":selected").val();
    var s = $($('#subject')).find(":selected").val();
    var d = $($('#department')).find(":selected").val();

    if (y == '') {
        y = new Date().getFullYear();
        $('#year').val(y);
    }

    if (!t.firstChild) {
        createTable(d, y + '-0' + m + '-01', s);
    }

    var mon;

    function prev() {
        var m = $($('#month')).find(":selected").val();
        if (m > 0 && m < 10) {
            if (m == 1) {
                m = 12;
                mon = m;
                $('#year').val(--y);
            } else {
                m--;
                mon = '0' + m;
            }
        } else {
            mon = m;
            if (m == 10) {
                m--;
                mon = '0' + m;
            } else {
                m--;
                mon = m;
            }
        }
        $('#month').val(m);
        createTable(d, y + '-' + mon  + '-01', s);
    }

    function next() {
        var m = $($('#month')).find(":selected").val();
        if (m > 0 && m < 10) {
            m++;
            if (m == 10) {
                mon = m;
            } else {
                mon = '0' + m;
            }
        } else {
            if (m == 12) {
                m = 1;
                $('#year').val(++y);
            } else {
                m ++;
                mon = m;
            }
        }
        $('#month').val(m);
        createTable(d, y + '-' + mon + '-01', s);
    }

    $("#left-container").click(function () {
        clearTable();
        prev();
    });

    $("#right-container").click(function () {
        clearTable();
        next();
    });

    $("#ok").click(function() {
        var m = $($('#month')).find(":selected").val();
        var d = $($('#department')).find(":selected").val();
        var s = $($('#subject')).find(":selected").val();
        clearTable();
        if (m > 0 && m < 10) {
            mon = '0' + m;
        } else {
            mon = m;
        }
        createTable(d, y + '-' + mon + '-01', s);
    });

    function clearTable() {
        while (t.firstChild) {
            t.removeChild(t.firstChild);
        }
    }
});