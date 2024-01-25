package kuaba.Module.properties;

import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

import kuaba.Module.impl.KuabaPeerModule;
import kuaba.Module.utils.Utils;

public class KuabaQuestionProperties implements IPropertyContent {
	
	@Override
	public void changeProperty(ModelElement element, int row, String value) {
		
		if (row == 1) {
			element.setName(value);
			Utils.setPropertyValue(KuabaPeerModule.MODULE_NAME, "Text", value, element);
		}
		if (row == 2)
			Utils.setPropertyValue(KuabaPeerModule.MODULE_NAME, "Type", value, element);
		if (row == 3) 
			Utils.setPropertyValue(KuabaPeerModule.MODULE_NAME, "CrationDate", value, element);
	}

	@Override
	public void update(ModelElement element, IModulePropertyTable table) {
		table.addProperty("Text", element.getName());
		String[] typePropertyValues = {"XOR", "AND", "OR"};
		table.addProperty("Type", Utils.getPropertyValue("Type", element), typePropertyValues);	
		if (Utils.getPropertyValue("CreationDate", element) == "") {
	        table.addProperty("CreationDate", Utils.setDate(KuabaPeerModule.MODULE_NAME ,"CreationDate", element));
	    } else {
	        table.addProperty("CreationDate", Utils.getPropertyValue("CreationDate", element));
	    }
	}

}
