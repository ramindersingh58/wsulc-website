<%-- 
    Document   : EnterLevelData
    Created on : Apr 26, 2017, 8:45:36 PM
    Author     : Raminder
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ULCWS</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
    <center>
        <div class="container">
            <div class="col-sm-4">
                <h1 class="jumbotron text-center">Enter Data of New Level Crossing</h1>

                <h5>Click on map to set position (latitude and longitude)  or manually fill latitude and longitude values to preview the location. <br>
                    You can also drag the map area to move the marker or drag the marker to specific place.<br>
                Use zoom in/zoom out feature if necessary.</h5>
                <form class="form-vertical" action="./SaveLevelCrossing" method="post" >
                    <div class="form-group">
                        <label class="col-sm-4">Latitude</label>
                        <div class="col-sm-8"><input type="text" name='lat' id='lat' required placeholder='Latitude' class="form-control" /> 
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4">Longitude</label>
                        <div class="col-sm-8"><input type="text" name='lng' id='lng' required placeholder='Longitude' class="form-control" /> 
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-0"></label>
                        <div class="col-sm-12"><input type="button" value="See On Map" onclick="update()" class="form-control" /> 
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-12"></label>
                        
                    </div>
                    <div class="form-group">
                        <label class="col-sm-6">Level Crossing Name</label>
                        <div class="col-sm-6"><input type="text" name='ln' id='ln' required placeholder='Level Crossing Name' class="form-control" /> 
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-6">Location Name</label>
                        <div class="col-sm-6"><input type="text" name='loc_name' id='loc_name' required value='unknown' class="form-control" /> 
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2"></label>
                        <div class="col-sm-10"><input type="submit" value="Add Level Crossing" class="btn btn-primary"/> 
                        </div>
                    </div>

                </form>
            </div>
            <div class="col-sm-8">
                <div id="map" style="width:100%;height:550px"></div>
            </div>

        </div>

        <%
            String m = request.getParameter("msg");
            if (m != null) {
        %>
        <h3 style="color:red"><%= m%></h3>

        <%
            }
        %>


        <script>
            var google_map;
            var marker = null;
            var myCenter
            function myMap() {
                myCenter = new google.maps.LatLng(31.619786, 74.876394);
                var mapCanvas = document.getElementById("map");
                var mapOptions =
                        {
                            center: myCenter,
                            zoom: 14,
                            mapTypeId: google.maps.MapTypeId.ROADMAP
                        };
                google_map = new google.maps.Map(mapCanvas, mapOptions);
                marker = new google.maps.Marker({
                    position: myCenter,
                    draggable: true,
                    map: google_map
                });

                google.maps.event.addListener(google_map, 'click', function (event)
                {
                    var latitude = event.latLng.lat();
                    var longitude = event.latLng.lng();
                    var location = new google.maps.LatLng(latitude, longitude);
                    updatePosition(latitude, longitude);
                    add_marker(location);
                });
                google.maps.event.addListener(marker, 'dragend', function () {
                    google_map.setCenter(this.getPosition()); // Set map center to marker position
                    updatePosition(this.getPosition().lat(), this.getPosition().lng()); // update position display
                });
                google.maps.event.addListener(google_map, 'dragend', function () {
                    marker.setPosition(this.getCenter()); // set marker position to map center
                    updatePosition(this.getCenter().lat(), this.getCenter().lng()); // update position display
                });
            }
            function updatePosition(lat, lng) {
                document.getElementById('lat').value = lat;
                document.getElementById('lng').value = lng;
            }
            function update()
            {
                var lati = document.getElementById('lat').value;
                var long = document.getElementById('lng').value;
                var loca = new google.maps.LatLng(lati, long);
                google_map.setCenter(loca);
                if (marker != null)
                {
                    marker.setPosition(loca);
                }
                else
                {
                    marker = new google.maps.Marker({
                        position: loca,
                        title: "New Level Crossing",
                        draggable: true,
                        map: google_map

                    });
                }
                return false;
            }

            function add_marker(location)
            {
                if (marker == null)
                {
                    marker = new google.maps.Marker({
                        position: location,
                        title: "New Level Crossing",
                        draggable: true,
                        map: google_map
                                // var image = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png';
                    });
                }
                else
                {
                    marker.setPosition(location);
//                    marker.setMap(null);
//                    marker = new google.maps.Marker({
//                        position: location,
//                        title: "New Level Crossing",
//                        draggable: false,
//                        map: google_map
//                    });
                }
            }
        </script>
        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCbJtZu9r6ApDln2YZha87S5zNGYWrGUSU&callback=myMap"></script>
    </center>
</body>
</html>
