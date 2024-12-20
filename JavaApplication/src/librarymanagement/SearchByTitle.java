package librarymanagement;

import java.util.ArrayList;
import java.util.List;

class SearchByTitle implements SearchStrategy {
    @Override
    public List<Book> search(String query, List<Book> books) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(query)) {
                results.add(book);
            }
        }
        return results;
    }
}

