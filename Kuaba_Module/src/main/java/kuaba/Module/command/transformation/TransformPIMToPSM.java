package kuaba.Module.command.transformation;

import java.util.List;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.api.modelio.model.ITransaction;

public class TransformPIMToPSM extends DefaultModuleCommandHandler {
	
    public TransformPIMToPSM() {
        super();
    }

    @Override
    public boolean accept(List<MObject> selectedElements, IModule module) {
    	IModelingSession session = module.getModuleContext().getModelingSession();
    	Stereotype PIMPackage = session.getMetamodelExtensions().getStereotype("KuabaModule", "PIMPackage", module.getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Package.class));
    	if (selectedElements.size() == 1 && selectedElements.get(0) instanceof Package && ((Package) selectedElements.get(0)).isStereotyped(PIMPackage)) {
    		return selectedElements.size() == 1;
    	}
        return false;
    }
  
    
    @Override
    public void actionPerformed(List<MObject> selectedElements, IModule module) {
    	
        IModelingSession session = module.getModuleContext().getModelingSession();
        if (selectedElements.size() == 1 && selectedElements.get(0) instanceof Package) {
            
        	Package selectedPackage = (Package) selectedElements.get(0);
        	Package parent = (Package) selectedPackage.getOwner();
            
        	try (ITransaction t = session.createTransaction("Process Package Contents")) {
        		Stereotype PSMPackage = session.getMetamodelExtensions().getStereotype("KuabaModule", "PSMPackage", module.getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Package.class));
                Package target = session.getModel().createPackage("PSM Package", parent, PSMPackage); 
             
                processPackageContents(session, selectedPackage, module, target);
                t.commit();
        	} catch (Exception e) {
        		module.getModuleContext().getLogService().error(e);
        	}      	
        }
    }

    public void processPackageContents(IModelingSession session, Package sourcePackage, IModule module, Package target) {
    	
    	try (ITransaction t = session.createTransaction("Process Package Contents")) {
        		
            Stereotype entityStereotype = session.getMetamodelExtensions().getStereotype("LocalModule", "Entity", module.getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Class.class));
        	Stereotype valueObjectStereotype = session.getMetamodelExtensions().getStereotype("LocalModule", "ValueObject", module.getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Class.class));
        	Stereotype serviceStereotype = session.getMetamodelExtensions().getStereotype("LocalModule", "Service", module.getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Class.class));
         	Stereotype aggregateRootStereotype = session.getMetamodelExtensions().getStereotype("LocalModule", "AggregateRoot", module.getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Class.class));
        	
            for (MObject element : sourcePackage.getCompositionChildren()) {
                if (element instanceof Class) {
                	
                	if (entityStereotype != null && ((Class) element).isStereotyped(entityStereotype) || valueObjectStereotype != null && ((Class) element).isStereotyped(valueObjectStereotype)) {
                		EntityValueObjectMapping mapping = new EntityValueObjectMapping();
                		mapping.mapEntityValueObject(session, module, target, (Class) element);
                	}
                   
                	if (serviceStereotype != null && ((Class) element).isStereotyped(serviceStereotype)) {
                		ServiceMapping mp = new ServiceMapping();
                		mp.mapService(session, module, target, (Class) element);
                	}
                	if (aggregateRootStereotype != null && ((Class) element).isStereotyped(aggregateRootStereotype)) {
                		AggregateMapping mp = new AggregateMapping();
                		mp.mapAggregate(session, module, target, (Class) element);
                	}
                }
            }
            t.commit();
        } catch (Exception e) {
            module.getModuleContext().getLogService().error(e);
        }
    }
}