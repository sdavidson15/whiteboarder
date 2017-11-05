var routePrefix = "localhost:8080/whiteboarder/"

function postSession() {

}


document.getElementById("create_session_btn").addEventListener("click", function () {
        change_page_qr();
});

function change_page_qr() {
        postSession();
}
