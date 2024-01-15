package kuaba.Module.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Date;

import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.modelio.model.IUmlModel;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TagType;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import kuaba.Module.impl.KuabaModule;

public class Utils {

	public static void setPropertyValue(String module,String property_name, String value, ModelElement element) {
		IModelingSession session = KuabaModule.getInstance().getModuleContext().getModelingSession();
		try (ITransaction t = session.createTransaction("");) {
			boolean exist = false;

			List<TaggedValue> tagElements = element.getTag();
			Iterator<TaggedValue> itChildren = tagElements.iterator();
			while (itChildren.hasNext()) {
				TaggedValue tag = itChildren.next();
				TagType type = tag.getDefinition();
				String tagname = type.getName();

				if (tagname.equals(property_name)) {
					exist = true;

					List<TagParameter> actualElements = tag.getActual();
					if (value != null) {
						TagParameter tagParam = actualElements.get(0);
						tagParam.setValue(value);
					}
				}
			}
			if (!exist) {
				IUmlModel model = KuabaModule.getInstance().getModuleContext().getModelingSession().getModel();
				TagType tag = KuabaModule.getInstance().getModuleContext().getModelingSession().getMetamodelExtensions().getTagType(module, property_name, element.getMClass());
				TaggedValue taggedValue = model.createTaggedValue(tag, element);
				if (value != null) {
					TagParameter param = model.createTagParameter();
					param.setValue(value);
					taggedValue.getActual().add(param);
				}
			}
			t.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getPropertyValue(String property_name, ModelElement element) {
		List<TaggedValue> tagElements = element.getTag();
		Iterator<TaggedValue> itChildren = tagElements.iterator();
		while (itChildren.hasNext()) {
			TaggedValue tag = itChildren.next();
			TagType type = tag.getDefinition();
			String tagname = type.getName();
			if (tagname.equals(property_name)) {
				List<TagParameter> actualElements = tag.getActual();
				TagParameter tagParam = actualElements.get(0);
				return tagParam.getValue();
			}
		}
		return "";
	}
	
	public static String setDate(String module,String property_name, ModelElement element) {
		Date date = new Date();
		setPropertyValue( module, property_name, date.toString(), element);
		return date.toString();
	}

}
