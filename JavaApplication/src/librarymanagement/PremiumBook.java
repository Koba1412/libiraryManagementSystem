package librarymanagement;

class PremiumBook extends BookDecorator {
    public PremiumBook(Book book) {
        super(book);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " (Premium Edition)";
    }
}