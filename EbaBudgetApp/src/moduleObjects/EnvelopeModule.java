package moduleObjects;

public class EnvelopeModule {

	public int priority;
	public String name;
	public double amount;
	public int fillSetting;
	public double fillAmount;
	public boolean cap;
	public double capAmount;
	public boolean extra;
	public boolean Default;
	public String toName;
	
	
	
	//CREATE
	public EnvelopeModule(String name, int fillSetting, double fillAmount, boolean cap, double capAmount, boolean extra, boolean Default) {
		this.name = name;
		this.fillSetting = fillSetting;
		this.fillAmount = fillAmount;
		this.cap = cap;
		this.capAmount = capAmount;
		this.extra = extra;
		this.Default = Default;
	}
	
	
	//REMOVE/GET
	public EnvelopeModule(String name) {
		this.name = name;
	}
	
	//TRANSFER
	public EnvelopeModule(String name, String toName, double amount) {
		this.name = name;
		this.toName = toName;
		this.amount = amount;
	}
	
	//EDIT
	public EnvelopeModule(int priority, String name, String newName, int fillSetting, double fillAmount, boolean cap, double capAmount, boolean extra, boolean Default) {
		this.priority = priority;
		this.name = name;
		this.toName = newName;
		this.fillSetting = fillSetting;
		this.fillAmount = fillAmount;
		this.cap = cap;
		this.capAmount = capAmount;
		this.extra = extra;
		this.Default = Default;
	}
	
	
}
