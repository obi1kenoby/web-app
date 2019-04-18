$(document).ready(function () {

    var t = document.getElementById('table');
    var y = $('#year').val();
    var m = $($('#month')).find(":selected").val();
    var s = $($('#subject')).find(":selected").val();
    var d = $($('#department')).find(":selected").val();

    if (y === '') {
        y = new Date().getFullYear();
        y.val(y);
    }

    var mon;

    function clickLeft() {
        if (m > 0 && m < 10) {
            mon = '0' + m;
            if (m === 1) {
                m = 13;
                $('#year').val(--y);
            }
        } else {
            mon = m;
        }
        $('#month').val(--m);
        createTable(d, y + '-' + mon  + '-01', s);
    }

    function clickRight() {
        if (m > 0 && m < 10) {
            mon = '0' + m;
        } else {
            mon = m;
            if (m === 12) {
                m = 0;
                $('#year').val(++y);
            }
        }
        $('#month').val(++m);
        createTable(d, y + '-' + mon + '-01', s);
    }

    $("#left-container").click(function () {
        clearTable();
        clickLeft();
    });

    $("#right-container").click(function () {
        clearTable();
        clickRight();
    });

    function clearTable() {
        while (t.firstChild) {
            t.removeChild(t.firstChild);
        }
    }
});