$( document ).ready(function() {
    $("#btn-register").click(function(event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();
        signup();

    });


    $("#btn-login").click(function(event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();
        login();

    });

});

function signup() {
    var username = $("#username").val();
    var password  = $("#password").val();
    var jsObj = { username: username, password: password};
    console.log(jsObj);
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/users/sign-up",
        data: JSON.stringify(jsObj),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
           alert("User has been created! Please signin to continue");

        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#result').html(json);

            console.log("ERROR : ", e);
            $("#btn-search").prop("disabled", false);

        }
    });
}

function login() {
    var username = $("#signin_username").val();
    var password = $("#signin_password").val();
    var data = {username:username, password:password};
    console.log(data);
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/login",
        data: JSON.stringify(data),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (resp, textStatus, xhr) {
            console.log("header" + resp + textStatus + xhr.getResponseHeader("Authorization"));
            document.location.href = "/welcome";
        },
        error: function (e) {
            alert("Invalid Credentials. Try Again!")
            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#result').html(json);

            console.log("ERROR : ", e);
            $("#btn-search").prop("disabled", false);

        }
    });

}