package tellh.com.recyclertreeview.bean;

public class Department {
    private final String departmentName;
    private final String employeeCount;

    public Department(String departmentName, String employeeCount) {
        this.departmentName = departmentName;
        this.employeeCount = employeeCount;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getEmployeeCount() {
        return employeeCount;
    }

    public static Department create(){
        return new Department("Кофейня №1", "16 сотрудников");
    }
}
