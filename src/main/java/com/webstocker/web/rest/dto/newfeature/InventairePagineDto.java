package com.webstocker.web.rest.dto.newfeature;

import com.webstocker.domain.Inventaire;

import java.util.List;

public class InventairePagineDto {

    private List<Inventaire> inventaires;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public List<Inventaire> getInventaires() {
        return inventaires;
    }

    public void setInventaires(List<Inventaire> inventaires) {
        this.inventaires = inventaires;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

