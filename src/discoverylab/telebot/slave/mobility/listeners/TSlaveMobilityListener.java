package discoverylab.telebot.slave.mobility.listeners;

import jssc.SerialPortException;
import TelebotDDSCore.Source.Java.Generated.master.mobility.TMasterToSlaveMobility;
import TelebotDDSCore.Source.Java.Generated.master.mobility.TMasterToSlaveMobilityDataReader;
import TelebotDDSCore.Source.Java.Generated.master.mobility.TMasterToSlaveMobilitySeq;

import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.ViewStateKind;

import discoverylab.telebot.slave.core.readers.CoreDataReaderAdapter;

public class TSlaveMobilityListener extends CoreDataReaderAdapter{
	
	public void on_data_available(DataReader reader) {
		TMasterToSlaveMobilityDataReader tMasterToSlaveMobilityDataReader = (TMasterToSlaveMobilityDataReader) reader;
		TMasterToSlaveMobilitySeq dataSeq = new TMasterToSlaveMobilitySeq();
		SampleInfoSeq infoSeq = new SampleInfoSeq();
		
		try {
			tMasterToSlaveMobilityDataReader.read(
					  dataSeq
					, infoSeq
					, ResourceLimitsQosPolicy.LENGTH_UNLIMITED
					, SampleStateKind.ANY_SAMPLE_STATE
					, ViewStateKind.ANY_VIEW_STATE
					, InstanceStateKind.ANY_INSTANCE_STATE);
			
			for(int i = 0; i < dataSeq.size(); i++) {
				SampleInfo info = (SampleInfo) infoSeq.get(i);
				
				if(info.valid_data) {					
					TMasterToSlaveMobility command = (TMasterToSlaveMobility)dataSeq.get(i);
					String commandStr = command.lMotor + " " + 
							command.rMotor + "\r";
					
					System.out.println(commandStr);
					getSerialPort().writeString(commandStr);
				}
			}
		} catch (RETCODE_NO_DATA noData) {
            // No data to process
        } 
		catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally {
			tMasterToSlaveMobilityDataReader.return_loan(dataSeq, infoSeq);
        }
	}
}