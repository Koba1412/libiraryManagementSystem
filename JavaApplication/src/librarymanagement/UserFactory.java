package librarymanagement;

class UserFactory {
    public static User createUser(String name, String role) {
        switch (role) {
            case "Admin":
                return new AdminUser(name);
            case "Regular":
                return new RegularUser(name);
            default:
                throw new IllegalArgumentException("Unknown user role");
        }
    }
}