package windows.models;

import com.vmware.vim25.mo.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VM {

	private StringProperty  GuestOS;
	private Network[] NetWork;
	private StringProperty  Name;
	private StringProperty  PowerState;
	private StringProperty  Status;
	private IntegerProperty CPU;
	private IntegerProperty Memory;
	private StringProperty ToolStatus;
	public String getGuestOS() {
		return GuestOS.get();
	}

	public String getName() {
		return Name.get();
	}

	public String getPowerState() {
		return PowerState.get();
	}

	public String getStatus() {
		return Status.get();
	}
	public int getCPU() {
		return CPU.get();
	}

	public int getMemory() {
		return Memory.get();
	}

	public String getToolStatus() {
		return ToolStatus.get();
	}

	public VM(VirtualMachine vm){
		GuestOS = new SimpleStringProperty(vm.getConfig().guestFullName);
		System.out.println(GuestOS);
		Name = new SimpleStringProperty(vm.getName());
		System.out.println(Name);
		PowerState = new SimpleStringProperty(vm.getSummary().getRuntime().powerState.name());
		System.out.println(PowerState);
		CPU = new SimpleIntegerProperty(vm.getSummary().config.numCpu);
		System.out.println(CPU);
		Status = new SimpleStringProperty(vm.getSummary().getOverallStatus().name());
		System.out.println(Status);
		Memory =new SimpleIntegerProperty(vm.getSummary().config.memorySizeMB);
		System.out.println(Memory);
		ToolStatus = new SimpleStringProperty(vm.getSummary().guest.toolsStatus.name());
		System.out.print(ToolStatus);
	}
}
