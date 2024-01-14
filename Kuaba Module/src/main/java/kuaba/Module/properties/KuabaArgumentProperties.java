package kuaba.Module.properties;

import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

import kuaba.Module.impl.KuabaPeerModule;
import kuaba.Module.utils.Utils;

public class KuabaArgumentProperties implements IPropertyContent {
	
	@Override
	public void changeProperty(ModelElement element, int row, String value) {
		if (row == 1) {
			Utils.setPropertyValue(KuabaPeerModule.MODULE_NAME, "Text", value, element);
			if (Utils.getPropertyValue("DisplayArguments", element) == "Show") {
				element.setName(value);
			}
		}
		
		if (row == 2) {
			if (value == "Show") {
				element.setName(Utils.getPropertyValue("Text", element));
				Utils.setPropertyValue(KuabaPeerModule.MODULE_NAME, "DisplayArguments", "Show", element);
			}
			else {
				element.setName("Argument");
				Utils.setPropertyValue(KuabaPeerModule.MODULE_NAME, "DisplayArguments", "Hide", element);
			}
		}
	}
	
	@Override
	public void update(ModelElement element, IModulePropertyTable table) {
		table.addProperty("Text", Utils.getPropertyValue("Text", element));
		
		String[] displayArgument = {"Show", "Hide"};
		String display = Utils.getPropertyValue("DisplayArguments", element);
		table.addProperty("Display Argument", display, displayArgument);
		
		if (Utils.getPropertyValue("CreationDate", element) == "") {
	        table.addProperty("CreationDate", Utils.setDate(KuabaPeerModule.MODULE_NAME, "CreationDate", element));
	    } else {
	        table.addProperty("CreationDate", Utils.getPropertyValue("CreationDate", element));
	    }
	}
}