package fr.enib.navisu.app.controller.actions;

import fr.enib.navisu.app.AppTopComponent;
import fr.enib.navisu.app.controller.services.CatalogService;
import fr.enib.navisu.app.model.ChartsFoldersModel;
import fr.enib.navisu.charts.controller.dao.KAPChartCatalogDB;
import fr.enib.navisu.charts.controller.parser.kap.KAPParser;
import fr.enib.navisu.charts.model.kap.KAPChart;
import fr.enib.navisu.common.catalog.Catalog;
import fr.enib.navisu.common.utils.Utils;
import fr.enib.navisu.common.xml.XMLReader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.*;
import org.openide.windows.InputOutput;

public final class UpdateDatabaseAction implements ActionListener {

    public static final String ICON_PATH = "fr/enib/navisu/app/controller/actions/update-db.png";
    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(UpdateDatabaseAction.class.getName());
    /**
     * The InputOutput
     */
    private final InputOutput IO = AppTopComponent.io();
    /**
     * The RequestProcessor
     */
    private static final RequestProcessor RP = new RequestProcessor("Update database action", 1, true);
    /**
     * The Task
     */
    private RequestProcessor.Task theTask = null;
    /**
     * The Parser
     */
    private KAPParser parser;
    /**
     * The Charts path
     */
    private File root;
    /**
     * The DAO
     */
    private final KAPChartCatalogDB db = AppTopComponent.db();
    private List<CatalogService> providers = null;

    @Override
    public void actionPerformed(ActionEvent e) {

        providers = new ArrayList<>(Lookup.getDefault().lookupAll(CatalogService.class));

        // La barre de progression
        final ProgressHandle ph = ProgressHandleFactory.createHandle("Updating database...", new Cancellable() {
            @Override
            public boolean cancel() {
                return handleCancel();
            }
        });
        // Code de la tâche 
        Runnable runnable;
        runnable = new Runnable() {
            @Override
            public void run() {

                ph.start();
                ph.switchToIndeterminate();

                ChartsFoldersModel chartsFolders = null;

                try {
                    chartsFolders = (ChartsFoldersModel) XMLReader.read(ChartsFoldersModel.class, ChartsFoldersModel.XML_FILE_PATH);
                } catch (IOException | JAXBException ex) {
                    IO.getErr().println(ex);
                }

                if (chartsFolders == null || chartsFolders.size() < 1) {
                    for(CatalogService provider : providers) {
                        provider.updateCatalog(null);
                    }
                    return;
                }

                List<File> kaps = new LinkedList<>();
                List<File> files;
                for (String folder : chartsFolders.getChartsFolders()) {

                    files = Utils.listFiles(new File(folder), ".kap", true);
                    if (files != null) {
                        print("Folder : " + folder);
                        kaps.addAll(files);
                    }
                }

                KAPChart newRow;
                parser = new KAPParser();
                db.clearAll();

                ph.switchToDeterminate(kaps.size());

                for (int i = 0; i < kaps.size(); i++) {
                    try {
                        newRow = parser.parse(kaps.get(i).getAbsolutePath());

                        if (!db.contains(newRow)) {
                            db.create(newRow);
                        }

                        ph.progress(i);
                        Thread.sleep(0);
                    } catch (InterruptedException ex) {
                        print("The task was cancelled");
                        Exceptions.printStackTrace(ex);
                    }
                }

                for (CatalogService provider : providers) {
                    provider.updateCatalog(db);
                }
            }
        };

        theTask = RP.create(runnable);
        theTask.addTaskListener(new TaskListener() {
            @Override
            public void taskFinished(Task task) {
                ph.finish();
            }
        });

        theTask.schedule(0); // Go !
    }

    private boolean handleCancel() {
        LOGGER.info("handleCancel");
        if (null == theTask) {
            return false;
        }

        return theTask.cancel();
    }

    public static String getICON_PATH() {
        return ICON_PATH;
    }
    
    public void print(String message) {
        IO.getOut().println("[UpdateDatabaseAction] " + message);
    }
}
