package moduleObjects;

public class EnvelopeModule {

	public int priority;
	public String name;
	public double amount;
	public int fillSetting;
	public double fillAmount;
	public boolean cap;
	public double capAmount;
	
	public EnvelopeModule(int priority, String name, double amount, int fillSetting, double fillAmount, boolean cap, double capAmount) {
		this.priority = priority;
		this.name = name;
		this.amount = amount;
		this.fillSetting = fillSetting;
		this.fillAmount = fillAmount;
		this.cap = cap;
		this.capAmount = capAmount;
	}
	
	
}
