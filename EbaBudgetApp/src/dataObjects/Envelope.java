package dataObjects;
import java.math.BigDecimal;

import data.Database;

public class Envelope {

    private int priority;
    private String name;
    private BigDecimal amount;
    private int fillSetting;
    private int fillAmount;
    private boolean cap;
    private int capAmount;
    private boolean extra;
    private boolean Default;

    public Envelope(int priority, String name, BigDecimal amount, int fillSetting, int fillAmount, boolean cap, int capAmount, boolean extra, boolean Default) {
        this.priority = priority;
        this.name = name;
        this.amount = amount;
        this.fillSetting = fillSetting;
        this.fillAmount = fillAmount;
        this.cap = cap;
        this.capAmount = capAmount;
        this.extra = extra;
        this.Default = Default;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (Database.editEnvelope(this.name, this.name, priority, this.amount, this.fillSetting, this.fillAmount, this.cap, this.capAmount, this.extra, this.Default)) {
            this.priority = priority;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (Database.editEnvelope(this.name, name, this.priority, this.amount, this.fillSetting, this.fillAmount, this.cap, this.capAmount, this.extra, this.Default)) {
            this.name = name;
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (Database.editEnvelope(this.name, this.name, this.priority, amount, this.fillSetting, this.fillAmount, this.cap, this.capAmount, this.extra, this.Default)) {
            this.amount = amount;
        }
    }

    public int getFillSetting() {
        return fillSetting;
    }

    public void setFillSetting(int fillSetting) {
        if (Database.editEnvelope(this.name, this.name, this.priority, this.amount, fillSetting, this.fillAmount, this.cap, this.capAmount, this.extra, this.Default)) {
            this.fillSetting = fillSetting;
        }
    }

    public int getFillAmount() {
        return fillAmount;
    }

    public void setFillAmount(int fillAmount) {
        if (Database.editEnvelope(this.name, this.name, this.priority, this.amount, this.fillSetting, fillAmount, this.cap, this.capAmount, this.extra, this.Default)) {
            this.fillAmount = fillAmount;
        }
    }

    public boolean hasCap() {
        return cap;
    }

    public void setCap(boolean cap) {
        if (Database.editEnvelope(this.name, this.name, this.priority, this.amount, this.fillSetting, this.fillAmount, cap, this.capAmount, this.extra, this.Default)) {
            this.cap = cap;
        }
    }

    public int getCapAmount() {
        return capAmount;
    }

    public void setCapAmount(int capAmount) {
        if (Database.editEnvelope(this.name, this.name, this.priority, this.amount, this.fillSetting, this.fillAmount, this.cap, capAmount, this.extra, this.Default)) {
            this.capAmount = capAmount;
        }
    }

    public boolean isExtra() {
        return extra;
    }

    public void setExtra(boolean extra) {
        if (Database.editEnvelope(this.name, this.name, this.priority, this.amount, this.fillSetting, this.fillAmount, this.cap, this.capAmount, extra, this.Default)) {
            this.extra = extra;
        }
    }

    public boolean isDefault() {
        return Default;
    }

    public void setDefault(boolean Default) {
        if (Database.editEnvelope(this.name, this.name, this.priority, this.amount, this.fillSetting, this.fillAmount, this.cap, this.capAmount, this.extra, Default)) {
            this.Default = Default;
        }
    }

    @Override
    public String toString() {
        return "\npriority: " + this.priority +
                "\nName: " + this.name +
                "\nAmount: " + this.amount +
                "\nFill Amount: " + this.fillAmount +
                "\nFill Setting: " + this.fillSetting +
                "\nCap: " + this.cap +
                "\nCap Amount: " + this.capAmount +
                "\nExtra: " + this.extra +
                "\nDefault: " + this.Default;
    }
}
