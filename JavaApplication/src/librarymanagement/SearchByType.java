
package librarymanagement;

// Search by Type

import java.util.ArrayList;
import java.util.List;

class SearchByType implements SearchStrategy {
    @Override
    public List<Book> search(String query, List<Book> books) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getType().equalsIgnoreCase(query)) {
                results.add(book);
            }
        }
        return results;
    }
}