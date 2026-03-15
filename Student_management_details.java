import java.io.*;
import java.util.*;
class Students implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private int age;
    private String course;

    public Students(String id, String name, int age, String course) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.course = course;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setCourse(String course) { this.course = course; }

    @Override
    public String toString() {
        return String.format("| %-10s | %-20s | %-5d | %-15s |", id, name, age, course);
    }
}

public class Student_management_details {
    private static ArrayList<Student> studentList = new ArrayList<>();
    private static final String FILE_NAME = "students.txt";
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadFromFile();


        if (studentList.isEmpty()) {
            studentList.add(new Student("101", "Rahul", 20, "CS"));
            studentList.add(new Student("102", "Anjali", 22, "IT"));
            saveToFile();
        }

        while (true) {
            System.out.println("\n===== STUDENT SYSTEM (File Enabled) =====");
            System.out.println("1. Add Student    2. View All");
            System.out.println("3. Update Record  4. Delete Record");
            System.out.println("5. Search Student 6. Sort Records");
            System.out.println("7. Exit");
            System.out.print("Select Option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewStudents(studentList);
                case 3 -> updateStudent();
                case 4 -> deleteStudent();
                case 5 -> searchStudent();
                case 6 -> sortStudents();
                case 7 -> {
                    saveToFile();
                    System.out.println("Data Saved. Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(studentList);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            studentList = (ArrayList<Student>) ois.readObject();
        } catch (Exception e) {
            // Error handling
        }
    }

    private static void searchStudent() {
        if (studentList.isEmpty()) { System.out.println("No records."); return; }
        System.out.println("\nSearch by: 1. ID  2. Name");
        int type = getIntInput();
        System.out.print("Enter search term: ");
        String term = sc.next().toLowerCase();

        boolean found = false;
        for (Student s : studentList) {
            if ((type == 1 && s.getId().toLowerCase().contains(term)) ||
                    (type == 2 && s.getName().toLowerCase().contains(term))) {
                if (!found) printHeader();
                System.out.println(s);
                found = true;
            }
        }
        if (!found) System.out.println("No matching student found.");
    }

    private static void sortStudents() {
        if (studentList.isEmpty()) { System.out.println("List is empty!"); return; }
        System.out.println("\nSort by: 1. Name (A-Z)  2. Age (Low-High)");
        int sortChoice = getIntInput();

        if (sortChoice == 1) {
            studentList.sort(Comparator.comparing(Student::getName));
            System.out.println("Sorted by Name.");
        } else if (sortChoice == 2) {
            studentList.sort(Comparator.comparingInt(Student::getAge));
            System.out.println("Sorted by Age.");
        }
        viewStudents(studentList);
    }

    private static void addStudent() {
        System.out.print("Enter ID: "); String id = sc.next();
        sc.nextLine();
        System.out.print("Enter Name: "); String name = sc.nextLine();
        System.out.print("Enter Age: "); int age = getIntInput();
        System.out.print("Enter Course: "); String course = sc.next();

        studentList.add(new Student(id, name, age, course));
        saveToFile();
        System.out.println("Student Added & Saved!");
    }

    private static void viewStudents(ArrayList<Student> list) {
        if (list.isEmpty()) { System.out.println("No records found."); return; }
        printHeader();
        for (Student s : list) System.out.println(s);
        System.out.println("-".repeat(60));
    }

    private static void updateStudent() {
        System.out.print("Enter Student ID to update: ");
        String id = sc.next();
        for (Student s : studentList) {
            if (s.getId().equalsIgnoreCase(id)) {
                System.out.print("New Name: "); sc.nextLine();
                s.setName(sc.nextLine());
                System.out.print("New Age: "); s.setAge(getIntInput());
                System.out.print("New Course: "); s.setCourse(sc.next());
                saveToFile();
                System.out.println("Updated!");
                return;
            }
        }
        System.out.println("ID not found.");
    }

    private static void deleteStudent() {
        System.out.print("Enter ID to delete: ");
        String id = sc.next();
        if (studentList.removeIf(s -> s.getId().equalsIgnoreCase(id))) {
            saveToFile();
            System.out.println("Deleted.");
        } else {
            System.out.println("ID not found.");
        }
    }

    private static void printHeader() {
        System.out.println("-".repeat(60));
        System.out.printf("| %-10s | %-20s | %-5s | %-15s |\n", "ID", "Name", "Age", "Course");
        System.out.println("-".repeat(60));
    }

    private static int getIntInput() {
        while (!sc.hasNextInt()) {
            System.out.print("Enter a number: ");
            sc.next();
        }
        return sc.nextInt();
    }
}