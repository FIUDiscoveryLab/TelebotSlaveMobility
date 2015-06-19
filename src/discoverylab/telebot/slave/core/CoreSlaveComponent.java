package discoverylab.telebot.slave.core;

import java.lang.reflect.InvocationTargetException;

import TelebotDDSCore.DDSCommunicator;
import TelebotDDSCore.Source.Java.Generated.master.hands.TMasterToHands;
import TelebotDDSCore.Source.Java.Generated.master.hands.TMasterToHandsDataReader;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.DataReaderImpl;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.topic.Topic;

import discoverylab.telebot.slave.core.readers.CoreDataReaderAdapter;
import jssc.SerialPort;
import jssc.SerialPortException;
import static discoverylab.util.LogUtils.*;

/**
 * 
 * @author Irvin Steve Cardenas
 *
 */
public abstract class CoreSlaveComponent {

	public static String TAG = makeLogTag("CoreComponent");
	
//  Serial
	private SerialPort serialPort;
	protected Boolean serialConnected 		= false;
	private Boolean serialPortsAvailable 	= false;
	private String serialPortName;
	private int baudRate;
	private int dataBits;
	private int stopBits; 
	private int parityType;
	private int eventMask;
	
//  DDS 
	private DDSCommunicator communicator;
	private static Topic topic 						= null;
	private static DataReaderImpl reader 			= null;
	private static CoreDataReaderAdapter listener		= null;
//	Object instance 								= new TMasterToHands();
	InstanceHandle_t instance_handle 				= InstanceHandle_t.HANDLE_NIL;
	
	/**
	 * Default Constructor Uses Default Values For Serial Connection
	 * @param serialPort 
	 */
	public CoreSlaveComponent(SerialPort serialPort){
		this.serialPort = serialPort;
		
		this.baudRate 			= CoreHandsConfig.DEFAULT_SERIAL_BAUD_RATE;
		this.dataBits 			= CoreHandsConfig.DEFAULT_SERIAL_DATA_BITS;
		this.stopBits 			= CoreHandsConfig.DEFAULT_SERIAL_STOP_BITS;
		this.parityType 		= CoreHandsConfig.DEFAULT_SERIAL_PARITY_TYPE;
		this.eventMask 			= CoreHandsConfig.DEFAULT_SERIAL_EVENT_MASK;
	}
	
	/**
	 * 
	 * @param serialPortName
	 * @param baudRate
	 * @param dataBits
	 * @param stopBits
	 * @param parityType
	 * @param eventMask
	 */
	public CoreSlaveComponent(String serialPortName, int baudRate, int dataBits, int stopBits, int parityType, int eventMask){
		this.serialPortName 	= serialPortName;
		this.baudRate 			= baudRate;
		this.dataBits 			= dataBits;
		this.stopBits 			= stopBits;
		this.parityType 		= parityType;
		this.eventMask 			= eventMask;
		
		serialPort = new SerialPort(serialPortName);
	}
	
	/**
	 * Slave Hands Initiate - Open Hand Serial Connection
	 * @return
	 */
	@SuppressWarnings("finally")
	public boolean initiate(){
		try {
			serialPort.openPort();
			serialPort.setParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
		} catch (SerialPortException e) {
			LOGE(TAG, "Error opening SerialPort: " + serialPortName  + " with Baudrate: " + baudRate);
			e.printStackTrace();
		}
		finally{
			return serialPort.isOpened();
		}
	}
	
	/**
	 * Perform Slave Component Calibration
	 * @return
	 */
	public abstract boolean calibrate();
	
	/**
	 * Initiate DDS Protocol
	 * @return
	 */
	public boolean initiateTransmissionProtocol(String topicName, Class type, CoreDataReaderAdapter listener){
		setListener(listener);
		getListener().setSerialPort(this.serialPort);
		
		communicator = new DDSCommunicator();
		try {
			communicator.createParticipant();
			
		} catch (Exception e) {
			LOGE(TAG, "Error Creating Participant");
			e.printStackTrace();
		}
		try {
			communicator.createPublisher();
		} catch (Exception e) {
			LOGE(TAG, "Error Creating Publisher");
			e.printStackTrace();
		}
		try {
			communicator.createSubscriber();
		} catch (Exception e) {
			LOGE(TAG, "Error Creating Subscriber");
			e.printStackTrace();
		}
		
		try {
			this.topic = communicator.createTopic(topicName, type);
		} catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| InstantiationException e) {
			LOGE(TAG, "Error Creating Topic");
			e.printStackTrace();
		}
		
		try {
			setReader((DataReaderImpl) communicator.getSubscriber()
						.create_datareader(
								topic, 
								Subscriber.DATAREADER_QOS_USE_TOPIC_QOS, 
								getListener(),
								StatusKind.STATUS_MASK_ALL));
		} catch (Exception e) {
			LOGE(TAG, "Error Creating Reader");
			e.printStackTrace();
		}
		
		//TODO Get assertion of Participant
		return false;
	}

	private static CoreDataReaderAdapter getListener() {
		return listener;
	}

	private static void setListener(CoreDataReaderAdapter listener) {
		CoreSlaveComponent.listener = listener;
	}

	private static DataReaderImpl getReader() {
		return reader;
	}

	private static void setReader(DataReaderImpl reader) {
		CoreSlaveComponent.reader = reader;
	}

}
