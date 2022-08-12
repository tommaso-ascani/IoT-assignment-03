<!DOCTYPE html>
<html lang="it">
    <head>
        <title>Test</title>
        <link rel="stylesheet" type="text/css" href="style/style.css"/>
        <script language="javascript" type="text/javascript" src="js/jquery-3.6.0.min.js"></script>        
    </head>
    <body>
        <h1>Garden Service Dashboard</h1>

        <table>
            <h2>Light System</h2>
            <tr>
                <th>Led 1</th>
                <th>Led 2</th>
                <th>Led 3</th>
                <th>Led 4</th>
            </tr>
            <tr id="light">
                <td id="led_1"><?php if($dbh->getValue("led_1")[0]['val'] == 0) {echo 'Off';} else {echo 'On';} ?></td>
                <td id="led_2"><?php if($dbh->getValue("led_2")[0]['val'] == 0) {echo 'Off';} else {echo 'On';} ?></td>
                <td id="led_3"><?php echo $dbh->getValue("led_3")[0]['val'] ?></td>
                <td id="led_4"><?php echo $dbh->getValue("led_4")[0]['val'] ?></td>
            </tr>
        </table>

        <table>
            <h2>Irrigation System</h2>
            <tr>
                <th>State</th>
                <th>Value</th>
            </tr>
            <tr id="irrigation">
                <td id="irrigationState"><?php if($dbh->getValue("irrigationState")[0]['val'] == 0) {echo 'Close';} else {echo 'Open';} ?></td>
                <td id="irrigationValue"><?php echo $dbh->getValue("irrigationValue")[0]['val'] ?></td>
            </tr>
        </table>
    </body>
    <script language="javascript" type="text/javascript" src="js/func.js"></script>
</html>