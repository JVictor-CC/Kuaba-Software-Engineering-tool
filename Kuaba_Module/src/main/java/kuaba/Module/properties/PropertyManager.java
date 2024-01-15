package kuaba.Module.properties;

import org.modelio.metamodel.uml.infrastructure.ModelElement;
import kuaba.Module.properties.*;

public class PropertyManager {
	
	public static IPropertyContent getPalette(ModelElement element) {
		if (element.isStereotyped("KuabaModule","kuabaQuestion")) {
			return new KuabaQuestionProperties();
		}
		if (element.isStereotyped("KuabaModule","kuabaIdea")) {
			return new KuabaIdeaProperties();
		}
		if (element.isStereotyped("KuabaModule","kuabaArgument")) {
			return new KuabaArgumentProperties();
		}
		if (element.isStereotyped("KuabaModule","isAddressedBy")) {
			return new KuabaDecisionProperties();
		}
		return new DefaultProperties();
	}
	
}
