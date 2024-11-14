package de.skymyth.utility.pagination;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Pagination<T> {

    Map<Integer, List<T>> pages;
    int currentPage;
    int pageSize;

    public Pagination(int pageSize) {
        this.pages = new HashMap<>();
        this.currentPage = 0;
        this.pageSize = pageSize;
    }

    public Pagination(int currentPage, int pageSize) {
        this.pages = new HashMap<>();
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public void addItem(T t) {
        int pageIndex = pages.size() - 1;
        if (pageIndex < 0 || pages.get(pageIndex).size() >= pageSize) {
            pageIndex++;
            pages.put(pageIndex, new ArrayList<>(pageSize));
        }
        pages.get(pageIndex).add(t);
    }

    public void addItems(List<T> list) {
        for (T item : list) {
            addItem(item);
        }
    }

    public int currentPage() {
        return this.currentPage;
    }

    public boolean hasNextPage() {
        return pages.containsKey(currentPage + 1) && currentPage < pages.size() - 1;
    }

    public boolean hasPreviousPage() {
        return pages.containsKey(currentPage - 1) && currentPage > 0;
    }

    public List<T> getItems(int page) {
        return pages.getOrDefault(page, new ArrayList<>());
    }

}