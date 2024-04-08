package kuaba.Module.properties;

import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

public interface IPropertyContent {

	void changeProperty(ModelElement element, int row, String value);

	void update(ModelElement element, IModulePropertyTable table);
}