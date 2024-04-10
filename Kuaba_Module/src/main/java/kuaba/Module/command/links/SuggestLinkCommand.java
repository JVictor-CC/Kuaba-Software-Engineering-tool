package kuaba.Module.command.links;

import java.util.List;

import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramLink.LinkRouterKind;
import org.modelio.api.modelio.diagram.ILinkRoute;
import org.modelio.api.modelio.diagram.tools.DefaultLinkTool;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.api.modelio.diagram.IDiagramLink;
import kuaba.Module.impl.KuabaModule;

public class SuggestLinkCommand extends DefaultLinkTool {

	@Override
	public boolean acceptFirstElement(IDiagramHandle diagramHandle, IDiagramGraphic targetNode) {
		ModelElement element = (ModelElement) targetNode.getElement();
		
		if (element.isStereotyped("KuabaModule", "Idea")) {
			return true;
		}
		else if (element.isStereotyped("KuabaModule", "Argument")) {
			return true;
		}
		else if (element.isStereotyped("KuabaModule", "Question")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean acceptSecondElement(IDiagramHandle diagramHandle, IDiagramGraphic originNode, IDiagramGraphic targetNode) {
		
		ModelElement targetElement = (ModelElement) targetNode.getElement();
		
		if (targetElement.isStereotyped("KuabaModule", "Question")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void actionPerformed(IDiagramHandle diagramHandle, IDiagramGraphic originNode, IDiagramGraphic targetNode, LinkRouterKind routerType, ILinkRoute path) {
		
		ModelElement originElement = (ModelElement) originNode.getElement();
		ModelElement targetElement = (ModelElement) targetNode.getElement();
		
		IModelingSession session = KuabaModule.getInstance().getModuleContext().getModelingSession();
		
		try (ITransaction t = session.createTransaction("Link Creation")) {
            
			Dependency newLink = session.getModel().createDependency(originElement, targetElement, "KuabaModule", "suggestsLink");
			newLink.setName("Suggests");
			
			List<IDiagramGraphic> diagramHandler = diagramHandle.unmask(newLink, 0, 0);
			IDiagramLink link = (IDiagramLink) diagramHandler.get(0);
			link.setRouterKind(routerType);
			link.setRoute(path);
			diagramHandle.save();
			
			link.setLinePattern(3);
			link.setLineRadius(5);
			link.setLineColor("128,128,128");
            t.commit();
           
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
