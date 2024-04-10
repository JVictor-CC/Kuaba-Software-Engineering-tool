package kuaba.Module.command.transformation;

import java.util.ArrayList;
import org.modelio.api.modelio.IModelioServices;
import org.modelio.api.modelio.model.IModelManipulationService;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.IModule;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.metamodel.uml.statik.Package;

public class GeneralMapping implements ITarget, IOperations{
	
	@Override
	public void processOperations(IModelingSession session, IModule module, Classifier targetClassifier, MObject sourceElement) {
        IModuleContext moduleContext = module.getModuleContext();
        IModelioServices modelioServices = moduleContext.getModelioServices();
        IModelManipulationService modelService = modelioServices.getModelManipulationService();
          
        java.util.List<MObject> Lista = new ArrayList<MObject>();
        
       try (ITransaction transaction = session.createTransaction("Process Operations")) {
           
    	   for (Operation sourceOperation : ((Classifier) sourceElement).getOwnedOperation()) {
        	   Lista.add((MObject) sourceOperation);
           }
           modelService.copyTo(Lista, (MObject) targetClassifier);
           transaction.commit();
       } catch (Exception e) {
           module.getModuleContext().getLogService().error(e);
       }
   }
	
	@Override
	public Class getElementinTarget(IModelingSession session, IModule module, Package target, Classifier element) {
        
		for (MObject x : target.getCompositionChildren()) {
            if (x instanceof Class) {
                if (element.getName().equals(x.getName())) {
                    return (Class) x;
                }
            }
        }
        return null; 
    }	
}