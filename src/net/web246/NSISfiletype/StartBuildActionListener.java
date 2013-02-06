package net.web246.NSISfiletype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "File", id = "net.web246.NSISfiletype.StartBuildActionListener")
@ActionRegistration(iconBase = "setup.png", iconInMenu = true, displayName = "#CTL_StartBuildActionListener")
@ActionReference(path = "Loaders/text/x-nsis/Actions", position = 150 )
@Messages("CTL_StartBuildActionListener=Build Installer")
public final class StartBuildActionListener implements ActionListener {

    private final NSISDataObject context;

    public StartBuildActionListener(NSISDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        FileObject f = context.getPrimaryFile();
        String displayName = FileUtil.getFileDisplayName(f);
        String msg = "I am " + displayName + ". Hear me roar!";
        NotifyDescriptor nd = new NotifyDescriptor.Message(msg);
        DialogDisplayer.getDefault().notify(nd);
    }
}
