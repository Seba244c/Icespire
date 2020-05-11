package io.github.seba244c.icespire.ecs;
import java.util.ArrayList;
import java.util.List;

/**
 * An object wich exists in a game world
 * @author Sebsa
 * @since 20-406 
 */
public class Entity {
    private List<Component> components;
    private Transform transform;
    private String name;
    
    public Entity() {
        components = new ArrayList<Component>();
        this.transform = new Transform();
    }
    
    public Entity(String name) {      
        components = new ArrayList<Component>();
        this.transform = new Transform();
        this.name = name;
    }
    
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(c);
                return;
            }
        }
    }

    public List<Component> getAllComponents() {
        return this.components;
    }

    public void addComponent(Component c) {
        components.add(c);
        c.entity = this;
    }
    
    public void update() {
    	for(Component component : components) {
    		component.update();
    	}
    }

	public Transform getTransform() {
		return transform;
	}

	public String getName() {
		if(name==null)
			return getClass().getSimpleName() + '@' + Integer.toHexString(hashCode());
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}