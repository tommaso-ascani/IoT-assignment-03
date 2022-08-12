$(document).ready(function(){

    setInterval(function(){

        $("#light").load(window.location.href + " #led_1, #led_2, #led_3, #led_4");
        $("#irrigation").load(window.location.href + " #irrigationState, #irrigationValue");
        
    }, 500);
});