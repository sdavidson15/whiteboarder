var routePrefix = "localhost:8080/whiteboarder/"

function postSession() {

        $.ajax('http://localhost:8080/whiteboarder/session', {
                type: 'POST',
                contentType: "application/json",
                success: function (response) {
                        // make cookie
                        document.cookie = "wb_session_id=" + response + ";path=/";
                        window.location.href = "GenerateQR.html"
                },
                dataType: 'text'
        });
}


document.getElementById("create_session_btn").addEventListener("click", function () {
        change_page_qr();
});

function change_page_qr() {
        postSession();
}