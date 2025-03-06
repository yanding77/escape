package escape.builder;

import escape.required.Rule.*;

public class RuleDescriptor {
	public RuleID ruleId;
	public int ruleValue;

	public RuleDescriptor() { }

	public RuleDescriptor(RuleID ruleId, int ruleValue) {
		this.ruleId = ruleId;
		this.ruleValue = ruleValue;
	}

	@Override
	public String toString() {
		return "RuleDescriptor [ruleId=" + ruleId + ", ruleValue=" + ruleValue + "]";
	}
}
