    $( document ).ready(function() {
        startpage();



        function showPosition(position) {
            document.getElementById('lat').value=position.coords.latitude;
            document.getElementById('long').value=position.coords.longitude;
        }
        function startpage() {
            $("#location").show();
            $("#hide-location").html("Detect Location");
            $('#root').html('');
            $('#home').html('');
            var HomeTable = React.createClass({
                render: function () {
                    return (
                        <div className="intro">
                        <div className="container">
                        <div className="row">
                        <div className="col-md-12">
                        <div className="header-content text-left">
                        <form id="homeform">
                        <h1 className="margin-auto">Time is precious!<br></br>Make the most of
                    it</h1>
                    <br></br><br></br>
                    <h4 >Location?(required)</h4>
                        <input type="text" name="location" id ="location"
                    placeholder="Location"/><br></br><a href="#" id="hide-location">Detect Location</a><br></br><br></br>
                    <br></br><br></br>
                    <h4>For how many days do you want us to check?(leave blank if its just for today)</h4>
                    <input type="text" name="days"
                    placeholder="In Days"/><br></br><br></br><br></br>
                    <h4>How many hours are you willing to spend?(required)</h4>
                        <input type="text" name="hours"
                    placeholder="In Hours"/><br></br><br></br><br></br>
                    <input type="hidden" name="lat" id="lat"/>
                        <input type="hidden" name="long" id="long"/>
                        </form>
                        </div>
                        <a href="#" className="btn btn-info button" id="btn-search">Get Events</a>
                    </div>
                    </div>
                    </div>
                    </div>
                );
                }
            });

            ReactDOM.render(
            <HomeTable/>, document.getElementById('home')
        );
            $(function () {
                $('#datetimepicker1').datetimepicker();
            });
            $("#hide-location").click(function(event) {
                $("#location").hide();
                $("#hide-location").html("Location Detected!");
                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(showPosition);
                } else {
                    x.innerHTML = "Geolocation is not supported by this browser.";
                }
            });
            $("#btn-search").click(function(event) {

                //stop submit the form, we will post it manually.
                event.preventDefault();

                fire_ajax_submit();

            });
        }

        $("#start-button").click(function(event) {
            $("#my_li").removeClass("active");
            $("#start_li").addClass("active");
            //stop submit the form, we will post it manually.
            event.preventDefault();

            startpage();

        });

        $("#my-history-button").click(function(event) {
            //stop submit the form, we will post it manually.
            event.preventDefault();

            fire_ajax_submit2();

        });

    });

function fire_ajax_submit2() {
    $("#start_li").removeClass("active");
    $("#my_li").addClass("active");
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "events/historyEvents",
        data: {},
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log("debug"+ data);
            $('#home').html('');
            var EMPLOYEES1 = data;
            var Employee1 = React.createClass({
                onTodoChange(value)
                {
                    this.setState({
                        name: value
                    });
                },
                render: function() {

                    return (
                        <tr>
                        <td>{this.props.employee.eventName}</td>
                    <td>  <label>
                    {this.props.employee.eventRating}
                </label></td>
                    <td><input type="number" name={this.props.employee.eventName} className="ratingsInput"/></td>
                        </tr>);
                }
            });
            var EmployeeTable1 = React.createClass({
                render: function() {
                    var rows = [];
                    this.props.employees.map(function(employee) {
                        rows.push(<Employee1 employee={employee} />);
                    });
                    return (
                        <div className="container">
                        <div className="row">
                        <form>
                        <table className="table table-striped">
                        <thead>
                        <tr>
                        <th>Event Name</th>
                    <th>Current Rating</th>
                    <th>New Rating</th>
                    </tr>
                    </thead>
                    <tbody>{rows}</tbody>
                    </table>
                    <a href="#" className="btn btn-info button" id = "btn-changerat">Change Rating!</a>
                    </form>
                    </div>
                    </div>

                );
                }
            });

            ReactDOM.render(
            <EmployeeTable1 employees ={EMPLOYEES1}/>, document.getElementById('root')
        );

            $("#btn-changerat").click(function(event) {
                //stop submit the form, we will post it manually.
                event.preventDefault();
                fire_ajax_submit3();
            });
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
function fire_ajax_submit3() {
    console.log("hi");
    var ratingsArray = $('.ratingsInput').map(function() {
        return '{"eventName":"'+this.name +'","eventRating":"' + this.value + '"}';
    }).get().toString();
    //ratingsArray = ratingsArray.substring(0, ratingsArray.length-1);
    ratingsArray = '[' + ratingsArray + ']';
    console.log("Data" + ratingsArray)
    $("#btn-search").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "events/saveRatings",
        data: ratingsArray,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log("hi");
            fire_ajax_submit2();
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

function fire_ajax_submit1() {
    console.log("hi");
    const data =  $("input[name=select]:checked").val();
    var sendData = '{"name":"' +data +'"}';
    console.log("Data" + sendData)
    $("#btn-search").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "events/saveEvent",
        data: sendData,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log("hi");
            fire_ajax_submit2();
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
function fire_ajax_submit() {

    const data =  $("#homeform").serializeArray();
    console.log("Data" + data);

    $("#btn-search").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "events/getEvents",
        data: JSON.stringify(data),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {

            var json = data;
            console.log(json);
            var EMPLOYEES = json;
            var Employee = React.createClass({
                render: function() {
                    return (
                        <tr>
                        <td>{this.props.employee.eventName}</td>
                    <td><a href={this.props.employee.eventLink}>Details</a></td>
                    <td>{this.props.employee.eventPlace}</td>
                    <td>{this.props.employee.travellingTimeInString}</td>
                    <td>{this.props.employee.eventStartTimeInString}</td>
                    <td>  <label>
                    <input type="radio" name="select" value={this.props.employee.eventName}/>
                    </label></td>
                    </tr>);
                }
            });
            var EmployeeTable = React.createClass({
                render: function() {
                    var rows = [];
                    this.props.employees.map(function(employee) {
                        rows.push(<Employee employee={employee} />);
                    });
                    if(rows.length>0) {
                        return (
                            <div className="container">
                                <div className="row">
                                    <form>
                                        <table className="table table-striped">
                                            <thead>
                                            <tr>
                                                <th>Event Name</th>
                                                <th>Event Link</th>
                                                <th>Place</th>
                                                <th>Travel Time</th>
                                                <th>Event Start Time</th>
                                                <th>
                                                    <label>
                                                        <i className={"fa fa-fw icon icon--kohana fa-check"}></i>
                                                        <span
                                                            className="input__label-content input__label-content--kohana">Choose Me!</span>
                                                    </label>
                                                </th>
                                            </tr>
                                            </thead>
                                            <tbody>{rows}</tbody>
                                        </table>
                                    </form>
                                    <a href="#" className="btn btn-info button" id="btn-save">Go!</a>
                                </div>
                            </div>

                        );
                    } else {
                        return (
                            <div className="container">
                                <div className="row">
                                    <h4>No Events Fo</h4>
                                </div>
                            </div>

                        );
                    }
                }
            });

            ReactDOM.render(
            <EmployeeTable employees ={EMPLOYEES}/>, document.getElementById('root')
        );
            $('#result').html(json);
            $("#btn-save").click(function(event) {
                console.log("hi");
                //stop submit the form, we will post it manually.
                event.preventDefault();
                fire_ajax_submit1();
            });
            console.log("SUCCESS : ", data);
            $("#btn-search").prop("disabled", false);

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