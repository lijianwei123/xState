package com.xross.tools.xstate;

public interface TransitAction {
	void transit(String sourceStateId, String targetStateId, Event event);
}
