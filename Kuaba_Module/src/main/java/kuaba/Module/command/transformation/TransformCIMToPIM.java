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

/**
 * Implementation of the IModuleContextualCommand interface.
 * <br>The module contextual commands are displayed in the contextual menu and in the specific toolbar of each module property page.
 * <br>The developer may inherit the DefaultModuleContextualCommand class which contains a default standard contextual command implementation.
 *
 */
public class TransformCIMToPIM extends DefaultModuleCommandHandler {
    /**
     * Constructor.
     */
    public TransformCIMToPIM() {
        super();
    }

    /**
     * @see org.modelio.api.module.commands.DefaultModuleContextualCommand#accept(java.util.List,
     *      org.modelio.api.module.IModule)
     */
    @Override
    public boolean accept(List<MObject> selectedElements, IModule module) {
    	IModelingSession session = module.getModuleContext().getModelingSession();
    	Stereotype CIMPackage = session.getMetamodelExtensions().getStereotype("KuabaModule", "CIMPackage", module.getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Package.class));
    	if (selectedElements.size() == 1 && selectedElements.get(0) instanceof Package && ((Package) selectedElements.get(0)).isStereotyped(CIMPackage)) {
    		return selectedElements.size() == 1;
    	}
        return false;
    }

    /**
     * @see org.modelio.api.module.commands.DefaultModuleContextualCommand#actionPerformed(java.util.List,
     *      org.modelio.api.module.IModule)
     */
    @Override
    public void actionPerformed(List<MObject> selectedElements, IModule module) {
        IModelingSession session = module.getModuleContext().getModelingSession();

        if (selectedElements.size() == 1 && selectedElements.get(0) instanceof Package) {
        	Package selectedPackage = (Package) selectedElements.get(0);
        	Package parent = (Package) selectedPackage.getOwner();
            try (ITransaction t = session.createTransaction("Process Package Contents")) {
                Stereotype kuabaquestion = session.getMetamodelExtensions().getStereotype("KuabaModule", "kuabaQuestion", module.getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Class.class));
                Stereotype PIMPackage = session.getMetamodelExtensions().getStereotype("KuabaModule", "PIMPackage", module.getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Package.class));
                Package target = session.getModel().createPackage("PIM Package", parent, PIMPackage); 
                               
                for (MObject element : selectedPackage.getCompositionChildren()) {
                    if (element instanceof Class) {
                        if (kuabaquestion != null && ((Class) element).isStereotyped(kuabaquestion)) {                      
                            processAssociations((Classifier) element, module, target, session);
                        }
                    }
                }
                t.commit();
            } catch (Exception e) {
                module.getModuleContext().getLogService().error(e);
            }
        } 
    }
    
    public void processAssociations(Classifier element, IModule module, Package pkg, IModelingSession session) {
        
        EList<AssociationEnd> associationEnds = element.getOwnedEnd();
        try (ITransaction t = session.createTransaction("Process Package Contents")) {
        	List<Class> createdClasses = new ArrayList<>();
        	boolean isTransformValid = false;
	        for (AssociationEnd associationEnd : associationEnds) {
	        	if ("A".equals(associationEnd.getName())) {
	        		isTransformValid = true;
	        		Class newClass = session.getModel().createClass(associationEnd.getTarget().getName(), pkg, null);
	        		createdClasses.add(newClass);
	        	}
	        }
	        if (isTransformValid) {
	        
	        	IModuleContext context = module.getModuleContext();
	        	IDiagramService diagramService = context.getModelioServices().getDiagramService();
	        	IStyleHandle style = diagramService.getStyle("PIMPSMStyle");
	            PIMDiagram pim = new PIMDiagram(pkg, style, context);
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
	        }
	        t.commit();
        } catch (Exception e) {       
            module.getModuleContext().getLogService().error(e);
        }
    }
}
