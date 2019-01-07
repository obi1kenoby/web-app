$("#children > li").click(function () {
    const background = $(this).css("background-color");
    const white = "rgb(255, 255, 255)";
    if (background !== white) {
        this.removeChild(this.lastChild);
        this.classList.remove("selected");
    } else {
        let child = document.createElement('span');
        child.innerHTML = "&#9989";
        this.appendChild(child);
        this.classList.add("selected");
    }
});

$('#add-modal').on('hidden.bs.modal', function () {
    let children = document.getElementById("children");
    $(children).empty();
});

/**
 * add.html button listener.
 * saves departments or subjects.
 */
$("#save").click(function () {
   console.log("save department or subject.")
});