package moduleObjects;

public class VendorModule {

	public String name;
	public String prefName;
	public String prefEnv;
	
	//CREATE/GET
	public VendorModule(String vendor) {
		this.name = vendor;
	}
	
	//EDIT
	public VendorModule(String name, String prefName, String prefEnv) {
		this.name = name;
		this.prefName = prefName;
		this.prefEnv = prefEnv;
	}
}
