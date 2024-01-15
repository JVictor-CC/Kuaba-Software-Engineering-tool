package kuaba.Module.properties;

import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

import kuaba.Module.utils.Utils;
import kuaba.Module.impl.KuabaPeerModule;

public class KuabaDecisionProperties implements IPropertyContent {
	@Override
	public void changeProperty(ModelElement element, int row, String value) {
		
		if(row == 1)
			if (value == "Accepted") {
				Utils.setPropertyValue(KuabaPeerModule.MODULE_NAME, "Accepted", value, element);
				element.setName("A");
				Utils.setDate(KuabaPeerModule.MODULE_NAME, "Date", element);
			} else {
				Utils.setPropertyValue(KuabaPeerModule.MODULE_NAME, "Accepted", value, element);
				element.setName("R");
				Utils.setDate(KuabaPeerModule.MODULE_NAME, "Date", element);
			}
			
		if(row == 2)
			Utils.setPropertyValue(KuabaPeerModule.MODULE_NAME, "Justification", value, element);
			
	}

	@Override
	public void update(ModelElement element, IModulePropertyTable table) {
		
		String[] acceptedPropertyValues = {"Accepted", "Rejected"};
		String decision = Utils.getPropertyValue("Accepted", element);
		table.addProperty("Decision", decision, acceptedPropertyValues);
		
		table.addProperty("Justification", Utils.getPropertyValue("Justification", element));
		
		table.addProperty("Date", Utils.getPropertyValue("Date", element));
		
	}

}
