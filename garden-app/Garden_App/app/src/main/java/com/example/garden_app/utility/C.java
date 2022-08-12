package com.example.garden_app.utility;

public class C {

    public static final String APP_LOG_TAG = "BT CLN";

    public static class bluetooth {
        public static final int ENABLE_BT_REQUEST = 1;
        // Replace this string with that of the device acting as server
        public static final String BT_DEVICE_ACTING_AS_SERVER_NAME = "isi00";
        public static final String BT_SERVER_UUID = "7ba55836-01eb-11e9-8eb2-f2801f1b9fd1";
    }

    public static final String LIB_TAG = "BluetoothLib";

    public class channel {
        public static final int MESSAGE_RECEIVED = 0;
        public static final int MESSAGE_SENT = 1;
    }

    public class message {
        public static final char MESSAGE_TERMINATOR = '\n';
    }

    public class emulator {
        public static final String HOST_IP = "10.0.2.2";
        public static final int HOST_PORT = 8080;
    }
}
