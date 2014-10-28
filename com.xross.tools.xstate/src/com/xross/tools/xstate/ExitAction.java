package com.xross.tools.xstate;

/**
 * Action triggered when existing a state
 * @author Jerry He
 */
public interface ExitAction {
	void exit(String sourceStateId, Event event);
}
