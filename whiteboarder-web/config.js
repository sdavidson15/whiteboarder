var config = (function () {
    var url,

        getUrl = function () {
            return url;
        },

        init = function () {
            var localUrl = 'http://localhost:8080';
            var prodUrl = 'http://proj-309-yt-c-1.cs.iastate.edu';
            $.ajax(prodUrl, {
                type: 'GET',
                success: function (response) {
                    url = prodUrl;
                },
                error: function (error) {
                    url = localUrl;
                }
            });
        }

    return {
        init: init,
        getUrl: getUrl
    };
}());