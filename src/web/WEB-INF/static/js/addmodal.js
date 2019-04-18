$("#children > li").click(function () {
    var background = $(this).css("background-color");
    var white = "rgb(255, 255, 255)";
    if (background !== white) {
        this.removeChild(this.lastChild);
        this.classList.remove("selected");
    } else {
        var child = document.createElement('span');
        child.innerHTML = "&#9989";
        this.appendChild(child);
        this.classList.add("selected");
    }
});

$('#add-modal').on('hidden.bs.modal', function () {
    var children = document.getElementById("children");
    $(children).empty();
});

$("#save").click(function () {
   console.log("save department or subject.")
});