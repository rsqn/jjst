ns("utils")

utils.pingMe = function() {
    console.log("Pinging me");

    #ifprofile debug
        console.log('DEBUG ME');  // its better to actually do an #include here
    #endif
}