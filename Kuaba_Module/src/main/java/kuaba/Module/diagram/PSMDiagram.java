package kuaba.Module.diagram;

import org.modelio.api.modelio.diagram.style.IStyleHandle;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

public class PSMDiagram extends DiagramBase {
    public PSMDiagram(ModelElement owner, IStyleHandle style, IModuleContext context) throws Exception {
        super(owner, style, context, "PSMDiagram", "PSM Diagram", ClassDiagram.class);
    }
}