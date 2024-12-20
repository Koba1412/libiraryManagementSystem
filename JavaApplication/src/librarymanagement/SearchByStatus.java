/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package librarymanagement;

import java.util.ArrayList;
import java.util.List;

class SearchByStatus implements SearchStrategy {
    @Override
    public List<Book> search(String query, List<Book> books) {
        // Assuming books have status attribute in future
        return new ArrayList<>();
    }
}