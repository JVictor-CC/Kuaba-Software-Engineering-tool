package kuaba.Module.command.transformation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.diagram.style.IStyleHandle;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.vcore.smkernel.mapi.MObject;

import kuaba.Module.diagram.PIMDiagram;

public class TransformCIMToPIM extends DefaultModuleCommandHandler {

    @Override
    public boolean accept(List<MObject> selectedElements, IModule module) {
    	IModelingSession session = module.getModuleContext().getModelingSession();
    	Stereotype CIMPackage = session.getMetamodelExtensions().getStereotype("KuabaModule", "CIMPackage", 
    			module.getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Package.class));
    	
    	if (selectedElements.size() == 1 &&
			selectedElements.get(0) instanceof Package &&
			((Package) selectedElements.get(0)).isStereotyped(CIMPackage)) {
    		
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
        	
            try (ITransaction transaction = session.createTransaction("Process Package Contents")) {
                Stereotype questionElement = session.getMetamodelExtensions().getStereotype("KuabaModule", "Question", 
                		module.getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Class.class));
                Stereotype PIMPackage = session.getMetamodelExtensions().getStereotype("KuabaModule", "PIMPackage", 
                		module.getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Package.class));
                Package targetPackage = session.getModel().createPackage("PIM Package", parent, PIMPackage); 
                List<Class> createdClasses = new ArrayList<>(); 
                
                for (MObject element : selectedPackage.getCompositionChildren()) {
                    if (element instanceof Class && questionElement != null && ((Class) element).isStereotyped(questionElement)) {                      
                    	createdClasses.addAll(processDecisions((Classifier) element, module, targetPackage, session));
                    }
                }
                processPIMDiagram(module, targetPackage, createdClasses);
                
                transaction.commit();
            } catch (Exception e) {
                module.getModuleContext().getLogService().error(e);
            }
        } 
    }
    
    private List<Class> processDecisions(Classifier element, IModule module, Package targetPackage, IModelingSession session) {
        
        EList<AssociationEnd> associationEnds = element.getOwnedEnd();
        List<Class> createdClasses = new ArrayList<>();
        
        try (ITransaction t = session.createTransaction("Process Package Contents")) {
        	for (AssociationEnd associationEnd : associationEnds) {
	        	if ("A".equals(associationEnd.getName())) {
	        		Class newClass = session.getModel().createClass(associationEnd.getTarget().getName(), targetPackage, null);
	        		createdClasses.add(newClass);
	        	}
	        }
	        t.commit();
        } catch (Exception e) {       
            module.getModuleContext().getLogService().error(e);
        }
		return createdClasses;
    }
    
    private void processPIMDiagram(IModule module, Package targetPackage, List<Class> createdClasses) {
    	IModuleContext context = module.getModuleContext();
    	IModelingSession session = context.getModelingSession();
    	
    	try (ITransaction transaction = session.createTransaction("Process Package Contents")) {
    		IDiagramService diagramService = context.getModelioServices().getDiagramService();
	    	IStyleHandle style = diagramService.getStyle("PIMPSMStyle");
	        PIMDiagram pim = new PIMDiagram(targetPackage, style, context);
	    	IDiagramHandle diagramHandler = diagramService.getDiagramHandle(pim.getElement());
	
	    	int X = 0;
	    	int Y = 200;
	    	int ctrlY = 1;
	    	
	    	for (Class newClass : createdClasses) {
	    		Y = 100;
	    		if (ctrlY == 1) {
	    			Y+=100;
	    			X+=200;		
	    			diagramHandler.unmask(newClass, X, Y);
	    			ctrlY = 2;
	    		} else if (ctrlY == 2) {
	    			Y-= 100; 
	    			diagramHandler.unmask(newClass, X, Y);
	    			ctrlY = 1;
	    		}
	    	}
	    	transaction.commit();
    	} catch (Exception e) {
    		module.getModuleContext().getLogService().error(e);
		}
    }
}
