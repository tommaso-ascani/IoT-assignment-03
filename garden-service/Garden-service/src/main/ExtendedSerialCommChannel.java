package main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.*;
import jssc.*;


/**
 * Comm channel implementation based on serial port.
 *
 * @author aricci
 *
 */
public class ExtendedSerialCommChannel implements CommChannel, SerialPortEventListener {

    private SerialPort serialPort;
    private BlockingQueue<String> queue;
    private StringBuffer currentMsg = new StringBuffer("");
    private int localIPportEmu;
    private static final int SERVER_PORT = 8080;
    private DbUtilities DBU;

    private AndroidEmulatorCommChannel androidEmulatorChannel;
    private static final char BT_MESSAGES_ID = '$';

    public ExtendedSerialCommChannel(String port, int rate) throws Exception {
        this(port, rate, SERVER_PORT);
        
    }

    public ExtendedSerialCommChannel(String port, int rate, int localIPportEmu) throws Exception {
        queue = new ArrayBlockingQueue<String>(100);
        this.DBU = new DbUtilities();
        this.localIPportEmu = localIPportEmu;

        serialPort = new SerialPort(port);
        try {
            serialPort.openPort();

            serialPort.setParams(rate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);

            // serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
            serialPort.addEventListener(this);

            System.out.println("serial port ok");
            // emulator server

            androidEmulatorChannel = new AndroidEmulatorCommChannel(localIPportEmu);
            androidEmulatorChannel.start();

        } catch (SerialPortException ex) {
            System.err.println("Error on writing string to port: " + port);
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println("Error on installing emu|IP server");
            ex.printStackTrace();
        }


    }

    @Override
    public void sendMsg(String msg) {
        char[] array = (msg+"\n").toCharArray();
        byte[] bytes = new byte[array.length];
        for (int i = 0; i < array.length; i++){
            bytes[i] = (byte) array[i];
        }
        try {
            synchronized (serialPort) {
                serialPort.writeBytes(bytes);
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public String receiveMsg() throws InterruptedException {
        // TODO Auto-generated method stub
        return queue.take();
    }

    @Override
    public boolean isMsgAvailable() {
        // TODO Auto-generated method stub
        return !queue.isEmpty();
    }

    /**
     * This should be called when you stop using the port.
     * This will prevent port locking on platforms like Linux.
     */
    public void close() {
        try {
            if (serialPort != null) {
                serialPort.removeEventListener();
                serialPort.closePort();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public void serialEvent(SerialPortEvent event) {
        /* if there are bytes received in the input buffer */
        if (event.isRXCHAR()) {
            try {
                String msg = serialPort.readString(event.getEventValue());

                msg = msg.replaceAll("\r", "");
                

                currentMsg.append(msg);

                boolean goAhead = true;

                while(goAhead) {
                    String msg2 = currentMsg.toString();
                    int index = msg2.indexOf("\n");
                    if (index >= 0) {

                        if(msg2.getBytes().length > 1 && msg2.getBytes()[index - 1] == BT_MESSAGES_ID) {
                            androidEmulatorChannel.sendMsgToAndroidEmulator(msg2.substring(0, index - 1));
                        } else {
                            queue.put(msg2.substring(0, index));
                        }

                        currentMsg = new StringBuffer("");
                        if (index + 1 < msg2.length()) {
                            currentMsg.append(msg2.substring(index + 1));
                        }
                    } else {
                        goAhead = false;
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Error in receiving string from COM-port: " + ex);
            }
        }
    }


    class AndroidEmulatorCommChannel extends Thread {

        private ServerSocket server;
        private Socket socket;

        public AndroidEmulatorCommChannel(int port) throws IOException {
            server = new ServerSocket(port);
        }

        @Override
        public void run() {
            try {
                socket = server.accept();
                while(true) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = in.readLine();
                    //Qui il messaggio arriva da Android e va ad Arduino
                    System.out.println(message);
                    switch(message) {
                    	case "S" : {
                    		HashMap<String, String> msg = DbUtilities.data;
                    		Collection<String> value  = DbUtilities.data.values();
                    		int[] arrayValue;
                    		 value.toArray();
                    		currentMsg.append(msg);

                            boolean goAhead = true;

                            while(goAhead) {
                                String msg2 = currentMsg.toString();
                                int index = msg2.indexOf("\n");
                                if (index >= 0) {

                                    if(msg2.getBytes().length > 1 && msg2.getBytes()[index - 1] == BT_MESSAGES_ID) {
                                        androidEmulatorChannel.sendMsgToAndroidEmulator(msg2.substring(0, index - 1));
                                    } else {
                                        queue.put(msg2.substring(0, index));
                                    }

                                    currentMsg = new StringBuffer("");
                                    if (index + 1 < msg2.length()) {
                                        currentMsg.append(msg2.substring(index + 1));
                                    }
                                } else {
                                    goAhead = false;
                                }
                            }
                    		break;
                    	}
                    	case "1true" : {
                    		DBU.changeValue("led_1", 1);
                    		break;
                    	}
                    	case "1false" : {
                    		DBU.changeValue("led_1", 0);
                    		break;
                    	}
                    	case "2true" : {
                    		DBU.changeValue("led_2", 1);
                    		break;
                    	}
                    	case "2false" : {
                    		DBU.changeValue("led_2", 0);
                    		break;
                    	}
                    	case "30" : {
                    		DBU.changeValue("led_3", 0);
                    		break;
                    	}
                    	case "31" : {
                    		DBU.changeValue("led_3", 1);
                    		break;
                    	}
                    	case "32" : {
                    		DBU.changeValue("led_3", 2);
                    		break;
                    	}
                    	case "33" : {
                    		DBU.changeValue("led_3", 3);
                    		break;
                    	}
                    	case "34" : {
                    		DBU.changeValue("led_3", 4);
                    		break;
                    	}
                    	case "40" : {
                    		DBU.changeValue("led_4", 0);
                    		break;
                    	}
                    	case "41" : {
                    		DBU.changeValue("led_4", 1);
                    		break;
                    	}
                    	case "42" : {
                    		DBU.changeValue("led_4", 2);
                    		break;
                    	}
                    	case "43" : {
                    		DBU.changeValue("led_4", 3);
                    		break;
                    	}
                    	case "44" : {
                    		DBU.changeValue("led_4", 4);
                    		break;
                    	}
                    	case "Itrue" : {
                    		DBU.changeValue("irrigationState", 1);
                    		break;
                    	}
                    	case "Ifalse" : {
                    		DBU.changeValue("irrigationState", 0);
                    		break;
                    	}
                    	case "I1" : {
                    		DBU.changeValue("irrigationValue", 1);
                    		break;
                    	}
                    	case "I2" : {
                    		DBU.changeValue("irrigationValue", 2);
                    		break;
                    	}
                    	case "I3" : {
                    		DBU.changeValue("irrigationValue", 3);
                    		break;
                    	}
                    	case "I4" : {
                    		DBU.changeValue("irrigationValue", 4);
                    		break;
                    	}
                    }
                    //sendMsg(message + BT_MESSAGES_ID);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        public void sendMsgToAndroidEmulator(String msg) {
            if (socket != null) {
                PrintWriter out;
                try {
                    out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
