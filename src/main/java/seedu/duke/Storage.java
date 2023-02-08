package seedu.duke;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Storage {
    private String filePath;

    /**
     * Class constructor.
     *
     * @param filePath the path of the text file to read/store tasks
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads data from a file, creates new Task objects and stores them in an ArrayList<Task>.
     *
     * @return the arrayList<Task> of Tasks stored in the file
     * @throws IOException if an error occurs during reading the file
     */
    public ArrayList<Task> readFromFile() throws IOException {
        ArrayList<Task> taskList = new ArrayList<>();
        try {
            File f = new File(this.filePath);
            Scanner s = new Scanner(f);
            while (s.hasNext()) {
                String task = s.nextLine();
                char taskType = task.charAt(1);
                char taskStatus = task.charAt(4);
                Task newTask;

                if (taskType == 'T') {
                    newTask = new ToDo(task.substring(7));
                }
                else if (taskType == 'D') {
                    LocalDateTime by = LocalDateTime.parse(task.substring(task.indexOf("(by: ") + 5, task.length() - 1),
                            DateTimeFormatter.ofPattern("HH:mm, EEEE, MMM dd yyyy"));
                    newTask = new Deadline(task.substring(7, task.indexOf(" (by:")), by);
                }
                else if (taskType == 'E'){
                    LocalDateTime from = LocalDateTime.parse(task.substring(task.indexOf("(from: ") + 7, task.indexOf("to: ") - 1),
                            DateTimeFormatter.ofPattern("HH:mm, EEEE, MMM dd yyyy"));
                    LocalDateTime to = LocalDateTime.parse(task.substring(task.indexOf("to: ") + 4, task.length() - 1),
                            DateTimeFormatter.ofPattern("HH:mm, EEEE, MMM dd yyyy"));

                    newTask = new Event(task.substring(7, task.indexOf(" (from:")), from, to);
                }
                else {
                    System.out.println("There is no such option!");
                    newTask = null;
                }

                if (taskStatus == 'X') {
                    newTask.mark();
                }

                taskList.add(newTask);
            }
            s.close();
        } catch (FileNotFoundException e) {
            try {
                Files.createDirectories(Paths.get(filePath));
                File file = new File(filePath);
            } catch (IOException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        }
        return taskList;
    }

    /**
     * Writes the data stored in ArrayList<Task> to a file specified by 'filepath'.
     *
     * @param taskList contains the Tasks to be written in the file
     */
    public void writeToFile(TaskList taskList) {
        try {
            FileWriter file = new FileWriter(this.filePath);
            for (int i = 0; i < taskList.getSize(); i++) {
                file.write(taskList.getTask(i) + System.lineSeparator());
            }
            file.close();
            System.out.println("Your updated list is stored in the file.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
