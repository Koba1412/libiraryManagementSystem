package librarymanagement;

// Factory Pattern: Book Factory
abstract class Book {
    private String title;
    private String type;

    public Book(String title, String type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return title + " (" + type + ")";
    }
}
