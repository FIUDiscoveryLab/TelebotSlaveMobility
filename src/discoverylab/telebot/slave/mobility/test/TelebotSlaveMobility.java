package discoverylab.telebot.slave.mobility.test;

import jssc.SerialPort;

import com.rti.dds.topic.Topic;

import discoverylab.telebot.slave.core.CoreSlaveComponent;

public class TelebotSlaveMobility extends CoreSlaveComponent {

	public TelebotSlaveMobility(String serialPortName, int baudRate,
			int dataBits, int stopBits, int parityType, int eventMask) {
		super(serialPortName, baudRate, dataBits, stopBits, parityType, eventMask);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean calibrate() {
		// TODO Auto-generated method stub
		return false;
	}


}
