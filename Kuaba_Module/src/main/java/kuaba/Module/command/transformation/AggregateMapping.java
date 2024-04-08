package kuaba.Module.command.transformation;

import org.eclipse.emf.common.util.EList;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.IModule;
import org.modelio.metamodel.uml.statik.AggregationKind;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.Package;

public class AggregateMapping extends GeneralMapping{
	
	public void mapAggregate(IModelingSession session, IModule module, Package target, Class element) {
    	
		Stereotype aggregatePartStereotype = session.getMetamodelExtensions().getStereotype("LocalModule", "AggregatePart", module.getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Class.class));
		try (ITransaction t = session.createTransaction("Process Aggregate")) {
			
			EList<AssociationEnd> associationEnds = getElementinTarget(session, module, target, element).getOwnedEnd();
			
			for (AssociationEnd associationEnd : associationEnds) {
				if (associationEnd.getOpposite().getOwner().isStereotyped(aggregatePartStereotype)) {
					associationEnd.setAggregation(AggregationKind.KINDISCOMPOSITION);
				}
			}
            t.commit();
        } catch (Exception e) {
            module.getModuleContext().getLogService().error(e);
        }
	}	
}
