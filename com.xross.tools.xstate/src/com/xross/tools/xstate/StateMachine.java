package com.xross.tools.xstate;

import java.util.List;
import java.util.NoSuchElementException;



public class StateMachine {
	private String name;
	private TransitionGuard gaurd;
	private State currentState;
	private State startState;
	private List<State> states;
	
	public StateMachine(String name, List<State> states, TransitionGuard gaurd) {
		this.name = name;
		this.gaurd = gaurd;
		this.states = states;
		verify();
	}
	
	public void verify() {
		for(State state: states) {
			if(state.getType() == StateType.start)
				if(startState == null)
					startState = state;
				else
					throw new IllegalStateException("Found multiple start states. There should only one start state for a state machine.");
		}
	}
	
	public String getName(){
		return name;
	}

	public State getCurrentState(){
		return currentState;
	}
	
	public void start() {
		if(currentState != null)
			throw new IllegalStateException(String.format("State machine: %s is already started. Currnet state id is %s", name, currentState.getId()));
		
		currentState = startState;
	}
	
	private State findState(String id) {
		for(State state: states) {
			if(state.getId().equalsIgnoreCase(id))
				return state;
		}
		
		throw new NoSuchElementException(String.format("The given state id: %s is not found", id));
	}
	
	public boolean isStarted() {
		if(currentState == null)
			return false;
		
		return currentState.getType() != StateType.end;
	}
	
	public boolean notify(Event event){
		State source = currentState;
		
		if(!currentState.isAcceptable(event))
			return false;
		
		Transition trans = currentState.getTransition(event);
		State target = trans.getTargetState();
		
		if(!gaurd.isTransitAllowed(source.getId(), target.getId(), event))
			return false;
		
		currentState.exist(event);
		trans.transit(event);
		target.enter(event);
		currentState = target;
		
		return true;
	}
	
	public boolean isEnded(){
		if(currentState == null)
			return false;
		
		return currentState.getType() == StateType.end;
	}
	
	/*
	 * Reset the state machine
	 */
	public void reset(){
		if(isEnded())
			throw new IllegalStateException(String.format("State machine: %s is already ended. Can not be reset.", name));
		
		currentState = startState;
	}
	
	public void restore(String id){
		currentState = findState(id);
	}
}
