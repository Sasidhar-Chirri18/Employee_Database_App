package employeeDBApp;

import java.util.List;
import java.util.Scanner;

import employeeDBApp.dao.EmployeeDAO;
import employeeDBApp.model.Employee;

public class Main {
	private static final EmployeeDAO dao = new EmployeeDAO();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Employee DB ---");
            System.out.println("1. Add employee");
            System.out.println("2. View employee by id");
            System.out.println("3. View all employees");
            System.out.println("4. Update employee");
            System.out.println("5. Delete employee");
            System.out.println("6. Exit");
            System.out.print("Choose: ");
            int choice = Integer.parseInt(sc.nextLine().trim());
            try {
                switch (choice) {
                    case 1 -> addEmployee(sc);
                    case 2 -> viewById(sc);
                    case 3 -> viewAll();
                    case 4 -> updateEmployee(sc);
                    case 5 -> deleteEmployee(sc);
                    case 6 -> { System.out.println("Bye!"); sc.close(); return; }
                    default -> System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void addEmployee(Scanner sc) throws Exception {
        System.out.print("Name: "); String name = sc.nextLine();
        System.out.print("Department: "); String dept = sc.nextLine();
        System.out.print("Salary: "); double sal = Double.parseDouble(sc.nextLine());
        System.out.print("Email: "); String email = sc.nextLine();

        Employee emp = new Employee(name, dept, sal, email);
        int id = dao.addEmployee(emp);
        System.out.println(id > 0 ? "Inserted with ID = " + id : "Insert failed");
    }

    private static void viewById(Scanner sc) throws Exception {
        System.out.print("ID: "); int id = Integer.parseInt(sc.nextLine());
        Employee emp = dao.getEmployeeById(id);
        System.out.println(emp == null ? "Not found" : emp);
    }

    private static void viewAll() throws Exception {
        List<Employee> list = dao.getAllEmployees();
        if (list.isEmpty()) System.out.println("No employees");
        else list.forEach(System.out::println);
    }

    private static void updateEmployee(Scanner sc) throws Exception {
        System.out.print("ID to update: "); int id = Integer.parseInt(sc.nextLine());
        Employee existing = dao.getEmployeeById(id);
        if (existing == null) {
            System.out.println("Employee not found");
            return;
        }
        System.out.print("New name (" + existing.getName() + "): "); String name = sc.nextLine();
        if (!name.isBlank()) existing.setName(name);
        System.out.print("New department (" + existing.getDepartment() + "): "); String dept = sc.nextLine();
        if (!dept.isBlank()) existing.setDepartment(dept);
        System.out.print("New salary (" + existing.getSalary() + "): "); String salStr = sc.nextLine();
        if (!salStr.isBlank()) existing.setSalary(Double.parseDouble(salStr));
        System.out.print("New email (" + existing.getEmail() + "): "); String email = sc.nextLine();
        if (!email.isBlank()) existing.setEmail(email);

        boolean ok = dao.updateEmployee(existing);
        System.out.println(ok ? "Updated" : "Update failed");
    }

    private static void deleteEmployee(Scanner sc) throws Exception {
        System.out.print("ID to delete: "); int id = Integer.parseInt(sc.nextLine());
        boolean ok = dao.deleteEmployee(id);
        System.out.println(ok ? "Deleted" : "Delete failed / not found");
    }
}


