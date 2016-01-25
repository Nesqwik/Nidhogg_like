package nidhogglike.game;

import java.util.HashMap;
import java.util.Map;

import gameframework.base.ObservableValue;
import gameframework.game.GameData;

public class NidhoggGameData extends GameData {
	protected Map<String, ObservableValue<Integer>> observableValues;
	
	public NidhoggGameData(NidhoggConfiguration configuration) {
		super(configuration);
		observableValues = new HashMap<>();
	}
	
	public void setObservableValue(String key, Integer value) {
		if(!observableValues.containsKey(key)) {
			observableValues.put(key, new ObservableValue<Integer>(value));
		} else {
			observableValues.get(key).setValue(value);
		}
	}
	
	public void incrementObservableValue(String key, Integer value) {
		Integer lastValue = getObservableValue(key).getValue();
		lastValue += value;
		setObservableValue(key, lastValue);
	}
	
	public ObservableValue<Integer> getObservableValue(String key) {
		return observableValues.get(key);
	}
	
}
