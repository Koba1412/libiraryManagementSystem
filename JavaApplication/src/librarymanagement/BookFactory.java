package librarymanagement;

class BookFactory {
    public static Book createBook(String title, String type) {
        switch (type) {
            case "Software Engineering":
                return new SoftwareEngineeringBook(title);
            case "Management":
                return new ManagementBook(title);
            case "Artificial Intelligence":
                return new ArtificialIntelligenceBook(title);
            default:
                throw new IllegalArgumentException("Unknown book type");
        }
    }
}