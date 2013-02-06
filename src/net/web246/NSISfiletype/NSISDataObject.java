/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.web246.NSISfiletype;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Cookie;
import org.openide.nodes.PropertySupport.ReadOnly;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@Messages({ "LBL_NSIS_LOADER=Files of NSIS" })
@MIMEResolver.ExtensionRegistration( displayName = "#LBL_NSIS_LOADER", mimeType = "text/x-nsis", extension = {"nsi"})
@DataObject.Registration( mimeType = "text/x-nsis", iconBase = "NSIS.png", displayName = "#LBL_NSIS_LOADER", position = 300)
@ActionReferences({
    @ActionReference( 
        path = "Loaders/text/x-nsis/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"), 
        position = 100, separatorAfter = 200
    ),
    @ActionReference(
        path = "Loaders/text/x-nsis/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
        position = 300
    ),
    @ActionReference(
        path = "Loaders/text/x-nsis/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
        position = 400,
        separatorAfter = 500
    ),
    @ActionReference(
        path = "Loaders/text/x-nsis/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
        position = 600
    ),
    @ActionReference(
        path = "Loaders/text/x-nsis/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
        position = 700,
        separatorAfter = 800
    ),
    @ActionReference(
        path = "Loaders/text/x-nsis/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
        position = 900,
        separatorAfter = 1000
    ),
    @ActionReference(
        path = "Loaders/text/x-nsis/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
        position = 1100,
        separatorAfter = 1200
    ),
    @ActionReference(
        path = "Loaders/text/x-nsis/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
        position = 1300
    ),
    @ActionReference(
        path = "Loaders/text/x-nsis/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
        position = 1400
    )
})
public class NSISDataObject extends MultiDataObject {

    public NSISDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/x-nsis", true);
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @MultiViewElement.Registration(
        displayName = "#LBL_NSIS_EDITOR",
        iconBase = "NSIS.png",
        mimeType = "text/x-nsis",
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
        preferredID = "NSIS",
        position = 1000
    )
    @Messages("LBL_NSIS_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MultiViewEditorElement(lkp);
    }
    
    /* this is what will be created instead of a generic dataNode ! */
    @Override
    protected Node createNodeDelegate() {
        return new NSISNode(this, Children.create(new NSISChildFactory(this), true), getLookup());
//        return new DataNode(this, Children.LEAF, getLookup());
    }
    class NSISNode extends DataNode {
        public NSISNode(NSISDataObject aThis, final Children kids, Lookup lookup) {
            super(aThis, kids, lookup);
            aThis.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    System.err.println("" + evt.getPropertyName());
                    if(evt.getPropertyName().equals("modified")) {
                        NodeRefreshEvent nre;
                        NSISDataObject.this.getCookieSet().add(nre = new NodeRefreshEvent());
                        NSISDataObject.this.getCookieSet().remove(nre);
                    }
                }
            });
        }
        @Override
        protected Sheet createSheet() {
            Sheet sheet = super.createSheet();
            Sheet.Set set = Sheet.createPropertiesSet();
            sheet.put(set);
            set.put(new LineCountProperty(this));
            return sheet;
        }
        private class LineCountProperty extends ReadOnly<Integer> {
            private final NSISNode node;
            public LineCountProperty(NSISNode node) {
                super("lineCount", Integer.class, "Line Count", "Number of Lines");
                this.node = node;
            }
            @Override
            public Integer getValue() throws IllegalAccessException, InvocationTargetException {
                int lineCount = 0;
                DataObject abcDobj = node.getDataObject();
                FileObject abcFo = abcDobj.getPrimaryFile();
                try {
                    lineCount = abcFo.asLines().size();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
                return lineCount;
            }
        }
    }

    static class NSISCatNode extends AbstractNode {
        NSISDataObject aThis;
        String search;
        public NSISCatNode(NSISDataObject aThis, String search, final Children kids, Lookup lookup) {
            super(kids, lookup);
            this.aThis = aThis;
            this.search = search;
        }

        @Override
        public Image getIcon(int arg0) {
            return super.getIcon(arg0); 
        }

        @Override
        public String getHtmlDisplayName() {
            return search;
        }
        
        @Override
        public Image getOpenedIcon(int arg0) {
            return super.getOpenedIcon(arg0); 
        }
        
        @Override
        protected Sheet createSheet() {
            Sheet sheet = super.createSheet();
            Sheet.Set set = Sheet.createPropertiesSet();
            sheet.put(set);
            set.put(new NSISCatNode.LineCountProperty(this));
            return sheet;
        }
        private class LineCountProperty extends ReadOnly<Integer> {
            private final NSISCatNode node;
            public LineCountProperty(NSISCatNode node) {
                super("lineCount", Integer.class, "Line Count", "Number of Lines");
                this.node = node;
            }
            @Override
            public Integer getValue() throws IllegalAccessException, InvocationTargetException {
                int lineCount = 0;
                FileObject abcFo = node.aThis.getPrimaryFile();
                try {
                    lineCount = abcFo.asLines().size();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
                return lineCount;
            }
        }
    }    
    private static class NSISChildFactory extends ChildFactory<String> {//implements LookupListener {
        private final NSISDataObject dObj;
//        private final Result<NodeRefreshEvent> lookupResult;

        public NSISChildFactory(NSISDataObject dObj) {
            this.dObj = dObj; 
//            lookupResult = dObj.getLookup().lookupResult(NodeRefreshEvent.class);
//            lookupResult.addLookupListener(this);
        }
        @Override
        protected boolean createKeys(List list) {
            list.add("stuff");
//            FileObject fObj = dObj.getPrimaryFile();
//            try {
//                List<String> dObjContent = fObj.asLines();
//                for(String s : dObjContent) {
//                    if(s.trim().startsWith("!define ")) {
//                        list.add(s);
//                    }
//                }
//            } catch (IOException ex) {
//                Exceptions.printStackTrace(ex);
//            }
            return true;
        }
        @Override
        protected Node createNodeForKey(String key) {
            Node childNode = new NSISCatNode(dObj,key, Children.create(new NSISCatChildFactory(dObj), true), dObj.getLookup());
            return childNode;
        }
//
//        @Override
//        public void resultChanged(LookupEvent ev) {
//            Lookup.Result r = (Lookup.Result) ev.getSource();
//            Collection<Cookie> c = r.allInstances();
//            for (Cookie object : c)
//            {
//                if (object instanceof NodeRefreshEvent){
//                    refresh(true);
//                    break; // only call once regardless of how many events there are
//                }
//            }
//        }
    }    
    private static class NSISCatChildFactory extends ChildFactory<String> implements LookupListener {
        private final NSISDataObject dObj;
        private final Result<NodeRefreshEvent> lookupResult;

        public NSISCatChildFactory(NSISDataObject dObj) {
            this.dObj = dObj; 
            lookupResult = dObj.getLookup().lookupResult(NodeRefreshEvent.class);
            lookupResult.addLookupListener(this);
        }
        @Override
        protected boolean createKeys(List list) {
            FileObject fObj = dObj.getPrimaryFile();
            try {
                List<String> dObjContent = fObj.asLines();
                for(String s : dObjContent) {
                    if(s.trim().startsWith("!define ")) {
                        list.add(s);
                    }
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            return true;
        }
        @Override
        protected Node createNodeForKey(String key) {
            Node childNode = new AbstractNode(Children.LEAF);
            childNode.setDisplayName(key);
            return childNode;
        }

        @Override
        public void resultChanged(LookupEvent ev) {
            Lookup.Result r = (Lookup.Result) ev.getSource();
            Collection<Cookie> c = r.allInstances();
            for (Cookie object : c)
            {
                if (object instanceof NodeRefreshEvent){
                    refresh(true);
                    break; // only call once regardless of how many events there are
                }
            }
        }
    }    
}
