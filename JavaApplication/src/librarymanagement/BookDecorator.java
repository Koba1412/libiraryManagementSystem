package librarymanagement;

// Decorator Pattern: Book Decorators
abstract class BookDecorator extends Book {
    protected Book book;

    public BookDecorator(Book book) {
        super(book.getTitle(), book.getType());
        this.book = book;
    }

    @Override
    public String getDescription() {
        return book.getDescription();
    }
}
