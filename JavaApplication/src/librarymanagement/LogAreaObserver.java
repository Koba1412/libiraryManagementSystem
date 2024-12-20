package librarymanagement;

import javax.swing.JTextArea;

public class LogAreaObserver implements LogObserver {
    private JTextArea logArea;

    public LogAreaObserver(JTextArea logArea) {
        this.logArea = logArea;
    }

    @Override
    public void update(String message) {
        logArea.append(message + "\n");  // Append the log message to JTextArea
    }
}

