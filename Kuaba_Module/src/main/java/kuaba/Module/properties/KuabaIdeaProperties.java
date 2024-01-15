package kuaba.Module.properties;

import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

import kuaba.Module.impl.KuabaPeerModule;
import kuaba.Module.utils.Utils;

public class KuabaIdeaProperties implements IPropertyContent {
	@Override
	public void changeProperty(ModelElement element, int row, String value) {
		if (row == 1) {
			element.setName(value);
			Utils.setPropertyValue( KuabaPeerModule.MODULE_NAME, "Text", value, element);
		}
	}

	@Override
	public void update(ModelElement element, IModulePropertyTable table) {
		table.addProperty("Text", element.getName());
		
		if (Utils.getPropertyValue("CreationDate", element) == "") {
	        table.addProperty("CreationDate", Utils.setDate( KuabaPeerModule.MODULE_NAME, "CreationDate", element));
	    } else {
	        table.addProperty("CreationDate", Utils.getPropertyValue("CreationDate", element));
	    }
	}

}
