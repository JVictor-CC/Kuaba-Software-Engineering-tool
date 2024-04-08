package kuaba.Module.command.transformation;

import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.IModule;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.InterfaceRealization;
import org.modelio.metamodel.uml.statik.Operation;

public class ServiceMapping extends GeneralMapping{
	
    public void mapService(IModelingSession session, IModule module, org.modelio.metamodel.uml.statik.Package target, Class element) { 
		Stereotype javaClassStereotype = session.getMetamodelExtensions().getStereotype("JavaDesigner", "JavaClass", module.getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Class.class));
		Stereotype javaInterfaceStereotype = session.getMetamodelExtensions().getStereotype("JavaDesigner", "JavaInterface", module.getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Interface.class));
        try (ITransaction transaction = session.createTransaction("Process Service")) {
        	
        	Class myClass = session.getModel().createClass(element.getName(), target, javaClassStereotype);       	
        	Interface myInterface = session.getModel().createInterface("I" + element.getName(), target, javaInterfaceStereotype);       	
            InterfaceRealization realization = session.getModel().createInterfaceRealization();
            
            realization.setImplementer(myClass);                
            realization.setImplemented(myInterface);
            
            processOperations(session, module, myClass, element);                   
            processOperations(session, module, myInterface, element);
            
            for (Operation operation : myInterface.getOwnedOperation()) {
                operation.setIsAbstract(true);                
            }   
            transaction.commit();               
        } catch (Exception e) {
            module.getModuleContext().getLogService().error(e);
        }
	}
}
