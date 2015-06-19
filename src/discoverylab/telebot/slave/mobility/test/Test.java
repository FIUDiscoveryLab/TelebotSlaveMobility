package discoverylab.telebot.slave.mobility.test;

import static discoverylab.util.LogUtils.makeLogTag;
import discoverylab.telebot.slave.mobility.configurations.Config;

public class Test {
	
	public static String TAG = makeLogTag(Test.class);
	
	public static void main(String args [] ){

		TelebotSlaveMobility telebotSlaveHands = new TelebotSlaveMobility(
				Config.SERIAL_PORT_NAME, 
				Config.SERIAL_BAUD_RATE,
				Config.SERIAL_DATA_BITS,
				Config.SERIAL_STOP_BITS,
				Config.SERIAL_PARITY_TYPE,
				Config.SERIAL_EVENT_MASK);
	}
}
