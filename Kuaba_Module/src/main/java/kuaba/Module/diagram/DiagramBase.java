package kuaba.Module.diagram;

import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.style.IStyleHandle;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.event.IModelChangeHandler;
import org.modelio.api.module.context.IModuleContext;

import kuaba.Module.api.IKuabaPeerModule;
import kuaba.Module.listeners.ModelChangeHandler;

public abstract class DiagramBase {
    protected AbstractDiagram element;

    public DiagramBase(ModelElement owner, IStyleHandle style, IModuleContext context, String stereotypeName, String diagramName, Class<? extends MObject> diagramClass) throws Exception {
        try {
            IModelingSession session = context.getModelingSession();
            Stereotype stereotype = session.getMetamodelExtensions().getStereotype(IKuabaPeerModule.MODULE_NAME, stereotypeName, context.getModelioServices().getMetamodelService().getMetamodel().getMClass(diagramClass));
            
            if(diagramClass.equals(StaticDiagram.class)) {
            	this.element = session.getModel().createStaticDiagram(diagramName, owner, stereotype);
            }
            else if(diagramClass.equals(ClassDiagram.class)) {
            	this.element = session.getModel().createClassDiagram(diagramName, owner, stereotype);
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (IDiagramHandle diagramHandler = context.getModelioServices().getDiagramService().getDiagramHandle(getElement())) {
            diagramHandler.getDiagramNode().setStyle(style);
            diagramHandler.save();
            diagramHandler.close();
        }

        IModelChangeHandler modelHandler = (IModelChangeHandler) new ModelChangeHandler();
        context.getModelingSession().addModelHandler(modelHandler);
    }
    
    public AbstractDiagram getElement() {
        return this.element;
    }
}