package kuaba.Module.command.transformation;

import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.module.IModule;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Package;

public interface ITarget {
    Class getElementinTarget(IModelingSession session, IModule module, Package target, Classifier element);
}
