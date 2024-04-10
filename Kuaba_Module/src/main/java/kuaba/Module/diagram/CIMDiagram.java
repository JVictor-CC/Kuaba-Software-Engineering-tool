package kuaba.Module.diagram;

import org.modelio.api.modelio.diagram.style.IStyleHandle;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

public class CIMDiagram extends DiagramBase {
    public CIMDiagram(ModelElement owner, IStyleHandle style, IModuleContext context) throws Exception {
        super(owner, style, context, "CIMDiagram", "Kuaba Diagram", StaticDiagram.class);
    }
}