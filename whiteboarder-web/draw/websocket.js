var websocketApp = (function () {
    var socket,
        url,
        username,
        sessionID,
        nameBox,
        nameBtn,

        getSessionID = function () {
            return sessionID;
        },

        getUsername = function () {
            return username;
        },

        handleEdit = function (edit, isRemove) {
            if (sessionID == null || username == null)
                return;

            h = { edit: edit, isRemove: isRemove };
            socket.send(JSON.stringify(h));
        },

        login = function () {
            username = nameBox.value;
            if (sessionID == null) {
                // TODO: parse url for session id
                sessionID = document.getElementById("sessionBox").value;
                socket.send("login:" + sessionID + "," + username);
                nameBtn.value = "Change name";
            } else {
                // TODO: Send http user rename request
            }
        },

        setupEventHandlers = function () {
            socket.onopen = function (event) {
                drawingApp.init();
            };
            socket.onerror = function (error) {
                alert('WebSocket Error: ' + JSON.stringify(error));
            };
            socket.onmessage = function (event) {
                var message = event.data;
                var h = JSON.parse(message);
                if (h.isRemove) {
                    drawingApp.removeEdit(h.edit);
                    return;
                }
                drawingApp.addEdit(h.edit);
            };
            socket.onclose = function (event) {
                drawingApp = null;
                nameBtn.value = "Join";
            };

            document.getElementById("nameForm").addEventListener("submit", function (e) {
                e.preventDefault();
                login();
            });
        },

        init = function () {
            url = window.location.href.replace(window.location.protocol, 'ws:').replace(window.location.pathname, '/ws/session');
            socket = new WebSocket(url);
            nameBox = document.getElementById("nameBox");
            nameBtn = document.getElementById("nameBtn");
            setupEventHandlers();
        };

    return {
        init: init,
        handleEdit: handleEdit,
        getSessionID: getSessionID,
        getUsername: getUsername
    };
}());