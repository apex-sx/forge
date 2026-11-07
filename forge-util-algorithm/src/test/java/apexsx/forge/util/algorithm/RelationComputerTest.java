package apexsx.forge.util.algorithm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RelationComputer 单元测试类
 * 测试内存中两个列表的关联操作，包括 Inner Join 和 Left Join
 */
@DisplayName("RelationComputer 关系计算器测试")
class RelationComputerTest {

    @Nested
    @DisplayName("Inner Join - 基于键的连接测试")
    class InnerJoinWithKeyTests {

        @Test
        @DisplayName("基本的 Inner Join - 一对一匹配")
        void testBasicInnerJoinOneToOne() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob"),
                    new Employee(3, "Charlie")
            );

            List<Department> departments = List.of(
                    new Department(1, "Engineering"),
                    new Department(2, "Sales"),
                    new Department(4, "Marketing")
            );

            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept.deptName),
                            true)
                    .on(emp -> new UniqueKey(emp.id), dept -> new UniqueKey(dept.empId))
                    .compute();

            assertEquals(2, result.size());
            assertTrue(result.stream().anyMatch(r -> r.empId == 1 && "Engineering".equals(r.deptName)));
            assertTrue(result.stream().anyMatch(r -> r.empId == 2 && "Sales".equals(r.deptName)));
            assertFalse(result.stream().anyMatch(r -> r.empId == 3));
            assertFalse(result.stream().anyMatch(r -> r.empId == 4));
        }

        @Test
        @DisplayName("Inner Join - 一对多匹配")
        void testInnerJoinOneToMany() {
            List<Student> students = List.of(
                    new Student(1, "Alice"),
                    new Student(2, "Bob")
            );

            List<Course> courses = List.of(
                    new Course(1, "Math"),
                    new Course(1, "English"),
                    new Course(2, "Physics")
            );

            List<StudentCourse> result = RelationComputer.of(students, courses,
                            (student, course) -> new StudentCourse(student.id, student.name, course.courseName),
                            true)
                    .on(s -> new UniqueKey(s.id), c -> new UniqueKey(c.studentId))
                    .compute();

            assertEquals(3, result.size());
            long aliceCourses = result.stream().filter(r -> "Alice".equals(r.studentName)).count();
            long bobCourses = result.stream().filter(r -> "Bob".equals(r.studentName)).count();
            assertEquals(2, aliceCourses);
            assertEquals(1, bobCourses);
        }

        @Test
        @DisplayName("Inner Join - 无匹配结果")
        void testInnerJoinNoMatch() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob")
            );

            List<Department> departments = List.of(
                    new Department(3, "Engineering"),
                    new Department(4, "Sales")
            );

            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept.deptName),
                            true)
                    .on(emp -> new UniqueKey(emp.id), dept -> new UniqueKey(dept.empId))
                    .compute();

            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("Inner Join - 空列表")
        void testInnerJoinEmptyList() {
            List<Employee> employees = Collections.emptyList();
            List<Department> departments = List.of(new Department(1, "Engineering"));

            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept.deptName),
                            true)
                    .on(emp -> new UniqueKey(emp.id), dept -> new UniqueKey(dept.empId))
                    .compute();

            assertEquals(0, result.size());
        }
    }

    @Nested
    @DisplayName("Left Join - 基于键的连接测试")
    class LeftJoinWithKeyTests {

        @Test
        @DisplayName("基本的 Left Join - 部分匹配")
        void testBasicLeftJoinPartialMatch() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob"),
                    new Employee(3, "Charlie")
            );

            List<Department> departments = List.of(
                    new Department(1, "Engineering"),
                    new Department(2, "Sales")
            );

            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept != null ? dept.deptName : null),
                            false)
                    .on(emp -> new UniqueKey(emp.id), dept -> new UniqueKey(dept.empId))
                    .compute();

            assertEquals(3, result.size());
            assertTrue(result.stream().anyMatch(r -> r.empId == 1 && "Engineering".equals(r.deptName)));
            assertTrue(result.stream().anyMatch(r -> r.empId == 2 && "Sales".equals(r.deptName)));
            assertTrue(result.stream().anyMatch(r -> r.empId == 3 && r.deptName == null));
        }

        @Test
        @DisplayName("Left Join - 全部无匹配")
        void testLeftJoinNoMatch() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob")
            );

            List<Department> departments = List.of(
                    new Department(3, "Engineering"),
                    new Department(4, "Sales")
            );

            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept != null ? dept.deptName : null),
                            false)
                    .on(emp -> new UniqueKey(emp.id), dept -> new UniqueKey(dept.empId))
                    .compute();

            assertEquals(2, result.size());
            assertTrue(result.stream().allMatch(r -> r.deptName == null));
        }

        @Test
        @DisplayName("Left Join - 一对多匹配")
        void testLeftJoinOneToMany() {
            List<Student> students = List.of(
                    new Student(1, "Alice"),
                    new Student(2, "Bob"),
                    new Student(3, "Charlie")
            );

            List<Course> courses = List.of(
                    new Course(1, "Math"),
                    new Course(1, "English")
            );

            List<StudentCourse> result = RelationComputer.of(students, courses,
                            (student, course) -> new StudentCourse(student.id, student.name, course != null ? course.courseName : null),
                            false)
                    .on(s -> new UniqueKey(s.id), c -> new UniqueKey(c.studentId))
                    .compute();

            assertEquals(4, result.size());
            long aliceCourses = result.stream().filter(r -> "Alice".equals(r.studentName) && r.courseName != null).count();
            long noCourse = result.stream().filter(r -> r.courseName == null).count();
            assertEquals(2, aliceCourses);
            assertEquals(2, noCourse);
        }
    }

    @Nested
    @DisplayName("过滤器测试")
    class FilterTests {

        @Test
        @DisplayName("主表预过滤")
        void testMainFilter() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob"),
                    new Employee(3, "Charlie")
            );

            List<Department> departments = List.of(
                    new Department(1, "Engineering"),
                    new Department(2, "Sales"),
                    new Department(3, "HR")
            );

            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept.deptName),
                            true)
                    .on(emp -> new UniqueKey(emp.id), dept -> new UniqueKey(dept.empId))
                    .filterMain(emp -> emp.id <= 2)
                    .compute();

            assertEquals(2, result.size());
            assertFalse(result.stream().anyMatch(r -> r.empId == 3));
        }

        @Test
        @DisplayName("副表预过滤")
        void testOtherFilter() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob"),
                    new Employee(3, "Charlie")
            );

            List<Department> departments = List.of(
                    new Department(1, "Engineering"),
                    new Department(2, "Sales"),
                    new Department(3, "HR")
            );

            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept.deptName),
                            true)
                    .on(emp -> new UniqueKey(emp.id), dept -> new UniqueKey(dept.empId))
                    .filterOther(dept -> !"HR".equals(dept.deptName))
                    .compute();

            assertEquals(2, result.size());
            assertFalse(result.stream().anyMatch(r -> "HR".equals(r.deptName)));
        }

        @Test
        @DisplayName("结果过滤")
        void testResultFilter() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob"),
                    new Employee(3, "Charlie")
            );

            List<Department> departments = List.of(
                    new Department(1, "Engineering"),
                    new Department(2, "Sales"),
                    new Department(3, "HR")
            );

            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept.deptName),
                            true)
                    .on(emp -> new UniqueKey(emp.id), dept -> new UniqueKey(dept.empId))
                    .filterResult(r -> r.empId <= 2)
                    .compute();

            assertEquals(2, result.size());
            assertFalse(result.stream().anyMatch(r -> r.empId == 3));
        }

        @Test
        @DisplayName("组合过滤器")
        void testCombinedFilters() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob"),
                    new Employee(3, "Charlie"),
                    new Employee(4, "David")
            );

            List<Department> departments = List.of(
                    new Department(1, "Engineering"),
                    new Department(2, "Sales"),
                    new Department(3, "HR"),
                    new Department(4, "Marketing")
            );

            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept.deptName),
                            true)
                    .on(emp -> new UniqueKey(emp.id), dept -> new UniqueKey(dept.empId))
                    .filterMain(emp -> emp.id <= 3)
                    .filterOther(dept -> !dept.deptName.equals("HR"))
                    .filterResult(r -> r.empId <= 2)
                    .compute();

            assertEquals(2, result.size());
            assertTrue(result.stream().allMatch(r -> r.empId <= 2));
        }

        @Test
        @DisplayName("Left Join 中的主表过滤")
        void testLeftJoinWithMainFilter() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob"),
                    new Employee(3, "Charlie")
            );

            List<Department> departments = List.of(
                    new Department(1, "Engineering")
            );

            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept != null ? dept.deptName : null),
                            false)
                    .on(emp -> new UniqueKey(emp.id), dept -> new UniqueKey(dept.empId))
                    .filterMain(emp -> emp.id <= 2)
                    .compute();

            assertEquals(2, result.size());
            assertTrue(result.stream().anyMatch(r -> r.empId == 1 && "Engineering".equals(r.deptName)));
            assertTrue(result.stream().anyMatch(r -> r.empId == 2 && r.deptName == null));
        }
    }

    @Nested
    @DisplayName("嵌套循环连接测试（无键提取器）")
    class NestedLoopJoinTests {

        @Test
        @DisplayName("使用 BiPredicate 的 Inner Join")
        void testNestedLoopInnerJoin() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob"),
                    new Employee(3, "Charlie")
            );

            List<Department> departments = List.of(
                    new Department(1, "Engineering"),
                    new Department(2, "Sales"),
                    new Department(4, "Marketing")
            );

            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept.deptName),
                            true)
                    .on((emp, dept) -> emp.id == dept.empId)
                    .compute();

            assertEquals(2, result.size());
            assertTrue(result.stream().anyMatch(r -> r.empId == 1 && "Engineering".equals(r.deptName)));
            assertTrue(result.stream().anyMatch(r -> r.empId == 2 && "Sales".equals(r.deptName)));
        }

        @Test
        @DisplayName("使用 BiPredicate 的 Left Join")
        void testNestedLoopLeftJoin() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob"),
                    new Employee(3, "Charlie")
            );

            List<Department> departments = List.of(
                    new Department(1, "Engineering"),
                    new Department(2, "Sales")
            );

            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept != null ? dept.deptName : null),
                            false)
                    .on((emp, dept) -> emp.id == dept.empId)
                    .compute();

            assertEquals(3, result.size());
            assertTrue(result.stream().anyMatch(r -> r.empId == 3 && r.deptName == null));
        }

        @Test
        @DisplayName("复杂条件的嵌套循环连接")
        void testNestedLoopComplexCondition() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob"),
                    new Employee(3, "Charlie")
            );

            List<Department> departments = List.of(
                    new Department(1, "Engineering"),
                    new Department(2, "Sales"),
                    new Department(3, "HR")
            );

            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept.deptName),
                            true)
                    .on((emp, dept) -> emp.id == dept.empId && !dept.deptName.equals("HR"))
                    .compute();

            assertEquals(2, result.size());
            assertFalse(result.stream().anyMatch(r -> "HR".equals(r.deptName)));
        }

        @Test
        @DisplayName("笛卡尔积（无条件）")
        void testCartesianProduct() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob")
            );

            List<Department> departments = List.of(
                    new Department(1, "Engineering"),
                    new Department(2, "Sales")
            );

            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept.deptName),
                            true)
                    .compute();

            assertEquals(4, result.size());
        }
    }

    @Nested
    @DisplayName("链式调用测试")
    class ChainingTests {

        @Test
        @DisplayName("两次连续的 Inner Join")
        void testChainedInnerJoins() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob")
            );

            List<Department> departments = List.of(
                    new Department(1, "Engineering"),
                    new Department(2, "Sales")
            );

            List<Location> locations = List.of(
                    new Location("Engineering", "Building A"),
                    new Location("Sales", "Building B"),
                    new Location("HR", "Building C")
            );

            List<EmployeeDetail> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept.deptName),
                            true)
                    .on(emp -> new UniqueKey(emp.id), dept -> new UniqueKey(dept.empId))
                    .join(locations,
                            (empDept, loc) -> new EmployeeDetail(empDept.empId, empDept.empName, empDept.deptName, loc.building),
                            true)
                    .on(ed -> new UniqueKey(((EmployeeDept) ed).deptName), loc -> new UniqueKey(loc.deptName))
                    .compute();

            assertEquals(2, result.size());
            assertTrue(result.stream().anyMatch(r -> r.empId == 1 && "Building A".equals(r.building)));
            assertTrue(result.stream().anyMatch(r -> r.empId == 2 && "Building B".equals(r.building)));
        }

        @Test
        @DisplayName("Inner Join 后接 Left Join")
        void testInnerJoinThenLeftJoin() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob"),
                    new Employee(3, "Charlie")
            );

            List<Department> departments = List.of(
                    new Department(1, "Engineering"),
                    new Department(2, "Sales")
            );

            List<Location> locations = List.of(
                    new Location("Engineering", "Building A")
            );

            List<EmployeeDetail> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept.deptName),
                            true)
                    .on(emp -> new UniqueKey(emp.id), dept -> new UniqueKey(dept.empId))
                    .join(locations,
                            (empDept, loc) -> new EmployeeDetail(empDept.empId, empDept.empName, empDept.deptName, loc != null ? loc.building : null),
                            false)
                    .on(ed -> new UniqueKey(((EmployeeDept) ed).deptName), loc -> new UniqueKey(loc.deptName))
                    .compute();

            assertEquals(2, result.size());
            assertTrue(result.stream().anyMatch(r -> r.empId == 1 && "Building A".equals(r.building)));
            assertTrue(result.stream().anyMatch(r -> r.empId == 2 && r.building == null));
        }
    }

    @Nested
    @DisplayName("边界条件和异常测试")
    class EdgeCaseTests {

        @Test
        @DisplayName("null 键处理")
        void testNullKeyHandling() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice"),
                    new Employee(2, "Bob"),
                    new Employee(3, "Charlie")
            );

            List<Department> departments = List.of(
                    new Department(1, "Engineering"),
                    new Department(0, "Unassigned"),
                    new Department(null, "Unknown")
            );

            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept.deptName),
                            true)
                    .on(emp -> new UniqueKey(emp.id), dept -> new UniqueKey(dept.empId))
                    .compute();

            assertEquals(1, result.size());
            assertTrue(result.stream().anyMatch(r -> r.empId == 1 && "Engineering".equals(r.deptName)));
        }

        @Test
        @DisplayName("大数据量性能测试")
        void testLargeDataSetPerformance() {
            int size = 10000;
            List<Employee> employees = new ArrayList<>(size);
            List<Department> departments = new ArrayList<>(size);

            for (int i = 0; i < size; i++) {
                employees.add(new Employee(i, "Employee" + i));
                if (i % 2 == 0) {
                    departments.add(new Department(i, "Dept" + i));
                }
            }

            long startTime = System.currentTimeMillis();
            List<EmployeeDept> result = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept.deptName),
                            true)
                    .on(emp -> new UniqueKey(emp.id), dept -> new UniqueKey(dept.empId))
                    .compute();
            long endTime = System.currentTimeMillis();

            assertEquals(size / 2, result.size());
            assertTrue(endTime - startTime < 1000, "Performance should be acceptable for 10k records");
        }

        @Test
        @DisplayName("参数非空校验 - mainList 为 null")
        void testMainListNotNull() {
            assertThrows(NullPointerException.class, () -> {
                RelationComputer.of(null, Collections.emptyList(), (e, d) -> null, true);
            });
        }

        @Test
        @DisplayName("参数非空校验 - otherList 为 null")
        void testOtherListNotNull() {
            assertThrows(NullPointerException.class, () -> {
                RelationComputer.of(Collections.emptyList(), null, (e, d) -> null, true);
            });
        }

        @Test
        @DisplayName("参数非空校验 - merger 为 null")
        void testMergerNotNull() {
            assertThrows(NullPointerException.class, () -> {
                RelationComputer.of(Collections.emptyList(), Collections.emptyList(), null, true);
            });
        }

        @Test
        @DisplayName("多次调用 compute 返回相同结果")
        void testMultipleComputeCalls() {
            List<Employee> employees = List.of(
                    new Employee(1, "Alice")
            );

            List<Department> departments = List.of(
                    new Department(1, "Engineering")
            );

            RelationComputer<Employee, Department, EmployeeDept> computer = RelationComputer.of(employees, departments,
                            (emp, dept) -> new EmployeeDept(emp.id, emp.name, dept.deptName),
                            true)
                    .on(emp -> new UniqueKey(emp.id), dept -> new UniqueKey(dept.empId));

            List<EmployeeDept> result1 = computer.compute();
            List<EmployeeDept> result2 = computer.compute();

            assertEquals(result1.size(), result2.size());
            assertEquals(result1.get(0).empId, result2.get(0).empId);
        }
    }

    @Nested
    @DisplayName("实际业务场景测试")
    class BusinessScenarioTests {

        @Test
        @DisplayName("订单和订单项的关联")
        void testOrderAndOrderItems() {
            List<Order> orders = List.of(
                    new Order(1001, "Customer A"),
                    new Order(1002, "Customer B"),
                    new Order(1003, "Customer C")
            );

            List<OrderItem> orderItems = List.of(
                    new OrderItem(1001, "Product X", 2),
                    new OrderItem(1001, "Product Y", 1),
                    new OrderItem(1002, "Product Z", 3)
            );

            List<OrderDetail> result = RelationComputer.of(orders, orderItems,
                            (order, item) -> new OrderDetail(order.orderId, order.customerName, item.productName, item.quantity),
                            true)
                    .on(o -> new UniqueKey(o.orderId), oi -> new UniqueKey(oi.orderId))
                    .compute();

            assertEquals(3, result.size());
            long customerAOrders = result.stream().filter(r -> "Customer A".equals(r.customerName)).count();
            assertEquals(2, customerAOrders);
        }

        @Test
        @DisplayName("用户和用户角色的关联（多对多）")
        void testUserAndRoles() {
            List<User> users = List.of(
                    new User(1, "Alice"),
                    new User(2, "Bob"),
                    new User(3, "Charlie")
            );

            List<UserRole> roles = List.of(
                    new UserRole(1, "ADMIN"),
                    new UserRole(1, "USER"),
                    new UserRole(2, "USER"),
                    new UserRole(2, "EDITOR")
            );

            List<UserRoleDetail> result = RelationComputer.of(users, roles,
                            (user, role) -> new UserRoleDetail(user.id, user.name, role.roleName),
                            true)
                    .on(u -> new UniqueKey(u.id), r -> new UniqueKey(r.userId))
                    .compute();

            assertEquals(4, result.size());
            long aliceRoles = result.stream().filter(r -> "Alice".equals(r.userName)).count();
            long bobRoles = result.stream().filter(r -> "Bob".equals(r.userName)).count();
            long charlieRoles = result.stream().filter(r -> "Charlie".equals(r.userName)).count();
            assertEquals(2, aliceRoles);
            assertEquals(2, bobRoles);
            assertEquals(0, charlieRoles);
        }

        @Test
        @DisplayName("商品和库存的 Left Join")
        void testProductAndInventoryLeftJoin() {
            List<Product> products = List.of(
                    new Product(1, "Laptop"),
                    new Product(2, "Phone"),
                    new Product(3, "Tablet")
            );

            List<Inventory> inventories = List.of(
                    new Inventory(1, 100),
                    new Inventory(2, 50)
            );

            List<ProductInventory> result = RelationComputer.of(products, inventories,
                            (product, inventory) -> new ProductInventory(product.id, product.name, inventory != null ? inventory.stock : 0),
                            false)
                    .on(p -> new UniqueKey(p.id), i -> new UniqueKey(i.productId))
                    .compute();

            assertEquals(3, result.size());
            assertTrue(result.stream().anyMatch(r -> r.productName.equals("Laptop") && r.stock == 100));
            assertTrue(result.stream().anyMatch(r -> r.productName.equals("Phone") && r.stock == 50));
            assertTrue(result.stream().anyMatch(r -> r.productName.equals("Tablet") && r.stock == 0));
        }
    }

    // ==================== 测试数据类 ====================

    static class Employee {
        int id;
        String name;

        Employee(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    static class Department {
        Integer empId;
        String deptName;

        Department(Integer empId, String deptName) {
            this.empId = empId;
            this.deptName = deptName;
        }
    }

    static class EmployeeDept {
        int empId;
        String empName;
        String deptName;

        EmployeeDept(int empId, String empName, String deptName) {
            this.empId = empId;
            this.empName = empName;
            this.deptName = deptName;
        }
    }

    static class Student {
        int id;
        String name;

        Student(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    static class Course {
        int studentId;
        String courseName;

        Course(int studentId, String courseName) {
            this.studentId = studentId;
            this.courseName = courseName;
        }
    }

    static class StudentCourse {
        int studentId;
        String studentName;
        String courseName;

        StudentCourse(int studentId, String studentName, String courseName) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.courseName = courseName;
        }
    }

    static class Location {
        String deptName;
        String building;

        Location(String deptName, String building) {
            this.deptName = deptName;
            this.building = building;
        }
    }

    static class EmployeeDetail {
        int empId;
        String empName;
        String deptName;
        String building;

        EmployeeDetail(int empId, String empName, String deptName, String building) {
            this.empId = empId;
            this.empName = empName;
            this.deptName = deptName;
            this.building = building;
        }
    }

    static class Order {
        int orderId;
        String customerName;

        Order(int orderId, String customerName) {
            this.orderId = orderId;
            this.customerName = customerName;
        }
    }

    static class OrderItem {
        int orderId;
        String productName;
        int quantity;

        OrderItem(int orderId, String productName, int quantity) {
            this.orderId = orderId;
            this.productName = productName;
            this.quantity = quantity;
        }
    }

    static class OrderDetail {
        int orderId;
        String customerName;
        String productName;
        int quantity;

        OrderDetail(int orderId, String customerName, String productName, int quantity) {
            this.orderId = orderId;
            this.customerName = customerName;
            this.productName = productName;
            this.quantity = quantity;
        }
    }

    static class User {
        int id;
        String name;

        User(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    static class UserRole {
        int userId;
        String roleName;

        UserRole(int userId, String roleName) {
            this.userId = userId;
            this.roleName = roleName;
        }
    }

    static class UserRoleDetail {
        int userId;
        String userName;
        String roleName;

        UserRoleDetail(int userId, String userName, String roleName) {
            this.userId = userId;
            this.userName = userName;
            this.roleName = roleName;
        }
    }

    static class Product {
        int id;
        String name;

        Product(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    static class Inventory {
        int productId;
        int stock;

        Inventory(int productId, int stock) {
            this.productId = productId;
            this.stock = stock;
        }
    }

    static class ProductInventory {
        int productId;
        String productName;
        int stock;

        ProductInventory(int productId, String productName, int stock) {
            this.productId = productId;
            this.productName = productName;
            this.stock = stock;
        }
    }
}
