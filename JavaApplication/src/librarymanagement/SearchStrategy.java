package librarymanagement;

import java.util.List;

// Strategy Pattern: Search
interface SearchStrategy {
    List<Book> search(String query, List<Book> books);
}
