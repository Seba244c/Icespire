package io.github.sebsa.icespire.utils;
import org.reflections.*;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.sebsa.icespire.ecs.Component;
import io.github.sebsa.icespire.ecs.Entity;

import io.github.sebsa.icespire.ecs.Scene;

public class SceneUtils {
	public static void jsonToScene() {
		Reflections reflections = new Reflections("");
		Object[] componenTypes = reflections.getSubTypesOf(Component.class).toArray();
		
		//String str=componenTypes[0].toString();
	    for(Object className : componenTypes) {
	    	System.out.println(className.toString().substring(className.toString().lastIndexOf('.')+1));
	    }
	}
	
	public static void sceneToJson(Scene scene) {
		JSONObject obj = new JSONObject();
		JSONArray entities = new JSONArray();
		
		for(Entity entity : scene.getEntities()) {
			JSONObject entiyObject = new JSONObject();
			JSONObject transformObject = new JSONObject();
			
			JSONArray componentArray = new JSONArray();
			
			transformObject.append("x", entity.getTransform().getPosition().x);
			transformObject.append("y", entity.getTransform().getPosition().y);
			transformObject.append("z", entity.getTransform().getPosition().z);
			
			entiyObject.append("n", entity.getName());
			entiyObject.append("t", transformObject);

			for(Component component : entity.getAllComponents()) {
				JSONObject componentObject = new JSONObject();
				String className = component.getClass().getName().substring(component.getClass().getName().lastIndexOf('.')+1);
				componentObject.append("c", className);
				componentArray.put(componentObject);
			}
			
			entiyObject.append("c", componentArray);
			
			entities.put(entiyObject);
		}
		obj.put("entities", entities);
		Logging.infoLog("SceneUtils", obj.toString());
	}
}
