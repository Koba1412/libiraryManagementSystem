package librarymanagement;

import java.util.List;
import java.util.ArrayList;

public class Logger {
    private static Logger instance;
    private List<LogObserver> observers = new ArrayList<>();
    private String logMessage;

    private Logger() {
        // Private constructor for Singleton
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    // Register an observer
    public void addObserver(LogObserver observer) {
        observers.add(observer);
    }

    // Remove an observer
    public void removeObserver(LogObserver observer) {
        observers.remove(observer);
    }

    // Notify all observers
    private void notifyObservers() {
        for (LogObserver observer : observers) {
            observer.update(logMessage);
        }
    }

    // Log a message and notify observers
    public void log(String message) {
        this.logMessage = message;
        notifyObservers(); // Notify all registered observers
    }

    public String getLogMessage() {
        return logMessage;
    }
}
