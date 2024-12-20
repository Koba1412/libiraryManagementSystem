package librarymanagement;
class RareEditionBook extends BookDecorator {
    public RareEditionBook(Book book) {
        super(book);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " (Rare Edition)";
    }
}
