package kuaba.Module.properties;

import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

public class DefaultProperties implements IPropertyContent {

	@Override
	public void changeProperty(ModelElement element, int row, String value) {
		if (row == 1)
			element.setName(value);
	}

	@Override
	public void update(ModelElement element, IModulePropertyTable table) {
		table.addProperty("Name", element.getName());
	}

}