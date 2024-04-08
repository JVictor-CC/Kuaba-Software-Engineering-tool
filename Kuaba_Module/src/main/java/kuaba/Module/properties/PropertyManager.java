package kuaba.Module.properties;

import org.modelio.metamodel.uml.infrastructure.ModelElement;
import kuaba.Module.properties.*;

public class PropertyManager {
	
	public static IPropertyContent getPalette(ModelElement element) {
		if (element.isStereotyped("KuabaModule","Question")) {
			return new KuabaQuestionProperties();
		}
		if (element.isStereotyped("KuabaModule","Idea")) {
			return new KuabaIdeaProperties();
		}
		if (element.isStereotyped("KuabaModule","Argument")) {
			return new KuabaArgumentProperties();
		}
		if (element.isStereotyped("KuabaModule","isAddressedBy")) {
			return new KuabaDecisionProperties();
		}
		return new DefaultProperties();
	}
	
}
