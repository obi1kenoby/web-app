$(document).on("click", ".item", function () {
    var background = $(this).css("background-color");
    var white = "rgb(255, 255, 255)";
    var counter = document.getElementById('counter');
    var count = parseInt(counter.innerHTML);
    if (background !== white) {
        this.removeChild(this.lastChild);
        this.classList.remove("selected");
        count--;
    } else {
        var child = document.createElement('span');
        child.innerHTML = "&#9989";
        this.appendChild(child);
        this.classList.add("selected");
        count++;
    }
    counter.innerHTML = count;
});

$("#delete").click(function () {
    var counter = document.getElementById('counter');
    if (parseInt(counter.innerHTML) === 0) {
        alert("Для удаления нужно выбрать хотя бы 1 запись!");
    } else {
        $(".selected").remove();
        counter.innerHTML = '0';
    }
});

$('#remove-modal').on('hidden.bs.modal', function () {
    var items = document.getElementById("items");
    var counter = document.getElementById('counter');
    $(items).empty();
    counter.innerHTML = '0';
});

