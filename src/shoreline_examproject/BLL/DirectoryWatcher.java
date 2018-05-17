package shoreline_examproject.BLL;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Monitors the given directories for changes.
 * @author sebok
 */
public class DirectoryWatcher
{
    private WatchService watcher;
    
    public void start() {
        Runnable r = () -> {
            watch();
        };
        
        Thread t = new Thread(r);
        t.start();
    }
    
    public void watch()
    {
        try {
            watcher = FileSystems.getDefault().newWatchService();
            
            File[] files = new File[]{new File("D:/Users/sebok/Documents/Suli/2nd_Semester_Exam_Project/files"), new File("D:/Users/sebok/Documents/Receptek")};
            
            
            WatchKey[] keys = new WatchKey[2];
            
            int n = 0;
            for (File file : files) {
                WatchKey k = file.toPath().register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
                keys[n] = k;
                n++;
            }
            
            while (true) {
                
                for (WatchKey key : keys) {
                    for (WatchEvent<?> pollEvent : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = pollEvent.kind();

                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }

                        WatchEvent<Path> ev = (WatchEvent<Path>) pollEvent;
                        Path filename = ev.context();

                        System.out.println(filename);
                    }
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(DirectoryWatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
