package discoverylab.telebot.slave.mobility.test;

import static discoverylab.util.LogUtils.LOGI;
import static discoverylab.util.LogUtils.makeLogTag;
import TelebotDDSCore.Source.Java.Generated.master.hands.TMasterToHands;
import TelebotDDSCore.Source.Java.Generated.master.hands.TOPIC_MASTER_TO_SLAVE_HANDS;
import TelebotDDSCore.Source.Java.Generated.master.mobility.TMasterToSlaveMobility;
import TelebotDDSCore.Source.Java.Generated.master.mobility.TOPIC_MASTER_TO_SLAVE_MOBILITY;
import discoverylab.telebot.slave.mobility.configurations.SlaveMobilityConfig;
import discoverylab.telebot.slave.mobility.listeners.TSlaveMobilityListener;

public class TelebotSlaveMobilityTest {
	
	public static String TAG = makeLogTag(TelebotSlaveMobilityTest.class);
	
	public static void main(String args [] ){

		TelebotSlaveMobility telebotSlaveMobility = new TelebotSlaveMobility(
				  SlaveMobilityConfig.SERIAL_PORT_NAME
				, SlaveMobilityConfig.SERIAL_BAUD_RATE
				, SlaveMobilityConfig.SERIAL_DATA_BITS
				, SlaveMobilityConfig.SERIAL_STOP_BITS
				, SlaveMobilityConfig.SERIAL_PARITY_TYPE
				, SlaveMobilityConfig.SERIAL_EVENT_MASK);
		
		// 1. INITIATE Slave Component DEVICE
				if( telebotSlaveMobility.initiate()){
					LOGI(TAG, "Hand Initiation Complete");
				}
				else {
					LOGI(TAG, "Hand Initiation Failed");
				}
				
		// 2. CALIBRATE
				if( telebotSlaveMobility.calibrate() ){
					LOGI(TAG, "Hand Calibration Complete");
				}
				else {
					LOGI(TAG, "Hand Calibration Failed");
				}
				
		// 3. INITIATE Transmission PROTOCOL
				TSlaveMobilityListener listener = new TSlaveMobilityListener();
				
				if( telebotSlaveMobility.initiateTransmissionProtocol(
						  TOPIC_MASTER_TO_SLAVE_MOBILITY.VALUE
						, TMasterToSlaveMobility.class
						, listener) ) {
					LOGI(TAG, "Protocol Sequence Initiated");
				}
				else {
					LOGI(TAG, "Protocol Sequence Failed");
				}
				
		// 4. INITIATE Transmission SEQUENCE		
	}
}
